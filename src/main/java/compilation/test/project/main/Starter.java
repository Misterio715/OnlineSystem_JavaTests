package compilation.test.project.main;
import compilation.test.project.queue.Receive;
import compilation.test.project.queue.Send;

public class Starter {
    public static Receive listener = new Receive();
    public static Send sender = new Send();
    public static void main(String[] args) {
        try {
            sender.setConnection("localhost", "server_answers");
            listener.setConnection("localhost", "site_messages");
            listener.startReceive();
        }
        catch (Exception e) {
            System.out.println("Queue connection error");
        }
    }
}