
package dbModel;

public class LoanTaken {
    private int takenId;
    private int memId;
    private String loanName;
    private boolean method;
    private double rate;
    private int rateType;
    private int fineType;
    private int guaranteeType;
    private String takenDate;
    private String takenTime;
    private int period;  
    private double amount;
    private double balance;

    public LoanTaken() {
    }

    public LoanTaken(int takenId, int memId, String loanName, boolean method, double rate, int rateType, int fineType, int guaranteeType, String takenDate, String takenTime, int period, double amount, double balance) {
        this.takenId = takenId;
        this.memId = memId;
        this.loanName = loanName;
        this.method = method;
        this.rate = rate;
        this.rateType = rateType;
        this.fineType = fineType;
        this.guaranteeType = guaranteeType;
        this.takenDate = takenDate;
        this.takenTime = takenTime;
        this.period = period;
        this.amount = amount;
        this.balance = balance;
    }

    public int getTakenId() {
        return takenId;
    }

    public void setTakenId(int takenId) {
        this.takenId = takenId;
    }

    public int getMemId() {
        return memId;
    }

    public void setMemId(int memId) {
        this.memId = memId;
    }

    public String getLoanName() {
        return loanName;
    }

    public void setLoanName(String loanName) {
        this.loanName = loanName;
    }

    public boolean isMethod() {
        return method;
    }

    public void setMethod(boolean method) {
        this.method = method;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public int getRateType() {
        return rateType;
    }

    public void setRateType(int rateType) {
        this.rateType = rateType;
    }

    public int getFineType() {
        return fineType;
    }

    public void setFineType(int fineType) {
        this.fineType = fineType;
    }

    public int getGuaranteeType() {
        return guaranteeType;
    }

    public void setGuaranteeType(int guaranteeType) {
        this.guaranteeType = guaranteeType;
    }

    public String getTakenDate() {
        return takenDate;
    }

    public void setTakenDate(String takenDate) {
        this.takenDate = takenDate;
    }

    public String getTakenTime() {
        return takenTime;
    }

    public void setTakenTime(String takenTime) {
        this.takenTime = takenTime;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
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
}
