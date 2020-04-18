import java.io.File;
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
        // SISEND Int: authToken. Strings: userName, hash of enteredOldPass, hash of newPass
        InputDeconstructor inputs = new InputDeconstructor(input, 1, 3);
        // TODO: Implementeerida authToken
        int authToken = inputs.getNthInt(0);
        String userName = inputs.getNthString(0);

        // Loeb sisse sisestatud vana salasõna ja praegu kehtiva salasõna
        String enteredOldPass = inputs.getNthString(1);
        String pathOfDirectory = "." + File.separator + "kasutajad" + File.separator + userName;
        String actualOldPass = Files.readString(Paths.get(pathOfDirectory)).strip();

        // Kui salasõnad klapivad, kirjutab uue salasõna faili
        if (enteredOldPass.equals(actualOldPass)) {
            String enteredNewPass = inputs.getNthString(2);
            Files.writeString(Paths.get("kasutajad", userName), enteredNewPass);
            errorCode = 0;
        } else {
            errorCode = 2;
        }

        return null;
    }
}