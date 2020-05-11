import javax.naming.ldap.StartTlsRequest;
import javax.net.ssl.*;
import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.cert.X509Certificate;
import java.util.Scanner;

// TODO: Better exception handling

public class ChatClient {
    private final String hostname;
    private final int port;
    private String userName;

    public ChatClient(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    public void execute() {
        try {
            // https://stackoverflow.com/questions/2893819/accept-servers-self-signed-ssl-certificate-in-java-client
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[0];
                        }
                        public void checkClientTrusted(
                                java.security.cert.X509Certificate[] certs, String authType) { }
                        public void checkServerTrusted(
                                java.security.cert.X509Certificate[] certs, String authType) { }
                    }
            };

            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            SSLSocketFactory factory = sc.getSocketFactory();
            SSLSocket socket = (SSLSocket) factory.createSocket(hostname, port);

            socket.addHandshakeCompletedListener(event -> {
                try {
                    // Kui me ei tea, milline peab olema serveri sertifikaat, usume serverit
                    // Kui me juba teame, see peab olema sama
                    String newCertificate = event.getPeerCertificates()[0].toString();

                    // Loob uue kausta "sertifikaadid", kui seda enne ei eksisteerinud
                    Path path = Path.of(".", "sertifikaadid");
                    Files.createDirectories(path);

                    File f = new File("sertifikaadid" + File.separator + hostname + ".txt");
                    if (!f.isFile()) {
                        FileWriter fw = new FileWriter(f);
                        fw.write(newCertificate);
                        fw.close();
                    } else {
                        Scanner s = new Scanner(new FileInputStream(f)).useDelimiter("\\A");
                        String oldCertificate = s.hasNext() ? s.next() : "";
                        if (!newCertificate.equals(oldCertificate)) {
                            throw new Exception();
                        }
                    }

                    System.out.println("Connected to the chat server");
                    //new ReadThread(socket, this).start();
                    WriteThread t = new WriteThread(socket, this);
                    t.start();
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("TLS viga! Võib-olla keegi üritab teostada MITM rünne.");
                    System.out.println("Kui te teate, et serveri omanik vahetas sertifikaati, kustutage faili");
                    System.out.println("sertifikaadid" + File.separator + hostname + ".txt");
                    try {
                        socket.close();
                    } catch (IOException ignored) { }
                }
            });
            socket.startHandshake();

        } catch (UnknownHostException ex) {
            System.out.println("Server not found: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("I/O Error: " + ex.getMessage());
        } catch (Exception ignored) { }
    }

    String getUserName() {
        return this.userName;
    }


    public static void main(String[] args) {
        if (args.length < 2) return;

        String hostname = args[0];
        int port = Integer.parseInt(args[1]);

        ChatClient client = new ChatClient(hostname, port);
        client.execute();
    }
}