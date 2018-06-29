import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public Client() {
        Scanner in = new Scanner(System.in);
        System.out.println("Enter in the Server IP: ");
        String ip = in.nextLine();
        System.out.println("Enter in the Server Port: ");
        int port = in.nextInt();
        in.nextLine();

        try {
            Socket clientSocket = new Socket(ip, port);
            PrintStream out = new PrintStream(clientSocket.getOutputStream(), true);
            Scanner server_in = new Scanner(clientSocket.getInputStream());
            System.out.println("Created things in client");
            while (true) {
                String line = server_in.nextLine();
                System.out.println(line); // Get from the server
                if (line.contains("Here is your value for pi:")) {
                    break;
                }
                out.println(in.nextLine()); // Write to the server
            }
            // Handle log file
//            System.out.println("Enter path to put log file into.");
//            String fileLoc = in.nextLine();
            String fileLoc = "E:\\JesseDev\\school\\COSC-2P13\\COSC-2P13-Project\\server-response.txt";
            Main.createFile(fileLoc);
            while (true) {
                String line = server_in.nextLine();
                if (line.equals("finished")) {
                    break;
                }
                Main.writeResult(fileLoc, "");
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public static void main(String[] args) {
        new Client();
    }
}
