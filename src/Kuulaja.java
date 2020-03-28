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
                    int requestType = din.read();
                    int requestSize = din.read();
                    byte[] request = new byte[requestSize];
                    din.read(request, 0, requestSize);
                    Integer errorCode = 0;
                    byte[] output = null;
                    switch (requestType) {
                        // siin kutsutakse handlereid välja caseidega
                        case 1:
                            RegisterHandler registerHandler = new RegisterHandler(errorCode);
                            output = registerHandler.handle(request);
                            break;
                        default:
                            System.out.println("Midagi on valesti, lugesin request typei: " + requestType);
                            // kliendile saadetakse "mittedefineeritud request type"
                            errorCode = 1;
                    }
                    dout.write(errorCode);
                    if (errorCode == 0 && output != null) {
                        dout.write(output.length);
                        dout.write(output);
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException("Pordilugemine ei tööta");
            }
        }
    }

}
