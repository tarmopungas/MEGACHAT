import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Lõim, mis loeb kasutaja sisendi ja edastab selle serverile.
 * Jookseb seni, kui kasutaja sisestab '/logout', et lahkuda.
 */

public class WriteThread extends Thread {
    private final Socket socket;
    private ChatClient client;
    Console console = System.console();
    private DataOutputStream doutput;
    private DataInputStream dinput;
    private Kasutaja kasutaja;
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
        console.writer().println("/create [name of chat room] – creates a new chat room");
    }

    /**
     * Kutsutakse välja /login käsu puhul, saadab serverisse login requesti
     * kui sisend sisaldab tühikutega eraldatud kasutajanime ja salasõna
     */
    private void login(String text) {
        if (text.split(" ").length == 2) {
            try {
                InputConstructor saadetav = new InputConstructor();
                // Kasutajanimi lisatakase
                saadetav.insertStr(text.split(" ")[1]);
                // Salasõnast võetakse räsi
                console.writer().println("Enter password: ");
                StringBuilder hashstr = createHash(String.valueOf(console.readPassword()));
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
                    int responseSize = dinput.readInt();
                    byte[] response = new byte[responseSize];
                    dinput.readNBytes(response, 0, responseSize);
                    kasutaja = (Kasutaja) ObjectConversion.convertFromBytes(response);
                    //authToken = ByteBuffer.wrap(response, 0, 4).getInt();
                    userName = kasutaja.getUserName();
                    loggedIn = true;
                    console.writer().println("Successfully logged in!");
                }
            } catch (IOException e) {
                throw new RuntimeException("Failed to send request");
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException("Encryptor function not found");
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Class not found");
            }
        } else {
            console.writer().println("Command was not understood, use syntax /login [username], username and password cannot include spaces");
        }
    }

    /**
     * Kutsutakse välja /register käsu puhul, saadab serverisse register requesti
     * kui sisend sisaldab tühikutega eraldatud kasutajanime ja salasõna
     */
    private void register(String text) {
        String[] tykid = text.split(" ");
        if (tykid.length == 2 && !tykid[1].contains("/") && !tykid[1].contains("\\")) {
            try {
                InputConstructor saadetav = new InputConstructor();
                // Kasutajanimi lisatakse
                saadetav.insertStr(text.split(" ")[1]);
                // Salasõnast võetakse räsi
                console.writer().println("Enter new password: ");
                StringBuilder hashstr = createHash(String.valueOf(console.readPassword()));
                console.writer().println("Confirm new password: ");
                StringBuilder hashstr2 = createHash(String.valueOf(console.readPassword()));
                if (!(hashstr.compareTo(hashstr2) == 0)) {
                    console.writer().println("Passwords didn't match. Try again please!");
                    return;
                }
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
            console.writer().println("Command was not understood, use syntax /register [username], username and password cannot include spaces or slashes");
        }
    }

    /**
     * Kutsutakse välja /changepw käsu puhul, saadab serverisse register requesti
     * kui sisend sisaldab tühikutega eraldatud vana salasõna ja uut salasõna
     */
    private void changePassword(String text) {
        if (text.split(" ").length == 1) {
            try {
                InputConstructor saadetav = new InputConstructor();
                // Vanast ja uuest salasõnast võetakse räsi
                console.writer().println("Enter old password: ");
                StringBuilder oldPassHash = createHash(String.valueOf(console.readPassword()));
                console.writer().println("Enter new password: ");
                StringBuilder newPassHash = createHash(String.valueOf(console.readPassword()));
                console.writer().println("Confirm new password: ");
                StringBuilder newPassHash2 = createHash(String.valueOf(console.readPassword()));
                if (!(newPassHash.compareTo(newPassHash2) == 0)) {
                    console.writer().println("New passwords didn't match. Try again please.");
                    return;
                }
                saadetav.insertStr(userName); // Kasutajanimi lisatakse
                saadetav.insertStr(oldPassHash.toString()); // Vana salasõna räsi lisatakse
                saadetav.insertStr(newPassHash.toString()); // Uue salasõna räsi lisatakse
                saadetav.insertInt(authToken); // authToken lisatakse
                byte[] request = saadetav.getOutput();
                // Reqtype
                doutput.writeInt(3);
                // Reqsize
                doutput.writeInt(request.length);
                // Request
                doutput.write(request, 0, request.length);
                int errCode = dinput.readInt();
                if (errCode != 0) {
                    console.writer().println("Wrong password. Please try again.");
                } else {
                    console.writer().println("Password change successful!");
                }

            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException("Encryptor function not found");
            } catch (IOException e) {
                throw new RuntimeException("Failed to send request");
            }
        } else {
            console.writer().println("Command was not understood, use syntax /changepw, password cannot include spaces.");
        }
    }

    /**
     * Kutsutakse välja /create käsu puhul, saadab serverisse requesti luua uus vestlusruum
     */
    private void createChat(String text){
        String[] tykid = text.split(" ");
        if (tykid.length == 2 && !tykid[1].contains("/") && !tykid[1].contains("\\")) {
            try {
                InputConstructor saadetav = new InputConstructor();
                // Nimi lisatakse
                saadetav.insertStr(text.split(" ")[1]);
                byte[] request = saadetav.getOutput();
                // Reqtype
                doutput.writeInt(4);
                // Reqsize
                doutput.writeInt(request.length);
                // Request
                doutput.write(request, 0, request.length);
                int errCode = dinput.readInt();
                if (errCode != 0) {
                    if (errCode == 2) {
                        console.writer().println("This chat room name is taken. Please try again.");
                    } else if (errCode == 3) {
                        // server arvab et sa ei ole sisse logitud, seda ei tohiks juhtuda kui klient nõuab enne selle käsu käivitamist sisselogimist
                        console.writer().println("Server failure (error 3)");
                    } else {
                        console.writer().println("Server failure");
                    }
                } else {
                    console.writer().println("Successfully created a chat room! Use /join [name] to enter");
                }
            } catch (IOException e) {
                throw new RuntimeException("Failed to send request");
            }
        } else {
            console.writer().println("Command was not understood, use syntax /create [name of chatroom], name cannot include spaces or slashes");
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
        console.writer().println("Hello, write /register [username] to register an account or /login [username] to log in");
        //console.writer().println("Hello, write /register [username] [password] to register an account or /login [username] [password] to log in");
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
                case ("/create"):
                    if (loggedIn) {
                        createChat(text);
                    } else {
                        console.writer().println("Log in to create a chatroom");
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