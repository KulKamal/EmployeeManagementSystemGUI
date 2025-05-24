import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class EmployeeManagementSystem {
    private List<Employee> employees;
    private String dataFile;

    public EmployeeManagementSystem(String dataFile) {
        this.dataFile = dataFile;
        this.employees = new ArrayList<>();
    }

    public List<Employee> getEmployees() {
        return employees; // Return the list of employees
    }
    
    public void loadEmployees() {
        employees.clear();
        File file = new File(dataFile);
        if (!file.exists()) {
            System.out.println("Data file not found. Generating sample data...");
            generateSampleData();
            saveEmployees();
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(dataFile))) {
            String line = reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 7) continue; // Skip malformed lines

                String type = parts[0];
                String id = parts[1];
                String name = parts[2];
                String department = parts[3];
                double baseSalary = Double.parseDouble(parts[4]);
                int performanceRating = Integer.parseInt(parts[5]);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date hireDate = sdf.parse(parts[6]);
                String extraField1 = parts.length > 7 ? parts[7] : "";
                String extraField2 = parts.length > 8 ? parts[8] : "";

                Employee employee = null;
                switch (type) {
                    case "Regular":
                        double overtimePay = extraField1.isEmpty() ? 0 : Double.parseDouble(extraField1);
                        employee = new RegularEmployee(id, name, department, baseSalary, overtimePay);
                        break;
                    case "Manager":
                        double bonus = extraField1.isEmpty() ? 0 : Double.parseDouble(extraField1);
                        employee = new Manager(id, name, department, baseSalary, bonus);
                        break;
                    case "Intern":
                        Date endDate = extraField1.isEmpty() ? new Date() : sdf.parse(extraField1);
                        employee = new Intern(id, name, department, baseSalary, endDate);
                        break;
                    default:
                        continue;
                }

                employee.setPerformanceRating(performanceRating);
                employee.setHireDate(hireDate);
                if (!extraField2.isEmpty()) {
                    if (extraField2.startsWith("Warning:")) {
                        employee.issueWarning(extraField2.substring(8));
                    } else if (extraField2.startsWith("Appraisal:")) {
                        employee.issueAppraisal(extraField2.substring(10));
                    }
                }
                employees.add(employee);
            }
            System.out.println("Employee data loaded successfully. Total employees: " + employees.size());
        } catch (Exception e) {
            System.out.println("Error loading employee data: " + e.getMessage());
        }
    }

    public void saveEmployees() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(dataFile))) {
            // Write header
            writer.write("type,id,name,department,baseSalary,performanceRating,hireDate,extraField1,extraField2\n");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            for (Employee emp : employees) {
                StringBuilder line = new StringBuilder();
                if (emp instanceof RegularEmployee) {
                    RegularEmployee re = (RegularEmployee) emp;
                    line.append("Regular,").append(emp.getId()).append(",").append(emp.getName()).append(",")
                        .append(emp.getDepartment()).append(",").append(emp.getBaseSalary()).append(",")
                        .append(emp.getPerformanceRating()).append(",").append(sdf.format(emp.getHireDate())).append(",")
                        .append(re.calculateSalary() - re.getBaseSalary()).append(",");
                } else if (emp instanceof Manager) {
                    Manager m = (Manager) emp;
                    line.append("Manager,").append(emp.getId()).append(",").append(emp.getName()).append(",")
                        .append(emp.getDepartment()).append(",").append(emp.getBaseSalary()).append(",")
                        .append(emp.getPerformanceRating()).append(",").append(sdf.format(emp.getHireDate())).append(",")
                        .append(m.calculateSalary() - m.getBaseSalary()).append(",");
                } else if (emp instanceof Intern) {
                    Intern i = (Intern) emp;
                    line.append("Intern,").append(emp.getId()).append(",").append(emp.getName()).append(",")
                        .append(emp.getDepartment()).append(",").append(emp.getBaseSalary()).append(",")
                        .append(emp.getPerformanceRating()).append(",").append(sdf.format(emp.getHireDate())).append(",")
                        .append(sdf.format(i.getEndDate())).append(",");
                }

                if (emp.getLastWarning() != null) {
                    line.append("Warning:").append(emp.getLastWarning());
                } else if (emp.getLastAppraisal() != null) {
                    line.append("Appraisal:").append(emp.getLastAppraisal());
                }
                writer.write(line.toString() + "\n");
            }
            System.out.println("Employee data saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving employee data: " + e.getMessage());
        }
    }

    public void addEmployee(Employee employee) {
        if (findEmployeeById(employee.getId()) != null) {
            System.out.println("Employee with ID " + employee.getId() + " already exists.");
            return;
        }
        employees.add(employee);
        System.out.println("Employee added successfully: " + employee.getName());
    }

    public void removeEmployee(String id) {
        Iterator<Employee> iterator = employees.iterator();
        while (iterator.hasNext()) {
            Employee emp = iterator.next();
            if (emp.getId().equals(id)) {
                iterator.remove();
                System.out.println("Employee removed successfully: " + emp.getName());
                return;
            }
        }
        System.out.println("Employee with ID " + id + " not found.");
    }

    public Employee findEmployeeById(String id) {
        for (Employee emp : employees) {
            if (emp.getId().equals(id)) {
                return emp;
            }
        }
        return null;
    }

    public List<Employee> findEmployeesByName(String name) {
        List<Employee> result = new ArrayList<>();
        for (Employee emp : employees) {
            if (emp.getName().equalsIgnoreCase(name)) {
                result.add(emp);
            }
        }
        return result;
    }

    public List<Employee> findEmployeesByPerformance(int rating) {
        List<Employee> result = new ArrayList<>();
        for (Employee emp : employees) {
            if (emp.getPerformanceRating() == rating) {
                result.add(emp);
            }
        }
        return result;
    }

    public void listAllEmployees() {
        if (employees.isEmpty()) {
            System.out.println("No employees in the system.");
            return;
        }
        System.out.println("\nList of all employees:");
        for (Employee emp : employees) {
            System.out.println(emp.toString());
        }
    }

    public void updateEmployee(String id, String name, String department, double baseSalary) {
        Employee emp = findEmployeeById(id);
        if (emp != null) {
            emp.setName(name);
            emp.setDepartment(department);
            emp.setBaseSalary(baseSalary);
            System.out.println("Employee updated successfully: " + emp.getName());
        } else {
            System.out.println("Employee with ID " + id + " not found.");
        }
    }

    public void generateSampleData() {
        employees.clear();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            addEmployee(new RegularEmployee("E001", "Basanta Bhusan Khadka", "IT", 60000, 5000));
            addEmployee(new Manager("E002", "Kul Chandra Kamal", "HR", 70000, 10000));
            addEmployee(new Intern("E003", "Roshan Gurung", "Marketing", 20000, sdf.parse("2025-12-31")));
            addEmployee(new RegularEmployee("E004", "Sandesh Bhatta", "Finance", 55000, 3000));
            addEmployee(new Manager("E005", "Gyani Bohara", "Operations", 75000, 12000));
            addEmployee(new RegularEmployee("E006", "Ramu Kaka", "Sales", 60000, 4000));
            addEmployee(new Manager("E007", "Hari Bahadur", "Engineering", 80000, 15000));
            addEmployee(new Intern("E008", "Madan Krishna", "Design", 25000, sdf.parse("2025-12-31")));
            addEmployee(new RegularEmployee("E009", "Bramanandam Brown", "Support", 52000, 2000));
            addEmployee(new Manager("E010", "Kevin Hart", "Product", 78000, 11000));
            System.out.println("Sample data generated with " + employees.size() + " employees.");
        } catch (Exception e) {
            System.out.println("Error creating sample data: " + e.getMessage());
        }
    }
}