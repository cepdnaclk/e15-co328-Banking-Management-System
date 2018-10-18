
package dbModel;

public class Fixed {
    private int fixId;
    private int accId;
    private String fixDate;
    private String fixTime;
    private int period;
    private double amount;
    private boolean isMaturity;
    private boolean isWithdraw;
    private boolean isBond;
    private double interest;

    public Fixed() {
    }

    public Fixed(int fixId, int accId, String fixDate, String fixTime, int period, double amount, boolean isMaturity, boolean isWithdraw, boolean isBond, double interest) {
        this.fixId = fixId;
        this.accId = accId;
        this.fixDate = fixDate;
        this.fixTime = fixTime;
        this.period = period;
        this.amount = amount;
        this.isMaturity = isMaturity;
        this.isWithdraw = isWithdraw;
        this.isBond = isBond;
        this.interest = interest;
    }

    public int getFixId() {
        return fixId;
    }

    public void setFixId(int fixId) {
        this.fixId = fixId;
    }

    public int getAccId() {
        return accId;
    }

    public void setAccId(int accId) {
        this.accId = accId;
    }

    public String getFixDate() {
        return fixDate;
    }

    public void setFixDate(String fixDate) {
        this.fixDate = fixDate;
    }

    public String getFixTime() {
        return fixTime;
    }

    public void setFixTime(String fixTime) {
        this.fixTime = fixTime;
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

    public boolean isIsMaturity() {
        return isMaturity;
    }

    public void setIsMaturity(boolean isMaturity) {
        this.isMaturity = isMaturity;
    }

    public boolean isIsWithdraw() {
        return isWithdraw;
    }

    public void setIsWithdraw(boolean isWithdraw) {
        this.isWithdraw = isWithdraw;
    }

    public boolean isIsBond() {
        return isBond;
    }

    public void setIsBond(boolean isBond) {
        this.isBond = isBond;
    }

    public double getInterest() {
        return interest;
    }

    public void setInterest(double interest) {
        this.interest = interest;
    }   
}
