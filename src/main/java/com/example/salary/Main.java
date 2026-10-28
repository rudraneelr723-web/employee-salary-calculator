package com.example.salary;

<<<<<<< HEAD
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
=======
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class Main extends JFrame {
    private final EmployeeDao dao = new EmployeeDao();
    private final JTextField idField = new JTextField(10);
    private final JTextField nameField = new JTextField(18);
    private final JTextField basicField = new JTextField(12);
    private final JTextField allowanceField = new JTextField(12);
    private final JTextField searchField = new JTextField(18);
    private final JLabel grossValue = new JLabel("₹0.00");
    private final JLabel deductionValue = new JLabel("₹0.00");
    private final JLabel netValue = new JLabel("₹0.00");
    private final JLabel status = new JLabel("Ready");
    private final DefaultTableModel model = new DefaultTableModel(
            new String[]{"ID", "Name", "Basic", "HRA", "DA", "Other Allowances", "Gross", "Deductions", "Net"}, 0) {
        @Override public boolean isCellEditable(int row, int column) { return false; }
    };
    private final JTable table = new JTable(model);

    public Main() {
        super("Employee Salary Management");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1050, 650));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(12, 12));

        JPanel root = new JPanel(new BorderLayout(12, 12));
        root.setBorder(new EmptyBorder(18, 18, 12, 18));
        root.setBackground(new Color(245, 247, 251));
        setContentPane(root);

        JLabel heading = new JLabel("Employee Salary Management");
        heading.setFont(new Font("SansSerif", Font.BOLD, 26));
        JLabel subtitle = new JLabel("Java Swing • JDBC • MySQL");
        subtitle.setForeground(new Color(95, 105, 120));
        JPanel titlePanel = new JPanel(new GridLayout(2, 1, 0, 4));
        titlePanel.setOpaque(false);
        titlePanel.add(heading);
        titlePanel.add(subtitle);
        root.add(titlePanel, BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout(12, 12));
        center.setOpaque(false);
        root.add(center, BorderLayout.CENTER);

        JPanel left = new JPanel(new BorderLayout(8, 8));
        left.setPreferredSize(new Dimension(290, 400));
        left.setBackground(Color.WHITE);
        left.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(225, 229, 237)),
                new EmptyBorder(14, 14, 14, 14)));
        center.add(left, BorderLayout.WEST);

        JLabel formTitle = new JLabel("Employee details");
        formTitle.setFont(new Font("SansSerif", Font.BOLD, 17));
        left.add(formTitle, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(0, 1, 4, 4));
        form.setOpaque(false);
        addField(form, "Employee ID", idField);
        addField(form, "Employee name", nameField);
        addField(form, "Basic salary (₹)", basicField);
        addField(form, "Other allowances (₹)", allowanceField);
        JPanel buttons = new JPanel(new GridLayout(0, 2, 7, 7));
        buttons.setOpaque(false);
        JButton add = button("Add");
        JButton update = button("Update");
        JButton delete = button("Delete");
        JButton clear = button("Clear");
        JButton calculate = button("Calculate");
        JButton report = button("> ₹50,000");
        buttons.add(add); buttons.add(update);
        buttons.add(delete); buttons.add(clear);
        buttons.add(calculate); buttons.add(report);

        JPanel formWrap = new JPanel(new BorderLayout(0, 12));
        formWrap.setOpaque(false);
        formWrap.add(form, BorderLayout.NORTH);
        formWrap.add(buttons, BorderLayout.CENTER);
        left.add(formWrap, BorderLayout.CENTER);

        JPanel right = new JPanel(new BorderLayout(8, 8));
        right.setOpaque(false);
        center.add(right, BorderLayout.CENTER);

        JPanel metrics = new JPanel(new GridLayout(1, 3, 10, 0));
        metrics.setOpaque(false);
        metrics.add(metric("Gross salary", grossValue));
        metrics.add(metric("Deductions", deductionValue));
        metrics.add(metric("Net salary", netValue));
        right.add(metrics, BorderLayout.NORTH);

        JPanel tablePanel = new JPanel(new BorderLayout(8, 8));
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(225, 229, 237)),
                new EmptyBorder(10, 10, 10, 10)));
        JPanel search = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        search.setOpaque(false);
        search.add(new JLabel("Search ID/name:"));
        search.add(searchField);
        JButton searchButton = button("Search");
        JButton refreshButton = button("Refresh");
        search.add(searchButton);
        search.add(refreshButton);
        tablePanel.add(search, BorderLayout.NORTH);

        table.setRowHeight(27);
        table.setAutoCreateRowSorter(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setFillsViewportHeight(true);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);
        right.add(tablePanel, BorderLayout.CENTER);

        status.setBorder(new EmptyBorder(4, 2, 0, 2));
        status.setForeground(new Color(75, 85, 100));
        root.add(status, BorderLayout.SOUTH);

        add.addActionListener(e -> runDb(() -> {
            Employee employee = readForm();
            dao.insert(employee);
            showSalary(employee.salary());
            refresh("");
            setStatus("Employee added successfully.");
        }));
        update.addActionListener(e -> runDb(() -> {
            Employee employee = readForm();
            if (dao.update(employee)) {
                showSalary(employee.salary());
                refresh("");
                setStatus("Employee updated successfully.");
            } else message("Employee ID not found.");
        }));
        delete.addActionListener(e -> runDb(() -> {
            int id = parseId();
            int choice = JOptionPane.showConfirmDialog(this, "Delete employee " + id + "?",
                    "Confirm deletion", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                if (dao.delete(id)) {
                    clearForm();
                    refresh("");
                    setStatus("Employee deleted.");
                } else message("Employee ID not found.");
            }
        }));
        clear.addActionListener(e -> clearForm());
        calculate.addActionListener(e -> {
            try {
                showSalary(Salary.calculate(parseMoney(basicField, "Basic salary"),
                        parseMoney(allowanceField, "Other allowances")));
                setStatus("Salary calculated. Save with Add or Update.");
            } catch (IllegalArgumentException ex) { message(ex.getMessage()); }
        });
        report.addActionListener(e -> runDb(() -> populate(dao.grossAbove50000())));
        searchButton.addActionListener(e -> runDb(() -> refresh(searchField.getText())));
        refreshButton.addActionListener(e -> runDb(() -> refresh("")));
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0) loadSelected();
        });

        runDb(() -> refresh(""));
    }

    private JPanel metric(String title, JLabel value) {
        JPanel panel = new JPanel(new GridLayout(2, 1, 0, 6));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(225, 229, 237)),
                new EmptyBorder(12, 14, 12, 14)));
        JLabel label = new JLabel(title);
        label.setForeground(new Color(95, 105, 120));
        value.setFont(new Font("SansSerif", Font.BOLD, 20));
        panel.add(label); panel.add(value);
        return panel;
    }

    private JButton button(String text) {
        JButton b = new JButton(text);
        b.setFocusPainted(false);
        return b;
    }

    private void addField(JPanel panel, String label, JTextField field) {
        JPanel p = new JPanel(new BorderLayout(0, 4));
        p.setOpaque(false);
        JLabel l = new JLabel(label);
        l.setForeground(new Color(75, 85, 100));
        p.add(l, BorderLayout.NORTH);
        p.add(field, BorderLayout.CENTER);
        panel.add(p);
    }

    private Employee readForm() {
        int id = parseId();
        String name = nameField.getText().trim();
        if (name.isEmpty()) throw new IllegalArgumentException("Enter employee name.");
        Salary salary = Salary.calculate(parseMoney(basicField, "Basic salary"),
                parseMoney(allowanceField, "Other allowances"));
        return new Employee(id, name, salary);
    }

    private int parseId() {
        try { return Integer.parseInt(idField.getText().trim()); }
        catch (NumberFormatException e) { throw new IllegalArgumentException("Employee ID must be a whole number."); }
    }

    private BigDecimal parseMoney(JTextField field, String label) {
        try {
            BigDecimal n = new BigDecimal(field.getText().trim());
            if (n.signum() < 0) throw new IllegalArgumentException(label + " cannot be negative.");
            return n;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Enter a valid " + label.toLowerCase() + ".");
        }
    }

    private void showSalary(Salary s) {
        grossValue.setText(format(s.gross()));
        deductionValue.setText(format(s.deductions()));
        netValue.setText(format(s.net()));
    }

    private String format(BigDecimal n) { return "₹" + String.format("%,.2f", n); }

    private void refresh(String search) throws SQLException { populate(dao.all(search)); }

    private void populate(List<Employee> employees) {
        model.setRowCount(0);
        for (Employee e : employees) {
            Salary s = e.salary();
            model.addRow(new Object[]{e.id(), e.name(), format(s.basic()), format(s.hra()),
                    format(s.da()), format(s.allowances()), format(s.gross()),
                    format(s.deductions()), format(s.net())});
        }
        setStatus(employees.size() + " employee record(s).");
    }

    private void loadSelected() {
        int row = table.convertRowIndexToModel(table.getSelectedRow());
        idField.setText(String.valueOf(model.getValueAt(row, 0)));
        nameField.setText(String.valueOf(model.getValueAt(row, 1)));
        runDb(() -> {
            Employee e = dao.find(Integer.parseInt(idField.getText()));
            if (e != null) {
                basicField.setText(e.salary().basic().toPlainString());
                allowanceField.setText(e.salary().allowances().toPlainString());
                showSalary(e.salary());
            }
        });
    }

    private void clearForm() {
        idField.setText("");
        nameField.setText("");
        basicField.setText("");
        allowanceField.setText("");
        grossValue.setText("₹0.00");
        deductionValue.setText("₹0.00");
        netValue.setText("₹0.00");
        table.clearSelection();
        setStatus("Form cleared.");
    }

    private void runDb(SqlTask task) {
        try { task.run(); }
        catch (SQLException e) {
            message("Database error: " + e.getMessage()
                    + "\\nCheck MySQL is running and DB_URL / DB_USER / DB_PASSWORD are correct.");
        } catch (IllegalArgumentException e) { message(e.getMessage()); }
    }

    private void message(String text) {
        JOptionPane.showMessageDialog(this, text, "Employee Salary System", JOptionPane.INFORMATION_MESSAGE);
    }

    private void setStatus(String text) { status.setText(text); }

    @FunctionalInterface private interface SqlTask { void run() throws SQLException; }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
            catch (Exception ignored) {}
            new Main().setVisible(true);
        });
>>>>>>> 4f51e5b6ca72d578a28d4391265552c02b40399c
    }
}
