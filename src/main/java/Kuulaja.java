import java.io.*;
import java.net.CacheRequest;
import java.net.ServerSocket;
import java.net.Socket;

public class Kuulaja implements Runnable {
    ServerSocket ss;
    int id;

    public Kuulaja(ServerSocket ss, int id) {
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
                 DataOutputStream dout = new DataOutputStream(out)) {
                // ootab uusi requeste
                String userName = null;
                while (true) {
                    int requestType = din.readInt();
                    if (requestType == -1) {
                        break;
                    }
                    int requestSize = din.readInt();
                    byte[] request = new byte[requestSize];
                    din.readNBytes(request, 0, requestSize);
                    int errorCode = 0;
                    byte[] output = null;
                    switch (requestType) {
                        // siin kutsutakse handlereid välja caseidega
                        case 1:
                            RegisterHandler registerHandler = new RegisterHandler();
                            registerHandler.handle(request);
                            errorCode = registerHandler.errorCode;
                            break;
                        case 2:
                            LoginHandler loginHandler = new LoginHandler();
                            output = loginHandler.handle(request);
                            errorCode = loginHandler.errorCode;
                            if (errorCode == 0){
                                userName = loginHandler.userName;
                            }
                            break;
                        case 3:
                            PasswordChangeHandler passwordChangeHandler = new PasswordChangeHandler();
                            passwordChangeHandler.handle(request);
                            errorCode = passwordChangeHandler.errorCode;
                            break;
                        case 4:
                            CreateChatroomHandler createChatroomHandler = new CreateChatroomHandler();
                            if (userName == null){
                                errorCode = 3;
                            } else {
                                createChatroomHandler.userName = userName;
                                createChatroomHandler.handle(request);
                                errorCode = createChatroomHandler.errorCode;
                            }
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
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
