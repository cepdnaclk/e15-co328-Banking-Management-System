package reportBeans;

/**
 *
 * @author RISITH-PC
 */
public class FixedScheduleDeposit {
    private int id;
    private String name;
    private String takenDate;
    private double amount;
    private int period;

    public FixedScheduleDeposit() {
    }

    public FixedScheduleDeposit(int id, String name, String takenDate, double amount, int period) {
        this.id = id;
        this.name = name;
        this.takenDate = takenDate;
        this.amount = amount;
        this.period = period;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTakenDate() {
        return takenDate;
    }

    public void setTakenDate(String takenDate) {
        this.takenDate = takenDate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getPeriod() {
        return period+" months";
    }

    public void setPeriod(int period) {
        this.period = period;
    }
      
}
