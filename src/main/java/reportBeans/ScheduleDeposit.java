package reportBeans;

/**
 *
 * @author RISITH-PC
 */
public class ScheduleDeposit {
    private int memberId;
    private String name;
    private double balance;

    public ScheduleDeposit() {
    }

    public ScheduleDeposit(int memberId, String name, double balance) {
        this.memberId = memberId;
        this.name = name;
        this.balance = balance;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
    
    
}
