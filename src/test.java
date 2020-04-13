import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

class Stester {
    public static void main(String[] args){
        /*try(ServerSocket ss = new ServerSocket(1337)){
            while (true){
                try (Socket socket = ss.accept();
                     InputStream in = socket.getInputStream();
                     OutputStream out = socket.getOutputStream();
                     DataInputStream din = new DataInputStream(in);
                     DataOutputStream dout = new DataOutputStream(out)){

                    int reqType = din.readInt();
                    System.out.printf("Lugesin reqtypei %d\n", reqType);
                    int reqSize = din.readInt();
                    System.out.printf("Lugesin reqsizei %d\n", reqSize);
                    byte[] req = new byte[reqSize];
                    for (byte b : req){
                        System.out.print(b);
                        System.out.print(" ");
                    }
                    System.out.println();
                } catch (IOException e){
                    throw new RuntimeException ("err1");
                }
            }
        } catch (IOException e){
            throw new RuntimeException ("err2");
        }
*/
        String str = "xd xd";
        switch(str.split(" ")[0]){
            case "xd xd":
                System.out.println("1");
                break;
            case "xd":
                System.out.println("2");
                break;
            default:
                System.out.println("3");
        }
    }
}

