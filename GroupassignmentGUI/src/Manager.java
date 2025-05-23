public class Manager extends Employee {
    private static final long serialVersionUID = 1L;
    private double bonus;

    public Manager(String id, String name, String department, double baseSalary, double bonus) {
        super(id, name, department, baseSalary);
        this.bonus = bonus;
    }

    public void setBonus(double bonus) {
        this.bonus = bonus;
    }

    @Override
    public double calculateSalary() {
        return baseSalary + bonus;
    }

    @Override
    public String toString() {
        return super.toString() + ", Type: Manager, Salary: " + calculateSalary();
    }
}