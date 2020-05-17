import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable { // 
  private String sonum;
  private static Date time;
  private String sender;

  public Message(){

  }

  public Message(String sonum, String sender) {
    this.sonum = sonum;
    time = new Date();
    this.sender = sender;
  }
  public String getSonum(){
    return sonum;
  }

  public String getSender() {return sender; }
}
