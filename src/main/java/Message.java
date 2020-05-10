import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;

public class Message implements Serializable { // 
  private HashMap<String, String> sõnum;
  private static final SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm:ss");

  public Message() {
    //this.sentAt = sdf.format(new Timestamp(System.currentTimeMillis()));
  }

  String content() {
    return ""; //this.sentAt + "| ";
  }
}
