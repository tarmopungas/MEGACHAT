import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Kuulaja implements Runnable{
    ServerSocket ss;
    int id;

    public Kuulaja(ServerSocket ss, int id){
        this.ss = ss;
        this.id = id;
    }

    @Override
    public void run() {
        // ootab uusi kliente
        while (true) {
            try (Socket socket = ss.accept();
                 InputStream in = socket.getInputStream();
                 OutputStream out = socket.getOutputStream();
                 DataInputStream din = new DataInputStream(in);
                 DataOutputStream dout = new DataOutputStream(out)){
                // ootab uusi requeste
                while (true) {
                    int requestType = din.readInt();
                    if (requestType == -1){
                        break;
                    }
                    //System.out.printf("Lugesin req typei %d\n", requestType);
                    int requestSize = din.readInt();
                    //System.out.printf("Lugesin req sizei %d\n", requestSize);
                    byte[] request = new byte[requestSize];
                    din.readNBytes(request, 0, requestSize);
                    Integer errorCode = 0; // TODO: Miks lihtsalt int ei sobiks?
                    byte[] output = null;
                    switch (requestType) {
                        // siin kutsutakse handlereid välja caseidega
                        case 1:
                            // TODO: Miks handlerile peab errorCode'i kaasa andma?
                            RegisterHandler registerHandler = new RegisterHandler(errorCode);
                            output = registerHandler.handle(request);
                            break;
                        case 2:
                            // TODO: Luua LoginHandler objekt
                            break;
                        case 3:
                            PasswordChangeHandler passwordChangeHandler = new PasswordChangeHandler(errorCode);
                            output = passwordChangeHandler.handle(request);
                            break;

                        default:
                            System.out.println("Midagi on valesti, lugesin request typei: " + requestType);
                            // kliendile saadetakse "mittedefineeritud request type"
                            errorCode = 1;
                    }
                    dout.writeInt(errorCode);
                    if (errorCode == 0 && output != null) {
                        dout.writeInt(output.length);
                        dout.write(output);
                    } else {
                        dout.writeInt(0);
                    }
                }
            } catch (IOException e) {
                // Klient sulges ühenduse
            }
        }
    }

}
