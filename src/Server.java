import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    private final int PORT;
    private final String LOG_PATH;


    /**
     * Creates a server to communicate with a client and run the Pi Calculations
     */
    public Server() {
        // Server setup
        Scanner setup_input = new Scanner(System.in);
        System.out.println("Server port: ");
        PORT = setup_input.nextInt();
        setup_input.nextLine();
        System.out.println("File path for logs: ");
        LOG_PATH = setup_input.nextLine();

        // Socket setup
        try {
            // Create the server socket
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server Start on port: " + PORT);

            // Get a client
            Socket clientSocket = serverSocket.accept();

            // Open communication channels
            PrintStream out = new PrintStream(clientSocket.getOutputStream(), true);
            Scanner in = new Scanner(clientSocket.getInputStream());

            // Get calculation inputs
            out.println("How many terms do you want to calculate? (e.g. 100000)");
            long terms = Long.parseLong(in.nextLine());
            out.println("How many threads to you want to use? (e.g. 4)");
            int threads = Integer.parseInt(in.nextLine());
            out.println("Which method do you want to use to avoid deadlocks? (1 or 2)");
            int method = Integer.parseInt(in.nextLine());

            // Start calculating
            BufferedWriter writer = Main.createFile(LOG_PATH);
            Controller controller = new Controller(threads, terms, method, LOG_PATH, writer);

            // Send result of calculation to client
            out.println("Here is your value for pi: " + controller.doCalculation());
            writer.close();

            // Start sending log file to client
            BufferedReader reader = new BufferedReader(new FileReader(LOG_PATH));
            String line = reader.readLine();
            while (line != null) {
                out.println(line);
                line = reader.readLine();
            }

            // Indicate that the log file is finished being passed to client
            out.println("finished");
            serverSocket.close();
            clientSocket.close();

        } catch (IOException e) {
            System.out.println("IO Exception in main");
            System.err.println(e);
        }
    }

    public static void main(String[] args) {
        new Server();
    }
}
