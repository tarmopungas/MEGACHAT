import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Lõim, mis loeb kasutaja sisendi ja edastab selle serverile.
 * Jookseb seni, kui kasutaja sisestab '/logout', et lahkuda.
 */

// TODO: Tekitada (int) muutujad requesttype'idele, et kood oleks kergemini loetav (int requestTypeChangePassword = 3 vms)

public class WriteThread extends Thread {
    private final Socket socket;
    private ChatClient client;
    Console console = System.console();
    private DataOutputStream doutput;
    private DataInputStream dinput;
    String userName = "not logged in";
    boolean loggedIn = false;
    // Authtoken saadetakse iga requestiga kaasa et tõestada, et see on volitatud kasutaja
    int authToken;


    public WriteThread(Socket socket, ChatClient client) {
        this.socket = socket;
        this.client = client;

        try {
            OutputStream output = socket.getOutputStream();
            doutput = new DataOutputStream(output);
            InputStream input = socket.getInputStream();
            dinput = new DataInputStream(input);
        } catch (IOException ex) {
            console.writer().println("Error getting output stream: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Kutsutakse välja /help käsu puhul, prindib välja kõik käsud ja nende kirjeldused
     */
    private void help() {
        console.writer().println("/help - prints this menu");
        console.writer().println("/logout - logs out and closes the program");
        console.writer().println("/changepw [old password] [new password] – changes your password");
    }

    /**
     * Kutsutakse välja /login käsu puhul, saadab serverisse login requesti
     * kui sisend sisaldab tühikutega eraldatud kasutajanime ja salasõna
     */
    private void login(String text) {
        if (text.split(" ").length == 3) {
            try {
                InputConstructor saadetav = new InputConstructor();
                // Kasutajanimi lisatakase
                saadetav.insertStr(text.split(" ")[1]);
                // Salasõnast võetakse räsi
                StringBuilder hashstr = createHash(text.split(" ")[2]);
                // Salasõna räsi lisatakse
                saadetav.insertStr(hashstr.toString());
                byte[] request = saadetav.getOutput();
                // Reqtype
                doutput.writeInt(2);
                // Reqsize
                doutput.writeInt(request.length);
                // Request
                doutput.write(request, 0, request.length);
                int errCode = dinput.readInt();
                if (errCode != 0) {
                    if (errCode == 2) {
                        console.writer().println("A user with this name does not exist. Please try again.");
                    } else if (errCode == 1) {
                        console.writer().println("Wrong password. Please try again.");
                    } else {
                        console.writer().println("Server failed to login. Please try again.");
                    }
                } else {
//                    int responseSize = dinput.readInt();
//                    byte[] response = new byte[responseSize];
//                    dinput.readNBytes(response, 0, responseSize);
//                    authToken = ByteBuffer.wrap(response, 0, 4).getInt();
                    userName = text.split(" ")[1];
                    loggedIn = true;
                    console.writer().println("Successfully logged in!");
                }
            } catch (IOException e) {
                throw new RuntimeException("Failed to send request");
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException("Encryptor function not found");
            }
        } else {
            console.writer().println("Command was not understood, use syntax /login [username] [password], username and password cannot include spaces");
        }
    }

    /**
     * Kutsutakse välja /register käsu puhul, saadab serverisse register requesti
     * kui sisend sisaldab tühikutega eraldatud kasutajanime ja salasõna
     */
    private void register(String text) {
        // TODO: Teha salasõna sisestamine peidetuks: st, et salasõna sisse trükkides ei kuvata seda ekraanil
        if (text.split(" ").length == 3) {
            try {
                InputConstructor saadetav = new InputConstructor();
                // Kasutajanimi lisatakse
                saadetav.insertStr(text.split(" ")[1]);
                // Salasõnast võetakse räsi
                StringBuilder hashstr = createHash(text.split(" ")[2]);
                // Salasõna räsi lisatakse
                saadetav.insertStr(hashstr.toString());
                byte[] request = saadetav.getOutput();
                // Reqtype
                doutput.writeInt(1);
                // Reqsize
                doutput.writeInt(request.length);
                // Request
                doutput.write(request, 0, request.length);
                int errCode = dinput.readInt();
                if (errCode != 0) {
                    if (errCode == 2) {
                        console.writer().println("This username is taken. Please try again.");
                    } else {
                        console.writer().println("Server failed to register");
                    }
                } else {
                    console.writer().println("Successfully created account! Use /login to login");
                }
            } catch (IOException e) {
                throw new RuntimeException("Failed to send request");
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException("Encryptor function not found");
            }
        } else {
            console.writer().println("Command was not understood, use syntax /register [username] [password], username and password cannot include spaces");
        }
    }

    /**
     * Kutsutakse välja /changepw käsu puhul, saadab serverisse register requesti
     * kui sisend sisaldab tühikutega eraldatud vana salasõna ja uut salasõna
     */
    private void changePassword(String text) {
        // TODO: Teha salasõnade sisestamine peidetuks: st, et salasõna sisse trükkides ei kuvata seda ekraanil
        if (text.split(" ").length == 3) {
            try {
                InputConstructor saadetav = new InputConstructor();
                // Hash old and new password
                StringBuilder oldPassHash = createHash(text.split(" ")[1]);
                StringBuilder newPassHash = createHash(text.split(" ")[2]);
                saadetav.insertStr(userName);
                saadetav.insertStr(oldPassHash.toString());
                saadetav.insertStr(newPassHash.toString());
                saadetav.insertInt(authToken);
                byte[] request = saadetav.getOutput();
                // Reqtype
                doutput.writeInt(3);
                // Reqsize
                doutput.writeInt(request.length);
                // Request
                doutput.write(request, 0, request.length);
                int errCode = dinput.readInt();
                if (errCode != 0) {
                    // TODO: anda täpsemalt kasutajale teada, mis valesti läks
                    console.writer().println("Failed to change password (probably entered wrong password)");
                } else {
                    dinput.readAllBytes();
                    console.writer().println("Password change successful!");
                }

            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException("Encryptor function not found");
            } catch (IOException e) {
                throw new RuntimeException("Failed to send request");
            }
        } else {
            console.writer().println("Command was not understood, use syntax /changepw [old password] [new password], password cannot include spaces.");
        }


    }

    private StringBuilder createHash(String password) throws NoSuchAlgorithmException {
        MessageDigest encryptor = MessageDigest.getInstance("SHA-256");
        byte[] hash = encryptor.digest(password.getBytes());
        StringBuilder hashstr = new StringBuilder();
        for (int i = 0; i < hash.length; i += 4) {
            String sym = Integer.toHexString(ByteBuffer.wrap(hash, i, 4).getInt());
            if (sym.length() == 1) hashstr.append("0");
            hashstr.append(sym);
        }
        return hashstr;
    }

    public void run() {
        console.writer().println("Hello, write /register [username] [password] to register an account or /login [username] [password] to log in");
        String text;

        do {
            text = console.readLine("[" + userName + "]: ");

            switch (text.split(" ")[0]) {
                case ("/help"):
                    help();
                    break;
                case ("/login"):
                    if (loggedIn) {
                        console.writer().println("Already logged in");
                    } else {
                        login(text);
                    }
                    break;
                case ("/register"):
                    if (loggedIn) {
                        console.writer().println("Log out to register a new account");
                    } else {
                        register(text);
                    }
                    break;
                case ("/changepw"):
                    if (loggedIn) {
                        changePassword(text);
                    } else {
                        console.writer().println("Log in to change your password");
                    }
                    break;
                case ("/logout"):
                    if (loggedIn) {
                        console.writer().println("You have successfully logged out. See you next time!");
                    } else {
                        console.writer().println("Bye-bye!");
                    }
                    break;
                default:
                    console.writer().println("The command was not understood, write /help to learn more");
            }
        } while (!text.equals("/logout"));

        try {
            socket.close();
        } catch (IOException ex) {

            console.writer().println("Error writing to server: " + ex.getMessage());
        }
    }
}