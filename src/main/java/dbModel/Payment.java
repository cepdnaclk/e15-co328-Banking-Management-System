
package dbModel;

public class Payment {
    private int payId;
    private int takenId;
    private String payDate;
    private String payTime;
    private double amount;
    private double fine;
    private double interest;

    public Payment() {
    }

    public Payment(int payId, int takenId, String payDate, String payTime, double amount, double fine, double interest) {
        this.payId = payId;
        this.takenId = takenId;
        this.payDate = payDate;
        this.payTime = payTime;
        this.amount = amount;
        this.fine = fine;
        this.interest = interest;
    }

    public int getPayId() {
        return payId;
    }

    public void setPayId(int payId) {
        this.payId = payId;
    }

    public int getTakenId() {
        return takenId;
    }

    public void setTakenId(int takenId) {
        this.takenId = takenId;
    }

    public String getPayDate() {
        return payDate;
    }

    public void setPayDate(String payDate) {
        this.payDate = payDate;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getFine() {
        return fine;
    }

    public void setFine(double fine) {
        this.fine = fine;
    }

    public double getInterest() {
        return interest;
    }

    public void setInterest(double interest) {
        this.interest = interest;
    }
    
}
