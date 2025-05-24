import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class EmployeeManagementGUI {
    private EmployeeManagementSystem ems;
    private JTextArea textArea;

    public EmployeeManagementGUI(EmployeeManagementSystem ems) {
        this.ems = ems;
        createAndShowGUI();
    }
    
    //method to sort data by ID
    private void sortEmployeesById() {
        List<Employee> employees = ems.getEmployees();
        employees.sort(Comparator.comparing(Employee::getId)); 
    }
    
    //method to sort data by Name
    private void sortEmployeesByName() {
        List<Employee> employees = ems.getEmployees();
        employees.sort(Comparator.comparing(Employee::getName)); 
    }


    private void createAndShowGUI() {
        JFrame frame = new JFrame("Employee Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        
        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(0, 2));

        // Load Data from file Button
        JButton loadButton = new JButton("Load Data");
        loadButton.addActionListener(e -> {
            ems.loadEmployees();
            sortEmployeesById();                      // Sort by ID after loading
            displayEmployees();
        });
        buttonPanel.add(loadButton);

        // Add Employee Button
        JButton addButton = new JButton("Add Employee");
        addButton.addActionListener(e -> addEmployee());
        buttonPanel.add(addButton);

        // Update Employee Button
        JButton updateButton = new JButton("Update Employee");
        updateButton.addActionListener(e -> updateEmployee());
        buttonPanel.add(updateButton);

        // Delete Employee Button
        JButton deleteButton = new JButton("Delete Employee");
        deleteButton.addActionListener(e -> deleteEmployee());
        buttonPanel.add(deleteButton);

        // Query Employee Button
        JButton queryButton = new JButton("Query Employee");
        queryButton.addActionListener(e -> queryEmployee());
        buttonPanel.add(queryButton);

        // List All Employees Button
        JButton listButton = new JButton("List All Employees");
        listButton.addActionListener(e -> displayEmployeesByName());
        buttonPanel.add(listButton);

        // Manage Performance Button
        JButton managePerformanceButton = new JButton("Manage Performance");
        managePerformanceButton.addActionListener(e -> managePerformance());
        buttonPanel.add(managePerformanceButton);

        // Save Employees Button
        JButton saveButton = new JButton("Save Employees");
        saveButton.addActionListener(e -> {
            ems.saveEmployees();
            JOptionPane.showMessageDialog(frame, "Employee data saved successfully.");
        });
        buttonPanel.add(saveButton);

        // Exit Button
        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(e -> System.exit(0));
        buttonPanel.add(exitButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);
        frame.add(panel);
        frame.setVisible(true);
    }

    private void displayEmployees() {
        textArea.setText(""); // Clear previous text
        List<Employee> employees = ems.getEmployees(); // Get all employees
        if (employees.isEmpty()) {
            textArea.append("No employees found.\n");
        } else {
            for (Employee emp : employees) {
                textArea.append(emp.toString() + "\n");
            }
        }
    }
    
    private void displayEmployeesByName() {
        textArea.setText(""); // Clear previous text
        List<Employee> employees = ems.getEmployees(); // Get all employees
        sortEmployeesByName();									//sort data by Name in Ascending order
        if (employees.isEmpty()) {
            textArea.append("No employees found.\n");
        } else {
            for (Employee emp : employees) {
                textArea.append(emp.toString() + "\n");
            }
        }
    }
    
    

    private void addEmployee() {
        // Implement the logic to add an employee using a dialog
        // Similar to the text-based addEmployeeMenu method
        String id = JOptionPane.showInputDialog("Enter employee ID:");
        String name = JOptionPane.showInputDialog("Enter employee name:");
        String department = JOptionPane.showInputDialog("Enter department:");
        double baseSalary = Double.parseDouble(JOptionPane.showInputDialog("Enter base salary:"));
        String type = JOptionPane.showInputDialog("Enter employee type (Regular/Manager/Intern):");

        Employee employee = null;
        if (type.equalsIgnoreCase("Regular")) {
            double overtime = Double.parseDouble(JOptionPane.showInputDialog("Enter overtime pay:"));
            employee = new RegularEmployee(id, name, department, baseSalary, overtime);
        } else if (type.equalsIgnoreCase("Manager")) {
            double bonus = Double.parseDouble(JOptionPane.showInputDialog("Enter bonus:"));
            employee = new Manager(id, name, department, baseSalary, bonus);
        } else if (type.equalsIgnoreCase("Intern")) {
            String endDateStr = JOptionPane.showInputDialog("Enter internship end date (yyyy-MM-dd):");
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date endDate = sdf.parse(endDateStr);
                employee = new Intern(id, name, department, baseSalary, endDate);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Invalid date format. Employee not added.");
                return;
            }
        }

        if (employee != null) {
            ems.addEmployee(employee);
            JOptionPane.showMessageDialog(null, "Employee added successfully.");
        }
    }

    private void updateEmployee() {
        String id = JOptionPane.showInputDialog("Enter employee ID to update:");
        Employee emp = ems.findEmployeeById(id);

        if (emp == null) {
            JOptionPane.showMessageDialog(null, "Employee not found.");
            return;
        }

        String name = JOptionPane.showInputDialog("Enter new name (leave blank to keep current):");
        String department = JOptionPane.showInputDialog("Enter new department (leave blank to keep current):");
        String salaryStr = JOptionPane.showInputDialog("Enter new base salary (leave blank to keep current):");

        if (!name.isEmpty()) emp.setName(name);
        if (!department.isEmpty()) emp.setDepartment(department);
        if (!salaryStr.isEmpty()) {
            try {
                double salary = Double.parseDouble(salaryStr);
                if (salary >= 0) emp.setBaseSalary(salary);
                else JOptionPane.showMessageDialog(null, "Salary cannot be negative. Salary not updated.");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid salary format. Salary not updated.");
            }
        }
        JOptionPane.showMessageDialog(null, "Employee updated successfully.");
    }

    private void deleteEmployee() {
        String id = JOptionPane.showInputDialog("Enter employee ID to delete:");
        ems.removeEmployee(id);
        JOptionPane.showMessageDialog(null, "Employee removed successfully.");
    }

    private void queryEmployee() {
        String id = JOptionPane.showInputDialog("Enter employee ID:");
        Employee emp = ems.findEmployeeById(id);
        if (emp != null) {
            JOptionPane.showMessageDialog(null, emp.toString());
        } else {
            JOptionPane.showMessageDialog(null, "Employee not found.");
        }
    }

    private void managePerformance() {
        String id = JOptionPane.showInputDialog("Enter employee ID:");
        Employee emp = ems.findEmployeeById(id);

        if (emp == null) {
            JOptionPane.showMessageDialog(null, "Employee not found.");
            return;
        }

        String[] options = {"Set Performance Rating", "Issue Warning", "Issue Appraisal", "View Current Performance"};
        int choice = JOptionPane.showOptionDialog(null, "Manage Performance for: " + emp.getName(),
                "Performance Management", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        switch (choice) {
            case 0:
                int rating = Integer.parseInt(JOptionPane.showInputDialog("Enter new performance rating (1-5):"));
                emp.setPerformanceRating(rating);
                JOptionPane.showMessageDialog(null, "Performance rating updated.");
                break;
            case 1:
                String warning = JOptionPane.showInputDialog("Enter warning message:");
                emp.issueWarning(warning);
                break;
            case 2:
                String appraisal = JOptionPane.showInputDialog("Enter appraisal message:");
                emp.issueAppraisal(appraisal);
                break;
            case 3:
                String performanceInfo = "Current performance rating: " + emp.getPerformanceRating();
                if (emp.getLastWarning() != null) {
                    performanceInfo += "\nLast warning: " + emp.getLastWarning();
                }
                if (emp.getLastAppraisal() != null) {
                    performanceInfo += "\nLast appraisal: " + emp.getLastAppraisal();
                }
                JOptionPane.showMessageDialog(null, performanceInfo);
                break;
            default:
                break;
        }
    }
}