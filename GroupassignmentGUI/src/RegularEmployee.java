public class RegularEmployee extends Employee {
    private static final long serialVersionUID = 1L;
    private double overtimePay;

    public RegularEmployee(String id, String name, String department, double baseSalary, double overtimePay) {
        super(id, name, department, baseSalary);
        this.overtimePay = overtimePay;
    }

    public void setOvertimePay(double overtimePay) {
        this.overtimePay = overtimePay;
    }

    @Override
    public double calculateSalary() {
        return baseSalary + overtimePay;
    }

    @Override
    public String toString() {
        return super.toString() + ", Type: Regular, Salary: " + calculateSalary();
    }
}