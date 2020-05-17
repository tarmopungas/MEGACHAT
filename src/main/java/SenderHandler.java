import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class SenderHandler {
    int errorCode;
    public String userName;
    byte[] output;

    public SenderHandler() {
    }

    void handle(byte[] input) {
        File kasutajaFail = new File("kasutajad" + File.separator + userName + ".txt");

        if (!kasutajaFail.exists()) {
            errorCode = 2;
        } else {
            Kasutaja kasutaja = ObjectConversion.loeKasutaja(kasutajaFail);
            Map<String, List<Message> > messages= kasutaja.readNewMessages();
            ObjectConversion.kirjutaKasutaja(kasutajaFail, kasutaja);
            InputConstructor inputConstructor = new InputConstructor();
            inputConstructor.insertInt(messages.size());
            int sumAll = 0;
            for (Map.Entry<String, List<Message> > pair : messages.entrySet()){
                sumAll += pair.getValue().size()*2+1;
            }
            inputConstructor.insertInt(sumAll);
            for (Map.Entry<String, List<Message> > pair : messages.entrySet()){
                inputConstructor.insertStr(pair.getKey());
                inputConstructor.insertInt(pair.getValue().size());
                for (Message message : pair.getValue()){
                    inputConstructor.insertStr(message.getSender());
                    inputConstructor.insertStr(message.getSonum());
                }
            }
            output = inputConstructor.getOutput();
            errorCode = 0;
        }
    }
}
