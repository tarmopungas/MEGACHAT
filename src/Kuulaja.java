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
        while (true) {
            try (Socket socket = ss.accept();
                 InputStream in = socket.getInputStream();
                 OutputStream out = socket.getOutputStream();
                 DataInputStream din = new DataInputStream(in);
                 DataOutputStream dout = new DataOutputStream(out)) {
                int requestType = din.read();
                switch (requestType) {
                    // siin kutsutakse handlereid välja caseidega
                    default:
                        System.out.println("Midagi on valesti, lugesin request typei: " + requestType);
                        // kliendile saadetakse "mittedefineeritud request type"
                        dout.write(1);
                }
            } catch (IOException e) {
                throw new RuntimeException("Pordilugemine ei tööta");
            }
        }
    }

}
