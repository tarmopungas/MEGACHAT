import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

public class ListroomsHandler {
    String userName;
    byte[] output;
    int errorCode;

    public ListroomsHandler() {
    }

    void handle(byte[] input) throws IOException {
        File kasutajaFail = new File("kasutajad" + File.separator + userName + ".txt");
        Kasutaja kasutaja = ObjectConversion.loeKasutaja(kasutajaFail);
        Set<String> rooms = kasutaja.getChatrooms();
        InputConstructor inputConstructor = new InputConstructor();
        inputConstructor.insertInt(rooms.size());
        for (String room : rooms){
            inputConstructor.insertStr(room);
        }
        output = inputConstructor.getOutput();
        errorCode = 0;
    }
}
