
package dbSubModel;

import java.text.DecimalFormat;


public class NextPayment { 
    //--------- Next Payment Details ---------//
    private int takenId;
    private String loanName;
    private boolean method;
    private double rate;
    private int rateType;
    private int fineType;    
    private int period;  
    private double amount;
    private double balance;
    private int dates;
    private int numOfDates;    //--- this is number of dates since last payment date ----//
    
    //-------- derive calculations ---------//
    private double interestAmount;
    private double fineAmount;
    private double installAmount;
    private double payAmount;
    
    //-------- Constants and Another Objects------------------//
    private static double dailyRate;
    private static final double yDates = 365.0;
    private static final double yMonths = 12.0;
    private static DecimalFormat numFormat = new DecimalFormat("0.00"); 
    
    public NextPayment() {
        
    }

    public String getLoanName() {
        return loanName;
    }

    public void setLoanName(String loanName) {
        this.loanName = loanName;
    }

    public void setMethod(boolean method) {
        this.method = method;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public void setRateType(int rateType) {
        this.rateType = rateType;
    }

    public void setFineType(int fineType) {
        this.fineType = fineType;
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

    public void setDates(int dates) {
        this.dates = dates;
    }
      
    public void setNumOfDates(int numOfDates) {
        this.numOfDates = numOfDates;
    }

    //--- Calculations of next Payments ---//
    
// rateType ---- 0 = daily,   1 = monthly,  2 = annually,      //
    public double getInterestAmount() {
        switch (rateType) {
            case 0:
                dailyRate = rate / 100.0;
                break;
            case 1:
                dailyRate = (rate * yMonths) / (100.0 * yDates);
                break;
            case 2:
                dailyRate = (rate / (100.0 * yDates));
                break;
        }
        
        if(method){
            interestAmount = balance*dailyRate*numOfDates;
           
        }else{
            interestAmount = amount*dailyRate*numOfDates;
        }
        return Double.parseDouble(numFormat.format(interestAmount));
    }

    // fineType----- 0 = nonType, 1 = costType, 2 = rateType ------//
    public double getFineAmount() {
        if (numOfDates > 30) {
            switch (fineType) {
                case 0:
                    fineAmount = 0;
                    break;
                case 1:
                    fineAmount = (numOfDates - 30) * 2;   // important. This constant should be get from database //
                    break;
                case 2:
                    if(method){
                        fineAmount = balance*(2/(yDates*100.0))*(numOfDates - 30);
                    }else{
                        fineAmount = amount*(2/(yDates*100.0))*(numOfDates - 30);
                    }
                    break;
            }
        } else {
            fineAmount = 0;
        }
        //return Double.parseDouble(numFormat.format(fineAmount));
        return Double.parseDouble(numFormat.format(fineAmount));
    }

    public double getInstallAmount() {
        double remainingMonths = period - (dates*yMonths/yDates);
        if(remainingMonths>0){
            installAmount = balance / (remainingMonths*1.0);
        }else{
            installAmount = balance;
        }
        return Double.parseDouble(numFormat.format(installAmount));
    }

    public double getPayAmount() {
        payAmount = interestAmount + fineAmount + installAmount;
        return Double.parseDouble(numFormat.format(payAmount));
    }

    public void setInstallAmount(double installAmount) {
        this.installAmount = installAmount;
    }

    public int getTakenId() {
        return takenId;
    }

    public void setTakenId(int takenId) {
        this.takenId = takenId;
    }
  
}
