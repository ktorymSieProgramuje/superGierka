import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Server {
    private  static String[] names = {"Wily","Kurwa","Jebane","Gowno"};
    private  static String[] adjs = {"Jebana","Chuj","Japierdole","Dramat"};
    private static final int PORT = 9090;
    private static ArrayList<String> logins = new ArrayList<>();

    private static ArrayList<ClientHandler> clients = new ArrayList<>();
    private static ExecutorService pool = Executors.newFixedThreadPool(5);

    public static void main(String[] args) throws IOException {
        ServerSocket listener = new ServerSocket(PORT);

        while(true) {
            System.out.println("S: Waiting for client connection...");
            Socket client = listener.accept();
            System.out.println("S:Client Connected");
            ClientHandler clientThread = new ClientHandler(client, clients, logins);
            clients.add(clientThread);


            pool.execute(clientThread);
        }
    }
    public static String getRandomName(){
        String name = names[(int)(Math.random()*names.length)];
        String adj = adjs[(int)(Math.random()*adjs.length)];
        return name + " " + adj;
    }
}
