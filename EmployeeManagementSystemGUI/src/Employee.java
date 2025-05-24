import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class Employee implements Serializable {
    private static final long serialVersionUID = 1L;
    protected String id;
    protected String name;
    protected String department;
    protected double baseSalary;
    protected int performanceRating;
    protected Date hireDate;
    protected String lastAppraisal;
    protected String lastWarning;

    public Employee(String id, String name, String department, double baseSalary) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.baseSalary = baseSalary;
        this.performanceRating = 3; // Default rating
        this.hireDate = new Date(); // Current date
    }

    public abstract double calculateSalary();

    public void setPerformanceRating(int rating) {
        if (rating >= 1 && rating <= 5) {
            this.performanceRating = rating;
        } else {
            System.out.println("Invalid rating. Must be between 1 and 5.");
        }
    }

    public void issueWarning(String warning) {
        this.lastWarning = warning;
        System.out.println("Warning issued to " + name + ": " + warning);
    }

    public void issueAppraisal(String appraisal) {
        this.lastAppraisal = appraisal;
        System.out.println("Appraisal issued to " + name + ": " + appraisal);
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getDepartment() { return department; }
    public double getBaseSalary() { return baseSalary; }
    public int getPerformanceRating() { return performanceRating; }
    public Date getHireDate() { return hireDate; }
    public String getLastAppraisal() { return lastAppraisal; }
    public String getLastWarning() { return lastWarning; }

    // Setters
    public void setName(String name) { this.name = name; }
    public void setDepartment(String department) { this.department = department; }
    public void setBaseSalary(double baseSalary) { this.baseSalary = baseSalary; }
    public void setHireDate(Date hireDate) { this.hireDate = hireDate; }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return String.format("ID: %s, Name: %s, Department: %s, Base Salary: %.2f, Performance: %d, Hire Date: %s",
                id, name, department, baseSalary, performanceRating, sdf.format(hireDate));
    }
}