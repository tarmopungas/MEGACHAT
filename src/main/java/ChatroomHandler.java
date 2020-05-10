import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ChatroomHandler {
    int errorCode;
    String userName;
    Vestlusruum vestlusruum = null;

    public ChatroomHandler() {
    }

    void connect(byte[] input) throws IOException {
        InputDeconstructor inputs = new InputDeconstructor(input, 0, 1);
        String name = inputs.getNthString(0);

        Path path = Path.of(".", "vestlusruumid");
        Files.createDirectories(path);

        File vestlusFail = new File("vestlusruumid" + File.separator + name + ".txt");

        if (!vestlusFail.exists()) {
            errorCode = 2;
        } else {
            this.vestlusruum = ObjectConversion.loeVestlusruum(vestlusFail);
            System.out.println(vestlusruum);

            // Lisab kasutaja faili märkme, et ta asub selles vestlusruumis
            File kasutajaFail = new File("kasutajad" + File.separator + userName + ".txt");
            Kasutaja kasutaja = ObjectConversion.loeKasutaja(kasutajaFail);
            ObjectConversion.kirjutaKasutaja(kasutajaFail, kasutaja);
            kasutaja.addChatroom(name);

            this.vestlusruum.attach(kasutaja); // Kasutaja märgitakse aktiivseks kuulajaks

            errorCode = 0;
        }
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
