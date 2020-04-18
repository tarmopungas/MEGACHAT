import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class PasswordField {
  protected static String getPassword(String prompt) {

    String password = "";
    ConsoleEraser consoleEraser = new ConsoleEraser();
    System.out.print(prompt);
    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    consoleEraser.setPriority(Thread.MAX_PRIORITY);
    consoleEraser.start();
    try {
      password = in.readLine();
    }
    catch (IOException e){
      System.out.println("Error trying to read your password!");
      System.exit(1);
    }

    consoleEraser.halt();
    System.out.print("\b");

    return password;
  }


  private static class ConsoleEraser extends Thread {
    private boolean running = true;

    public void run() {
      while (running) {
        System.out.print("\b ");
        try {
          sleep(1);
        }
        catch(InterruptedException e) {
          break;
        }
      }
    }
    public synchronized void halt() {
      running = false;
    }
  }
}
