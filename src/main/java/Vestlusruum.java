import org.apache.commons.collections4.queue.CircularFifoQueue;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Vestlusruum implements Serializable, Subject {
    private String name;
    private String owner; // Omaniku ID
    private CircularFifoQueue<Message> messages;
    private ArrayList<Kasutaja> aktiivsedKasutajad;
    private Set<String> members = new HashSet<String>();


    public Vestlusruum(String name, String firstMember) {
        this.name = name;
        this.owner = firstMember;
        this.messages = new CircularFifoQueue<>(20);
        this.members.add(firstMember);
    }

    public String getName() {
        return name;
    }

    public CircularFifoQueue<Message> getMessages() {
        return messages;
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

    @Override
    public void attach(Kasutaja user) {
        aktiivsedKasutajad.add(user);
    }

    @Override
    public void detach(Kasutaja o) {
        aktiivsedKasutajad.remove(o);
    }

    @Override
    public void notify(Kasutaja o) {
        for (int i = 0; i < aktiivsedKasutajad.size(); i++) {
            aktiivsedKasutajad.get(i).update(this);
        }
    }
}