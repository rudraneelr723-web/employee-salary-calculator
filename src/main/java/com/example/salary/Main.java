package com.example.salary;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner in = new Scanner(System.in);
    private static final EmployeeDao dao = new EmployeeDao();

    public static void main(String[] args) {
        System.out.println("=== EMPLOYEE SALARY MANAGEMENT ===");
        while (true) {
            System.out.println("""

                    1. Add employee
                    2. Update employee
                    3. Delete employee
                    4. Search employees
                    5. List all employees
                    6. Calculate salary
                    7. Show employees with gross salary > 50000
                    0. Exit
                    """);
            int choice = number("Choose: ");
            try {
                switch (choice) {
                    case 1 -> add();
                    case 2 -> update();
                    case 3 -> delete();
                    case 4 -> list(dao.all(text("Search by ID/name: ")));
                    case 5 -> list(dao.all(""));
                    case 6 -> calculate();
                    case 7 -> list(dao.grossAbove50000());
                    case 0 -> { System.out.println("Goodbye."); return; }
                    default -> System.out.println("Invalid option.");
                }
            } catch (SQLException e) {
                System.out.println("Database error: " + e.getMessage());
                System.out.println("Check MySQL is running, schema.sql is imported, and DB_URL/DB_USER/DB_PASSWORD are correct.");
            } catch (IllegalArgumentException e) {
                System.out.println("Input error: " + e.getMessage());
            }
        }
    }

    private static void add() throws SQLException {
        Employee e = readEmployee();
        dao.insert(e);
        System.out.println("Employee added.");
        print(e);
    }

    private static void update() throws SQLException {
        Employee e = readEmployee();
        if (dao.update(e)) {
            System.out.println("Employee updated.");
            print(e);
        } else System.out.println("Employee ID not found.");
    }

    private static void delete() throws SQLException {
        int id = number("Employee ID to delete: ");
        if (text("Delete employee " + id + "? (y/n): ").equalsIgnoreCase("y")) {
            System.out.println(dao.delete(id) ? "Employee deleted." : "Employee ID not found.");
        } else System.out.println("Cancelled.");
    }

    private static Employee readEmployee() {
        int id = number("Employee ID: ");
        String name = text("Employee name: ");
        if (name.isBlank()) throw new IllegalArgumentException("Name cannot be empty.");
        BigDecimal basic = money("Basic salary: ");
        BigDecimal allowances = money("Other allowances: ");
        return new Employee(id, name, Salary.calculate(basic, allowances));
    }

    private static void calculate() {
        Salary s = Salary.calculate(money("Basic salary: "), money("Other allowances: "));
        System.out.println("HRA: " + s.hra() + " | DA: " + s.da() + " | Gross: " + s.gross()
                + " | PF: " + s.pf() + " | Professional tax: " + s.professionalTax()
                + " | Deductions: " + s.deductions() + " | Net: " + s.net());
    }

    private static BigDecimal money(String prompt) {
        try {
            BigDecimal n = new BigDecimal(text(prompt));
            if (n.signum() < 0) throw new IllegalArgumentException("Salary cannot be negative.");
            return n;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Enter a valid amount.");
        }
    }

    private static int number(String prompt) {
        try { return Integer.parseInt(text(prompt)); }
        catch (NumberFormatException e) { throw new IllegalArgumentException("Enter a whole number."); }
    }

    private static String text(String prompt) {
        System.out.print(prompt);
        return in.nextLine().trim();
    }

    private static void list(List<Employee> employees) {
        if (employees.isEmpty()) {
            System.out.println("No employee records found.");
            return;
        }
        System.out.printf("%-6s %-22s %12s %12s %12s %12s %12s %12s%n",
                "ID", "Name", "Basic", "HRA", "DA", "Gross", "Deduct.", "Net");
        System.out.println("-".repeat(106));
        for (Employee e : employees) {
            Salary s = e.salary();
            System.out.printf("%-6d %-22s %12s %12s %12s %12s %12s %12s%n",
                    e.id(), e.name(), s.basic(), s.hra(), s.da(), s.gross(), s.deductions(), s.net());
        }
        System.out.println(employees.size() + " record(s).");
    }

    private static void print(Employee e) {
        Salary s = e.salary();
        System.out.printf("ID: %d | Name: %s | Basic: %s | HRA: %s | DA: %s | Allowances: %s%n",
                e.id(), e.name(), s.basic(), s.hra(), s.da(), s.allowances());
        System.out.printf("Gross: %s | PF: %s | Professional tax: %s | Deductions: %s | Net: %s%n",
                s.gross(), s.pf(), s.professionalTax(), s.deductions(), s.net());
    }
}