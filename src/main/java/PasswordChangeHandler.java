import java.io.File;

public class PasswordChangeHandler {
    /**
     * Handler kontrollib, kas vana salasõna klapib sisestatuga. Kui ei klapi, saadab error code'i. Kui klapib, asendab vana salasõna uuega.
     * */

    int errorCode;

    public PasswordChangeHandler() {
    }

    void handle(byte[] input) {
        // SISEND Int: authToken. Strings: hash of enteredOldPass, hash of enteredNewPass
        InputDeconstructor inputs = new InputDeconstructor(input, 1, 3);
        // TODO: implementeerida authToken
        int authToken = inputs.getNthInt(0);
        String userName = inputs.getNthString(0);
        String enteredOldPass = inputs.getNthString(1);

        // Loeb sisse Kasutaja isendi
        File kasutajaFail = new File("." + File.separator + "kasutajad" + File.separator + userName + ".txt");
        Kasutaja kasutaja = ObjectConversion.loeKasutaja(kasutajaFail);
        String actualOldPass = kasutaja.getPassword();

        // Kui salasõnad klapivad, kirjutab uue salasõna faili
        if (enteredOldPass.equals(actualOldPass)) {
            String enteredNewPass = inputs.getNthString(2);
            kasutaja.setPassword(enteredNewPass);
            ObjectConversion.kirjutaKasutaja(kasutajaFail, kasutaja);
            errorCode = 0;
        } else {
            errorCode = 2;
        }
    }
}