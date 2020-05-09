import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Vestlusruum implements Serializable {
    private String name;
    private Set<String> members = new HashSet<String>();

    public Vestlusruum() {
    }

    public Vestlusruum(String name, String firstMember) {
        this.name = name;
        this.members.add(firstMember);
    }

    public String getName() {
        return name;
    }

    public Set<String> getMembers() {
        return members;
    }

    public void addMember(String userName) {
        this.members.add(userName);
    }

    public void removeMember(String userName) {
        this.members.remove(userName);
    }
}
