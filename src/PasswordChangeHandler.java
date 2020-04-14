/**
 * This is just a template for the password change handler (based on RegisterHandler)
 * Handler kontrollib, kas vana salasõna klapib sisestatuga. Kui ei klapi, saadab error code'i. Kui klapib, asendab vana salasõna uuega.
 * Kuhu salasõna salvestatakse?
 * */

import java.io.IOException;

public class PasswordChangeHandler {
    Integer errorCode;

    public PasswordChangeHandler(Integer errorCode) {
        this.errorCode = errorCode;
    }

    byte[] handle(byte[] input) throws IOException {
        InputDeconstructor inputs = new InputDeconstructor(input, 0, 2);

        return null;
    }
}