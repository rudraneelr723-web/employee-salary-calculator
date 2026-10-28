package com.example.salary;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDao {
    public void insert(Employee e) throws SQLException {
        String sql = """
            INSERT INTO employees
            (employee_id, employee_name, basic_salary, other_allowances, hra, da,
             gross_salary, pf_deduction, professional_tax, total_deductions, net_salary)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;
        try (Connection c = Database.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            bindSalary(p, e, 1);
            p.executeUpdate();
        }
    }

    public boolean update(Employee e) throws SQLException {
        String sql = """
            UPDATE employees SET employee_name=?, basic_salary=?, other_allowances=?,
            hra=?, da=?, gross_salary=?, pf_deduction=?, professional_tax=?,
            total_deductions=?, net_salary=? WHERE employee_id=?
            """;
        try (Connection c = Database.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            Salary s = e.salary();
            p.setString(1, e.name());
            p.setBigDecimal(2, s.basic());
            p.setBigDecimal(3, s.allowances());
            p.setBigDecimal(4, s.hra());
            p.setBigDecimal(5, s.da());
            p.setBigDecimal(6, s.gross());
            p.setBigDecimal(7, s.pf());
            p.setBigDecimal(8, s.professionalTax());
            p.setBigDecimal(9, s.deductions());
            p.setBigDecimal(10, s.net());
            p.setInt(11, e.id());
            return p.executeUpdate() > 0;
        }
    }

    public boolean delete(int id) throws SQLException {
        try (Connection c = Database.getConnection();
             PreparedStatement p = c.prepareStatement("DELETE FROM employees WHERE employee_id=?")) {
            p.setInt(1, id);
            return p.executeUpdate() > 0;
        }
    }

    public List<Employee> all(String search) throws SQLException {
        List<Employee> result = new ArrayList<>();
        String sql = """
            SELECT employee_id, employee_name, basic_salary, other_allowances
            FROM employees
            WHERE CAST(employee_id AS CHAR) LIKE ? OR employee_name LIKE ?
            ORDER BY employee_id
            """;
        try (Connection c = Database.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            String term = "%" + search.trim() + "%";
            p.setString(1, term);
            p.setString(2, term);
            try (ResultSet r = p.executeQuery()) {
                while (r.next()) {
                    Salary s = Salary.calculate(r.getBigDecimal("basic_salary"),
                            r.getBigDecimal("other_allowances"));
                    result.add(new Employee(r.getInt("employee_id"), r.getString("employee_name"), s));
                }
            }
        }
        return result;
    }

    public Employee find(int id) throws SQLException {
        String sql = "SELECT employee_id, employee_name, basic_salary, other_allowances FROM employees WHERE employee_id=?";
        try (Connection c = Database.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, id);
            try (ResultSet r = p.executeQuery()) {
                if (!r.next()) return null;
                return new Employee(r.getInt("employee_id"), r.getString("employee_name"),
                        Salary.calculate(r.getBigDecimal("basic_salary"), r.getBigDecimal("other_allowances")));
            }
        }
    }

    public List<Employee> grossAbove50000() throws SQLException {
        List<Employee> result = new ArrayList<>();
        String sql = """
            SELECT employee_id, employee_name, basic_salary, other_allowances
            FROM employees WHERE gross_salary > 50000 ORDER BY gross_salary DESC
            """;
        try (Connection c = Database.getConnection(); PreparedStatement p = c.prepareStatement(sql);
             ResultSet r = p.executeQuery()) {
            while (r.next()) {
                result.add(new Employee(r.getInt("employee_id"), r.getString("employee_name"),
                        Salary.calculate(r.getBigDecimal("basic_salary"), r.getBigDecimal("other_allowances"))));
            }
        }
        return result;
    }

    private void bindSalary(PreparedStatement p, Employee e, int start) throws SQLException {
        Salary s = e.salary();
        p.setInt(start, e.id());
        p.setString(start + 1, e.name());
        p.setBigDecimal(start + 2, s.basic());
        p.setBigDecimal(start + 3, s.allowances());
        p.setBigDecimal(start + 4, s.hra());
        p.setBigDecimal(start + 5, s.da());
        p.setBigDecimal(start + 6, s.gross());
        p.setBigDecimal(start + 7, s.pf());
        p.setBigDecimal(start + 8, s.professionalTax());
        p.setBigDecimal(start + 9, s.deductions());
        p.setBigDecimal(start + 10, s.net());
    }
}
