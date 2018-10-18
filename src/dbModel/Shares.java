
package dbModel;

public class Shares {
    private int shaId;
    private int memId;
    private String shaDate;
    private String shaTime;
    private double amount;
    
    private double balance;
    private String name;
    
    public Shares() {
    }

    public Shares(int shaId, int memId, String shaDate, String shaTime, double amount) {
        this.shaId = shaId;
        this.memId = memId;
        this.shaDate = shaDate;
        this.shaTime = shaTime;
        this.amount = amount;
    }

    public int getShaId() {
        return shaId;
    }

    public void setShaId(int shaId) {
        this.shaId = shaId;
    }

    public int getMemId() {
        return memId;
    }

    public void setMemId(int memId) {
        this.memId = memId;
    }

    public String getShaDate() {
        return shaDate;
    }

    public void setShaDate(String shaDate) {
        this.shaDate = shaDate;
    }

    public String getShaTime() {
        return shaTime;
    }

    public void setShaTime(String shaTime) {
        this.shaTime = shaTime;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    } 
}
