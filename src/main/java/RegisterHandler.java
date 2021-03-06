import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class RegisterHandler {
    int errorCode;

    public RegisterHandler() {
    }

    void handle(byte[] input) throws IOException {
        // SISEND Strings: userName, passwordHash
        InputDeconstructor inputs = new InputDeconstructor(input, 0, 2);
        String userName = inputs.getNthString(0);

        // Loob uue kausta "kasutajad", kui seda enne ei eksisteerinud
        Path path = Path.of(".", "kasutajad");
        Files.createDirectories(path);

        // Loob kasutajale temanimelise faili
        File kasutajaFail = new File("kasutajad" + File.separator + userName + ".txt");

        if (kasutajaFail.exists()) {
            errorCode = 2;
        } else {
            String enteredPass = inputs.getNthString(1);

            Kasutaja kasutaja = new Kasutaja(userName, enteredPass);

            // Kirjutab faili Kasutaja isendi
            ObjectConversion.kirjutaKasutaja(kasutajaFail, kasutaja);
            errorCode = 0;
        }
    }
}
