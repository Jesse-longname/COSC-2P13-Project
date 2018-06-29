import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * Creates a client to communicate with our Server. Handles inptu from user and output from the Pi Calculation.
 */
public class Client {
    private final String IP;
    private final int PORT;
    private final String LOG_PATH;

    public Client() {
        // Get settings to connect to the server
        Scanner in = new Scanner(System.in);
        System.out.println("Enter in the Server IP: ");
        IP = in.nextLine();
        System.out.println("Enter in the Server Port: ");
        PORT = in.nextInt();
        in.nextLine();

        System.out.println("Enter path to put log file into.");
        LOG_PATH = in.nextLine();

        try {
            // Connect to the server
            Socket clientSocket = new Socket(IP, PORT);

            // Open communication channels with server
            PrintStream out = new PrintStream(clientSocket.getOutputStream(), true);
            Scanner server_in = new Scanner(clientSocket.getInputStream());

            // Respond to server queries until result is obtained
            while (true) {
                String line = server_in.nextLine();
                System.out.println(line); // Get from the server
                // Break out when we have gotten the result from the server
                if (line.contains("Here is your value for pi:")) {
                    break;
                }
                out.println(in.nextLine()); // Write to the server
            }

            // Start getting the log file from the server
            BufferedWriter  writer = Main.createFile(LOG_PATH);
            while (true) {
                String line = server_in.nextLine();
                if (line.equals("finished")) { // Finished getting log file
                    break;
                }
                Main.writeResult(writer, LOG_PATH, line);
            }
            // Close communications
            writer.close();
            clientSocket.close();
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public static void main(String[] args) {
        new Client();
    }
}
