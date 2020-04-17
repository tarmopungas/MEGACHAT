import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PasswordChangeHandler {
    /**
     * Handler kontrollib, kas vana salasõna klapib sisestatuga. Kui ei klapi, saadab error code'i. Kui klapib, asendab vana salasõna uuega.
     * */

    int errorCode;

    public PasswordChangeHandler() {
    }

    byte[] handle(byte[] input) throws IOException {
        // SISEND Int: authToken. Strings: userName, oldPassHash, newPassHash
        InputDeconstructor inputs = new InputDeconstructor(input, 1, 2);
        int authToken = inputs.getNthInt(0); // TODO: Kontrollida, et authToken klapib

        // Loeb sisse sisestatud passi hashi ja kontrollib, et see klapib
        String oldPassHash = inputs.getNthString(1);
        String pass = Arrays.toString(Files.readAllBytes(Paths.get("kasutajad", inputs.getNthString(0))));

        // Kui salasõnad klapivad, kirjutab uue salasõna faili
        if (oldPassHash.equals(pass)) {
            Files.write(Paths.get("kasutajad", inputs.getNthString(0)), new ArrayList<>(List.of(inputs.getNthString(2))));
            errorCode = 0;
        } else {
            errorCode = 2;
        }

        return null;
    }
}