import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {

    private static void checkArguments(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: port");
            System.exit(1);
        }
    }


    public static void main(String[] args) {

        checkArguments(args);

        try (Socket clientSocket = new ServerSocket(Integer.parseInt(args[0])).accept()){
            new RequestHandler(clientSocket).run();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
