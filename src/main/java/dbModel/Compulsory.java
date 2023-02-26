
package dbModel;

import java.text.DecimalFormat;

public class Compulsory {
    private int comId;
    private int accId;
    private String comDate;
    private String comTime;
    private int comType;
    private int numOfDates;
    private double amount;
    private double balance;
    private double interest;
    
    //--- additional attributes ---//
    private String type;

    private static DecimalFormat numFormat = new DecimalFormat("0.00"); 
    
    public Compulsory() {
    }

    public Compulsory(int comId, int accId, String comDate, String comTime, int comType, double amount, double balance, String type) {
        this.comId = comId;
        this.accId = accId;
        this.comDate = comDate;
        this.comTime = comTime;
        this.comType = comType;
        this.amount = amount;
        this.balance = balance;
        this.type = type;
    }

    public int getComId() {
        return comId;
    }

    public void setComId(int comId) {
        this.comId = comId;
    }

    public int getAccId() {
        return accId;
    }

    public void setAccId(int accId) {
        this.accId = accId;
    }

    public String getComDate() {
        return comDate;
    }

    public void setComDate(String comDate) {
        this.comDate = comDate;
    }

    public String getComTime() {
        return comTime;
    }

    public void setComTime(String comTime) {
        this.comTime = comTime;
    }

    public int getComType() {
        return comType;
    }

    public void setComType(int comType) {
        this.comType = comType;
    }

    public int getNumOfDates() {
        return numOfDates;
    }

    public void setNumOfDates(int numOfDates) {
        this.numOfDates = numOfDates;
    }
    
    public double getAmount() {
        return Double.parseDouble(numFormat.format(amount));
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
         switch(comType){
            case 0:
                type = "Deposit";
                break;
            case 1:
                type = "Interest";
                break;
        }
        return type;
    }     
}
