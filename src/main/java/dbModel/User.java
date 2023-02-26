package dbModel;

/**
 *
 * @author RISITH-PC
 */
public class User {
    private String userName;
    private String password;
    private int userType;
    private String type;
    
    public User() {
    }

    public User(String userName, String password, int userType) {
        this.userName = userName;
        this.password = password;
        this.userType = userType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getType() {
        switch(userType){
        case 0: type = "Admin"; break;
        case 1: type = "Clerk"; break;
        case 2: type = "Audit"; break;
    }
        return type;
    }
    
    
}
