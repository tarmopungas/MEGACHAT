import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class RegisterHandler {
    Integer errorCode;

    public RegisterHandler(Integer errorCode) {
        this.errorCode = errorCode;
    }

    byte[] handle(byte[] input) throws IOException {
        InputDeconstructor inputs = new InputDeconstructor(input, 0, 2);
        String username = inputs.getNthString(0);
        File kasutajaFail = new File("kasutajad/" + username);
        if (kasutajaFail.exists()) {
            errorCode = 2;
        } else {
            String pass = inputs.getNthString(1);
            Files.write(Paths.get("kasutajad", username), new ArrayList<>(List.of(pass)));
            errorCode = 0;
        }
        return null;
    }
}
