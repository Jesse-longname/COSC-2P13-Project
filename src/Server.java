import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(12000);
            System.out.println("Opened Server.");
            Socket clientSocket = serverSocket.accept();
            System.out.println("Got client");
            PrintStream out = new PrintStream(clientSocket.getOutputStream(), true);
            Scanner in = new Scanner(clientSocket.getInputStream());
            System.out.println("Created stuff");
            out.println("How many terms do you want to calculate? (e.g. 100000)");
            System.out.println("Wrote to client");
            long terms = Long.parseLong(in.nextLine());
            out.println("How many threads to you want to use? (e.g. 4)");
            int threads = Integer.parseInt(in.nextLine());
            out.println("Which method do you want to use to avoid deadlocks? (1 or 2)");
            int method = Integer.parseInt(in.nextLine());
            Main.createFile("E:\\JesseDev\\school\\COSC-2P13\\COSC-2P13-Project\\server-output.txt");
            Controller controller = new Controller(threads, terms, method, "E:\\JesseDev\\school\\COSC-2P13\\COSC-2P13-Project\\server-output.txt");
            out.println("Here is your value for pi: " + controller.doCalculation());
            BufferedReader reader = new BufferedReader(new FileReader("E:\\JesseDev\\school\\COSC-2P13\\COSC-2P13-Project\\server-output.txt"));
            String line = reader.readLine();
            while (line != null) {
                out.println(line);
            }
            out.println("finished");

        } catch (IOException e) {
            System.out.println("IO Exception in main");
            System.err.println(e);
        }

    }
}
