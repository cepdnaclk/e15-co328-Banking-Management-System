
package dbSubModel;

import java.text.DecimalFormat;

public class MemberAccount {
    private int memId;
    private String name;
    private String address;
    private String dob;
    private String nic;
    private String contact;
    private boolean gender;
    private boolean isMember;
    private boolean isActive;
    private boolean isApplicant;
    private int parentId;
    private int accId;
    private double norBalance;
    private double comBalance;

    private static DecimalFormat numFormat = new DecimalFormat("0.00"); 
    
    public MemberAccount() {
    }

    public MemberAccount(int memId, String name, String address, String dob, String nic, String contact, boolean gender, boolean isMember, boolean isActive, boolean isApplicant, int parentId, int accId, double norBalance, double comBalance) {
        this.memId = memId;
        this.name = name;
        this.address = address;
        this.dob = dob;
        this.nic = nic;
        this.contact = contact;
        this.gender = gender;
        this.isMember = isMember;
        this.isActive = isActive;
        this.isApplicant = isApplicant;
        this.parentId = parentId;
        this.accId = accId;
        this.norBalance = norBalance;
        this.comBalance = comBalance;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public boolean isIsMember() {
        return isMember;
    }

    public void setIsMember(boolean isMember) {
        this.isMember = isMember;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public boolean isIsApplicant() {
        return isApplicant;
    }

    public void setIsApplicant(boolean isApplicant) {
        this.isApplicant = isApplicant;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getAccId() {
        return accId;
    }

    public void setAccId(int accId) {
        this.accId = accId;
    }

    public double getNorBalance() {
        return Double.parseDouble(numFormat.format(norBalance));
    }

    public void setNorBalance(double norBalance) {
        this.norBalance = norBalance;
    }

    public double getComBalance() {
        return Double.parseDouble(numFormat.format(comBalance));
    }

    public void setComBalance(double comBalance) {
        this.comBalance = comBalance;
    }
    
    
}
