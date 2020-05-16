import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

public class MessageHandler {
    byte[] output;
    int errorCode;

    public MessageHandler() {
    }

    void handle(byte[] input) throws IOException {
        InputDeconstructor inputDeconstructor = new InputDeconstructor(input,0,2);
        String room = inputDeconstructor.getNthString(0);
        Message message = new Message(inputDeconstructor.getNthString(1));

        File vestlusFail = new File("vestlusruumid" + File.separator + room + ".txt");
        if (vestlusFail.exists()){
            Vestlusruum vestlusruum = ObjectConversion.loeVestlusruum(vestlusFail);
            vestlusruum.addMessage(message);
            //vestlusruum.notify();
            ObjectConversion.kirjutaVestlusruum(vestlusFail, vestlusruum);
        } else {
            errorCode = 4;
        }
        errorCode = 0;
    }
}
