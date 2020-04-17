import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class LoginHandler {
    int errorCode;

    public LoginHandler() {
    }

    byte[] handle(byte[] input) throws IOException {
        // SISEND Strings: userName, passwordHash
        InputDeconstructor inputs = new InputDeconstructor(input, 0, 2);
        String username = inputs.getNthString(0);

        String pathOfDirectory = "." + File.separator + "kasutajad" + File.separator + username;
        File kasutajaFail = new File(pathOfDirectory);

        if (!kasutajaFail.exists()) {
            errorCode = 2;
        } else {
            String enteredPass = inputs.getNthString(1);
            String actualPass = Files.readString(Paths.get(pathOfDirectory)).strip();

            if (enteredPass.equals(actualPass)) {
                errorCode = 0;
            } else {
                errorCode = 1;
            }
        }
        return null;
    }
}