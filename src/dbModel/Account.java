
package dbModel;

public class Account {
    private int accId;
    private int memId;
    private double norBalance;
    private double comBalance;

    public Account() {
    }

    public Account(int accId, int memId, double norBalance, double comBalance) {
        this.accId = accId;
        this.memId = memId;
        this.norBalance = norBalance;
        this.comBalance = comBalance;
    }

    public int getAccId() {
        return accId;
    }

    public void setAccId(int accId) {
        this.accId = accId;
    }

    public int getMemId() {
        return memId;
    }

    public void setMemId(int memId) {
        this.memId = memId;
    }

    public double getNorBalance() {
        return norBalance;
    }

    public void setNorBalance(double norBalance) {
        this.norBalance = norBalance;
    }

    public double getComBalance() {
        return comBalance;
    }

    public void setComBalance(double comBalance) {
        this.comBalance = comBalance;
    }   
}
