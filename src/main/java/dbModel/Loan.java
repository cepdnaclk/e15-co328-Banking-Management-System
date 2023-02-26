package dbModel;

public class Loan {
    private int loanId;
    private String loanName;
    private double rate;
    private Double maxAmount;
    private boolean forMember;
    private boolean method;
    private int rateType;
    private int fineType;
    private int guaranteeType;    
    private Integer minPeriod;
    private Integer maxPeriod;
    
    private String methodName;

    public Loan() {}

    public Loan(int loanId, String loanName, double rate, boolean forMember, boolean method, int rateType, int fineType, int guaranteeType, Double maxAmount, Integer minPeriod, Integer maxPeriod, String methodName) {
        this.loanId = loanId;
        this.loanName = loanName;
        this.rate = rate;
        this.forMember = forMember;
        this.method = method;
        this.rateType = rateType;
        this.fineType = fineType;
        this.guaranteeType = guaranteeType;
        this.maxAmount = maxAmount;
        this.minPeriod = minPeriod;
        this.maxPeriod = maxPeriod;
        this.methodName = methodName;
    }

    public int getLoanId() {
        return loanId;
    }

    public void setLoanId(int loanId) {
        this.loanId = loanId;
    }

    public String getLoanName() {
        return loanName;
    }

    public void setLoanName(String loanName) {
        this.loanName = loanName;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public boolean isForMember() {
        return forMember;
    }

    public void setForMember(boolean forMember) {
        this.forMember = forMember;
    }

    public boolean isMethod() {
        return method;
    }

    public void setMethod(boolean method) {
        this.method = method;
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

    public Double getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(Double maxAmount) {
        this.maxAmount = maxAmount;
    }

    public Integer getMinPeriod() {
        return minPeriod;
    }

    public void setMinPeriod(Integer minPeriod) {
        this.minPeriod = minPeriod;
    }

    public Integer getMaxPeriod() {
        return maxPeriod;
    }

    public void setMaxPeriod(Integer maxPeriod) {
        this.maxPeriod = maxPeriod;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }   

}
