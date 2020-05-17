import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class AddHandler {
    int errorCode;

    public AddHandler() {
    }

    void handle(byte[] input) {
        InputDeconstructor inputDeconstructor = new InputDeconstructor(input,0,2);
        String room = inputDeconstructor.getNthString(0);
        String name = inputDeconstructor.getNthString(1);
        File kasutajaFail = new File("kasutajad" + File.separator + name + ".txt");
        File vestlusFail = new File("vestlusruumid"+File.separator + room + ".txt");
        if (!kasutajaFail.exists()) {
            errorCode = 2;
        } else if (!vestlusFail.exists()) {
            errorCode = 4;
        } else {
            Kasutaja kasutaja = ObjectConversion.loeKasutaja(kasutajaFail);
            kasutaja.addChatroom(room);
            ObjectConversion.kirjutaKasutaja(kasutajaFail, kasutaja);

            Vestlusruum vestlusruum = ObjectConversion.loeVestlusruum(vestlusFail);
            vestlusruum.addMember(name);
            ObjectConversion.kirjutaVestlusruum(vestlusFail, vestlusruum);
            errorCode = 0;
        }
    }
}
