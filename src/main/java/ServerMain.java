import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class ServerMain {
    public static void main(String[] args) throws Exception {
        int threadNum = 100;
        try (ServerSocket ss = new ServerSocket(1337)) {
            List<Thread> pool = new ArrayList<>();
            for (int i = 0; i < threadNum; i++) {
                pool.add(new Thread(new Kuulaja(ss, i)));
                pool.get(pool.size() - 1).start();
            }

            for (Thread thread : pool) {
                thread.join();
            }
        }
    }
}
