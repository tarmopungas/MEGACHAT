import java.io.Serializable;
import java.util.List;

public class Kasutaja implements Serializable {
    private String userName;
    private String password;
    private int authToken;
    private List<String> incomingFriendRequests;
    private List<String> friends;

    public Kasutaja(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public List<String> getFriends() {
        return friends;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
