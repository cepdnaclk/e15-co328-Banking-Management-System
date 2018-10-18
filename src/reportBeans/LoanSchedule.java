/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reportBeans;

/**
 *
 * @author RISITH-PC
 */
public class LoanSchedule {
    private String record;
    
    private int memId;
    private String name;
    private String takenDate;
    private int period;
    private double amount;
    private double balance;

    public LoanSchedule() {
    }

    public LoanSchedule(String record, int memId, String name, String takenDate, int period, double amount, double balance) {
        this.record = record;
        this.memId = memId;
        this.name = name;
        this.takenDate = takenDate;
        this.period = period;
        this.amount = amount;
        this.balance = balance;
    }

    public String getRecord() {
        return record;
    }

    public void setRecord(String record) {
        this.record = record;
    }

    public int getMemId() {
        return memId;
    }

    public void setMemId(int memId) {
        this.memId = memId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTakenDate() {
        return takenDate;
    }

    public void setTakenDate(String takenDate) {
        this.takenDate = takenDate;
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
