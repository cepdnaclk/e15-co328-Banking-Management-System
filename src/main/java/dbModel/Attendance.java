
package dbModel;

public class Attendance {
    private int attId;
    private int memId;
    private String meetDate;
    private boolean status;

    public Attendance() {
    }

    public Attendance(int attId, int memId, String meetDate, boolean status) {
        this.attId = attId;
        this.memId = memId;
        this.meetDate = meetDate;
        this.status = status;
    }

    public int getAttId() {
        return attId;
    }

    public void setAttId(int attId) {
        this.attId = attId;
    }

    public int getMemId() {
        return memId;
    }

    public void setMemId(int memId) {
        this.memId = memId;
    }

    public String getMeetDate() {
        return meetDate;
    }

    public void setMeetDate(String meetDate) {
        this.meetDate = meetDate;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
    
}
