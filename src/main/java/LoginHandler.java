import java.io.*;

public class LoginHandler {
    int errorCode;
    String userName;

    public LoginHandler() {
    }

    byte[] handle(byte[] input) throws IOException {
        // SISEND Strings: userName, passwordHash
        // TODO: implementeerida authToken
        InputDeconstructor inputs = new InputDeconstructor(input, 0, 2);
        userName = inputs.getNthString(0);

        File kasutajaFail = new File("." + File.separator + "kasutajad" + File.separator + userName + ".txt");

        if (!kasutajaFail.exists()) {
            errorCode = 2;
            return null;
        } else {
            String enteredPass = inputs.getNthString(1);

            Kasutaja kasutaja = ObjectConversion.loeKasutaja(kasutajaFail);

            String actualPass = kasutaja.getPassword();
            if (enteredPass.equals(actualPass)) {
                errorCode = 0;
                return ObjectConversion.convertToBytes(kasutaja);
            } else {
                errorCode = 1;
                return null;
            }
        }
    }
}