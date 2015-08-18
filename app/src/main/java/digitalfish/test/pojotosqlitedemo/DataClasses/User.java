package digitalfish.test.pojotosqlitedemo.DataClasses;

/**
 * Created by Piotr Malak on 2015-06-15.
 */
public class User {
    String UserName;
    String Login;
    String Password;

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String pUserName) {
        UserName = pUserName;
    }

    public String getLogin() {
        return Login;
    }

    public void setLogin(String pLogin) {
        Login = pLogin;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String pPassword) {
        Password = pPassword;
    }
}
