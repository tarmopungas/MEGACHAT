import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class CreateChatroomHandler {
    int errorCode;
    String userName;

    public CreateChatroomHandler() {
    }

    void handle(byte[] input) throws IOException {
        // SISEND Strings: chatroom name
        InputDeconstructor inputs = new InputDeconstructor(input, 0, 1);
        String name = inputs.getNthString(0);

        // Loob uue kausta "kasutajad", kui seda enne ei eksisteerinud
        Path path = Path.of(".", "vestlusruumid");
        Files.createDirectories(path);

        // Loob kasutajale temanimelise faili
        File vestlusFail = new File("vestlusruumid" + File.separator + name + ".txt");

        if (vestlusFail.exists()) {
            errorCode = 2;
        } else {
            Vestlusruum vestlusruum = new Vestlusruum(name, userName);

            // Kirjutab faili Vestlusruum isendi
            ObjectConversion.kirjutaVestlusruum(vestlusFail, vestlusruum);

            // Lisab kasutaja faili märkme, et ta asub selles vestlusruumis
            File kasutajaFail = new File("kasutajad" + File.separator + userName + ".txt");
            Kasutaja kasutaja = ObjectConversion.loeKasutaja(kasutajaFail);
            kasutaja.addChatroom(name);
            ObjectConversion.kirjutaKasutaja(kasutajaFail, kasutaja);

            errorCode = 0;
        }
    }
}
