import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Kasutaja implements Serializable {
    private String userName;
    private String password;
    private List<String> incomingFriendRequests;
    private List<String> friends;
    private Set<String> chatrooms = new HashSet<>();

    public Kasutaja() {
    }

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

    public Set<String> getChatrooms() {
        return chatrooms;
    }

    public void addChatroom(String name){chatrooms.add(name);}

    public void setPassword(String password) {
        this.password = password;
    }

    public void update(Vestlusruum vestlusruum) {
        vestlusruum.getMessages();
    }
}
