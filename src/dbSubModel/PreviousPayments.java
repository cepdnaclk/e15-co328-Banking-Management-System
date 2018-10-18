/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbSubModel;

/**
 *
 * @author RISITH-PC
 */
public class PreviousPayments {
    private String payDate;
    private String payTime;
    private String loanName;
    private int numOfLoans;
    private double fine;
    private double interest;
    private double installAmount;
    private double paymentAmount;

    public PreviousPayments() {
    }

    public PreviousPayments(String payDate, String payTime, String loanName, int numOfLoans, double fine, double interest, double installAmount, double paymentAmount) {
        this.payDate = payDate;
        this.payTime = payTime;
        this.loanName = loanName;
        this.numOfLoans = numOfLoans;
        this.fine = fine;
        this.interest = interest;
        this.installAmount = installAmount;
        this.paymentAmount = paymentAmount;
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

    public String getLoanName() {
        return loanName;
    }

    public void setLoanName(String loanName) {
        this.loanName = loanName;
    }

    public int getNumOfLoans() {
        return numOfLoans;
    }

    public void setNumOfLoans(int numOfLoans) {
        this.numOfLoans = numOfLoans;
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

    public double getInstallAmount() {
        return installAmount;
    }

    public void setInstallAmount(double installAmount) {
        this.installAmount = installAmount;
    }

    public double getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }
    
    
}
