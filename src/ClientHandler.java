import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Set;

public class ClientHandler implements Runnable {

    private Socket client;
    private BufferedReader in;
    private PrintWriter out;
    private ArrayList<ClientHandler> clients;
    private String login;
    private ArrayList<String> logins;

    public ClientHandler(Socket clientSocket, ArrayList<ClientHandler> clients, ArrayList<String> logins) throws IOException {
        this.client = clientSocket;
        this.clients = clients;
        this.logins = logins;
        in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        out = new PrintWriter(client.getOutputStream(), true);
    }

    @Override
    public void run() {


        out.println("S:CONNECT");

        try {

            while (true) {
                out.println("S:ENTER YOUR LOGIN: ");
                login = in.readLine();

                if (!logins.contains(login) && !login.isEmpty()) {
                    logins.add(login);
                    serverMessageToAll(login + " logged in");
                    break;
                } else {
                    serverMessage("Login already taken");
                }

            }

            int i = 0;

            while (logins.size() < 5) {

                if (i % 41 == 0) {
                    serverMessage("Waiting for players...");
                }
                Thread.sleep(400);
                i++;
            }

            serverMessage("START");

            while (true) {
                String request = in.readLine();
                if (request.contains("name")) {
                    out.println(Server.getRandomName());
                } else if (request.startsWith("say")) {
                    int fristSpace = request.indexOf(" ");
                    if (fristSpace != -1) {
                        outToAll(request.substring(fristSpace + 1));
                    }
                } else if (request.startsWith("CHOOSE")) {
                    int fristSpace = request.indexOf(" ");
                    if (fristSpace != -1) {

                    }
                } else {
                    out.println("ERROR");
                }
            }


        } catch (IOException e) {
            System.err.println("IO exepction in client handler");
            System.err.println(e.getStackTrace());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            out.close();
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void outToAll(String msg) {
        for (ClientHandler aClient : clients) {
            aClient.out.println(login + ": " + msg);
        }
    }

    private void serverMessageToAll(String msg) {
        for (ClientHandler aClient : clients) {
            aClient.out.println("S:" + msg);
        }
    }

    private void serverMessage(String msg) {
        for (ClientHandler aClient : clients) {
            if(aClient.login.equals(login)){
                aClient.out.println(msg);
            }
        }

    }
}