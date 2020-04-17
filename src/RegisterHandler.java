import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class RegisterHandler {
    int errorCode;

    public RegisterHandler() {
    }

    byte[] handle(byte[] input) throws IOException {
        // SISEND Strings: userName, passwordHash
        InputDeconstructor inputs = new InputDeconstructor(input, 0, 2);
        String userName = inputs.getNthString(0);

        // Loob uue kausta "kasutajad", kui seda enne ei eksisteerinud
        String pathOfDirectory = "." + File.separator + "kasutajad";
        Files.createDirectories(Paths.get(pathOfDirectory));

        File kasutajaFail = new File("kasutajad" + File.separator + userName);

        if (kasutajaFail.exists()) {
            errorCode = 2;
        } else {
            String pass = inputs.getNthString(1);
            Files.write(Paths.get("kasutajad", userName), new ArrayList<>(List.of(pass)));
            errorCode = 0;
        }
        return null;
    }
}
