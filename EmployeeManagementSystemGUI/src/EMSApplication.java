import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

public class EMSApplication {
    private static Scanner scanner = new Scanner(System.in);
    private static EmployeeManagementSystem ems;
    private static final String DATA_FILE = "employee_data.csv";

    public static void main(String[] args) {
        ems = new EmployeeManagementSystem(DATA_FILE);
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            System.out.println("No data file found. Generating sample data...");
            ems.generateSampleData();
            ems.saveEmployees();
        } else {
            ems.loadEmployees();
        }

        System.out.println("Choose interface mode:");
        System.out.println("1. Text-Based Interface");
        System.out.println("2. Graphical User Interface");
        int choice = getIntInput("Enter your choice: ");

        if (choice == 1) {
            runTextBasedInterface();
        } else if (choice == 2) {
            new EmployeeManagementGUI(ems);
        } else {
            System.out.println("Invalid choice. Exiting.");
        }
        scanner.close();
    }

    private static void runTextBasedInterface() {
        boolean exit = false;
        while (!exit) {
            displayMainMenu();
            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    ems.loadEmployees();
                    break;
                case 2:
                    addEmployeeMenu();
                    break;
                case 3:
                    updateEmployeeMenu();
                    break;
                case 4:
                    deleteEmployeeMenu();
                    break;
                case 5:
                    queryEmployeeMenu();
                    break;
                case 6:
                    ems.listAllEmployees();
                    break;
                case 7:
                    managePerformanceMenu();
                    break;
                case 8:
                    ems.saveEmployees();
                    break;
                case 9:
                    exit = true;
                    System.out.println("Exiting Employee Management System. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void displayMainMenu() {
        System.out.println("\nEmployee Management System");
        System.out.println("1. Load employee data from file");
        System.out.println("2. Add new employee");
        System.out.println("3. Update employee information");
        System.out.println("4. Delete employee");
        System.out.println("5. Query employee details");
        System.out.println("6. List all employees");
        System.out.println("7. Manage employee performance");
        System.out.println("8. Save employee data to file");
        System.out.println("9. Exit");
    }

    private static void addEmployeeMenu() {
        System.out.println("\nAdd New Employee");
        System.out.println("1. Regular Employee");
        System.out.println("2. Manager");
        System.out.println("3. Intern");
        System.out.println("4. Back to main menu");

        int typeChoice = getIntInput("Enter employee type: ");
        if (typeChoice == 4) return;

        String id = getStringInput("Enter employee ID: ");
        String name = getStringInput("Enter employee name: ");
        String department = getStringInput("Enter department: ");
        double baseSalary = getDoubleInput("Enter base salary: ");

        Employee employee = null;
        switch (typeChoice) {
            case 1:
                double overtime = getDoubleInput("Enter overtime pay: ");
                employee = new RegularEmployee(id, name, department, baseSalary, overtime);
                break;
            case 2:
                double bonus = getDoubleInput("Enter bonus: ");
                employee = new Manager(id, name, department, baseSalary, bonus);
                break;
            case 3:
                try {
                    String endDateStr = getStringInput("Enter internship end date (yyyy-MM-dd): ");
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date endDate = sdf.parse(endDateStr);
                    employee = new Intern(id, name, department, baseSalary, endDate);
                } catch (Exception e) {
                    System.out.println("Invalid date format. Employee not added.");
                    return;
                }
                break;
            default:
                System.out.println("Invalid choice. Employee not added.");
                return;
        }
        ems.addEmployee(employee);
    }

    private static void updateEmployeeMenu() {
        String id = getStringInput("Enter employee ID to update: ");
        Employee emp = ems.findEmployeeById(id);

        if (emp == null) {
            System.out.println("Employee not found.");
            return;
        }

        System.out.println("Current employee details:");
        System.out.println(emp);

        String name = getStringInput("Enter new name (leave blank to keep current): ");
        String department = getStringInput("Enter new department (leave blank to keep current): ");
        String salaryStr = getStringInput("Enter new base salary (leave blank to keep current): ");

        if (!name.isEmpty()) emp.setName(name);
        if (!department.isEmpty()) emp.setDepartment(department);
        if (!salaryStr.isEmpty()) {
            try {
                double salary = Double.parseDouble(salaryStr);
                if (salary >= 0) emp.setBaseSalary(salary);
                else System.out.println("Salary cannot be negative. Salary not updated.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid salary format. Salary not updated.");
            }
        }
        System.out.println("Employee updated successfully.");
    }

    private static void deleteEmployeeMenu() {
        String id = getStringInput("Enter employee ID to delete: ");
        ems.removeEmployee(id);
    }

    private static void queryEmployeeMenu() {
        System.out.println("\nQuery Employees");
        System.out.println("1. By ID");
        System.out.println("2. By Name");
        System.out.println("3. By Performance Rating");
        System.out.println("4. Back to main menu");

        int choice = getIntInput("Enter your choice: ");
        if (choice == 4) return;

        switch (choice) {
            case 1:
                String id = getStringInput("Enter employee ID: ");
                Employee emp = ems.findEmployeeById(id);
                if (emp != null) {
                    System.out.println("Employee found:");
                    System.out.println(emp);
                } else {
                    System.out.println("Employee not found.");
                }
                break;
            case 2:
                String name = getStringInput("Enter employee name: ");
                List<Employee> byName = ems.findEmployeesByName(name);
                if (!byName.isEmpty()) {
                    System.out.println("Employees found:");
                    for (Employee e : byName) {
                        System.out.println(e);
                    }
                } else {
                    System.out.println("No employees found with that name.");
                }
                break;
            case 3:
                int rating = getIntInput("Enter performance rating (1-5): ");
                if (rating >= 1 && rating <= 5) {
                    List<Employee> byRating = ems.findEmployeesByPerformance(rating);
                    if (!byRating.isEmpty()) {
                        System.out.println("Employees with rating " + rating + ":");
                        for (Employee e : byRating) {
                            System.out.println(e);
                        }
                    } else {
                        System.out.println("No employees found with that rating.");
                    }
                } else {
                    System.out.println("Invalid rating. Must be between 1 and 5.");
                }
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private static void managePerformanceMenu() {
        String id = getStringInput("Enter employee ID: ");
        Employee emp = ems.findEmployeeById(id);

        if (emp == null) {
            System.out.println("Employee not found.");
            return;
        }

        System.out.println("\nManage Performance for: " + emp.getName());
        System.out.println("1. Set performance rating");
        System.out.println("2. Issue warning");
        System.out.println("3. Issue appraisal");
        System.out.println("4. View current performance");
        System.out.println("5. Back to main menu");

        int choice = getIntInput("Enter your choice: ");
        if (choice == 5) return;

        switch (choice) {
            case 1:
                int rating = getIntInput("Enter new performance rating (1-5): ");
                emp.setPerformanceRating(rating);
                System.out.println("Performance rating updated.");
                break;
            case 2:
                String warning = getStringInput("Enter warning message: ");
                emp.issueWarning(warning);
                break;
            case 3:
                String appraisal = getStringInput("Enter appraisal message: ");
                emp.issueAppraisal(appraisal);
                break;
            case 4:
                System.out.println("Current performance rating: " + emp.getPerformanceRating());
                if (emp.getLastWarning() != null) {
                    System.out.println("Last warning: " + emp.getLastWarning());
                }
                if (emp.getLastAppraisal() != null) {
                    System.out.println("Last appraisal: " + emp.getLastAppraisal());
                }
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    private static double getDoubleInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                double value = Double.parseDouble(scanner.nextLine());
                if (value >= 0) return value;
                System.out.println("Value cannot be negative.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    private static String getStringInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) return input;
            System.out.println("Input cannot be empty.");
        }
    }
}
