import java.text.SimpleDateFormat;
import java.util.Date;

public class Intern extends Employee {
    private static final long serialVersionUID = 1L;
    private Date endDate;

    public Intern(String id, String name, String department, double baseSalary, Date endDate) {
        super(id, name, department, baseSalary);
        this.endDate = endDate;
    }

    @Override
    public double calculateSalary() {
        return baseSalary;
    }

    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return super.toString() + ", Type: Intern, Salary: " + calculateSalary() + ", Internship End Date: " + sdf.format(endDate);
    }
}