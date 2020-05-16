import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable { // 
  private String sonum;
  private static Date time;

  public Message(){
    
  }

  public Message(String sonum) {
    this.sonum = sonum;
    time = new Date();
  }
  public String getSonum(){
    return sonum;
  }
}
