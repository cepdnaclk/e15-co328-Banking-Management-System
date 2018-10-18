
package dbModel;

import java.text.DecimalFormat;

public class Normal {
    private int norId;
    private int accId;
    private String norDate;
    private String norTime;
    private int norType;
    private int numOfDates;
    private double amount;
    private double balance;
    private double interest;
    
    //--- additional attributes ---//
    private String type;
    private String deposit;
    private String withDraw;
    
    
    private static DecimalFormat numFormat = new DecimalFormat("0.00"); 
    
    public Normal() {
    }

    public Normal(int norId, int accId, String norDate, String norTime, int norType, int numOfDates, double amount, double balance, double interest, String type, String deposit, String withDraw) {
        this.norId = norId;
        this.accId = accId;
        this.norDate = norDate;
        this.norTime = norTime;
        this.norType = norType;
        this.numOfDates = numOfDates;
        this.amount = amount;
        this.balance = balance;
        this.interest = interest;
        this.type = type;
        this.deposit = deposit;
        this.withDraw = withDraw;
    }

  
   
    public int getNorId() {
        return norId;
    }

    public void setNorId(int norId) {
        this.norId = norId;
    }

    public int getAccId() {
        return accId;
    }

    public void setAccId(int accId) {
        this.accId = accId;
    }

    public String getNorDate() {
        return norDate;
    }

    public void setNorDate(String norDate) {
        this.norDate = norDate;
    }

    public String getNorTime() {
        return norTime;
    }

    public void setNorTime(String norTime) {
        this.norTime = norTime;
    }

    public int getNorType() {
        return norType;
    }

    public void setNorType(int norType) {
        this.norType = norType;
    }

    public double getAmount() {
        return amount;
    }

     public int getNumOfDates() {
        return numOfDates;
    }

    public void setNumOfDates(int numOfDates) {
        this.numOfDates = numOfDates;
    }
    
    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getBalance() {
        return Double.parseDouble(numFormat.format(balance));
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    
    public double getInterest() {
        return interest;
    }

    public void setInterest(double interest) {
        this.interest = interest;
    }
    
    //---- additional attributes getters ----//
    public String getType() {
        switch(norType){
            case 0:
                type = "Deposit";
                break;
            case 1:
                type = "Interest";
                break;
            case 2:
                type = "WithDraw";
                break;
        }
        return type;
    }

    public String getDeposit() {
        if(amount>=0){
            deposit = numFormat.format(amount);
        }else{
            deposit = "****";
        }
        return deposit;
    }

    public String getWithDraw() {
        if(amount>=0){
            withDraw = "****";
        }else{
            withDraw = numFormat.format(-amount);
        }
        return withDraw;
    }

}
