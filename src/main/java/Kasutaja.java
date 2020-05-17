import java.io.Serializable;
import java.util.*;

public class Kasutaja implements Serializable {
    private String userName;
    private String password;
    private List<String> incomingFriendRequests;
    private List<String> friends;
    private Set<String> chatrooms = new HashSet<>();
    private Map<String,List<Message> > unreadMessages = new HashMap<>(); //key - vestlusruumi nimi

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

    public void addUnreadMessage(String name, Message message){
        if (unreadMessages.containsKey(name)){
            unreadMessages.get(name).add(message);
        } else {
            unreadMessages.put(name, new ArrayList<>());
            unreadMessages.get(name).add(message);
        }
    }
    public Map<String,List<Message> > getUnreadMessages(){return unreadMessages;}

    public Map<String,List<Message> > readNewMessages(){
        Map<String,List<Message> > out = new HashMap<String, List<Message>>(unreadMessages);
        unreadMessages = new HashMap<>();
        return out;
    }

    public void update(Vestlusruum vestlusruum) {
        vestlusruum.getMessages();
    }
}
