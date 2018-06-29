import java.io.*;
import java.nio.file.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * Asks the user to enter some runtime variables, and passes them to the Controller to start running everything.
 */
public class Main {
    public static boolean DEBUG_MODE = false;
    public static int FILE_MODE = 1; // 0 = no file output, 1 = Output using Buffered, 2 = Output using Files.
    public static long total_time;

    public static void main(String[] args) {
        String file = args[1];
        try {
            FILE_MODE = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.out.println("Incorrect Write Mode. Please enter 1 for Buffered, 2 for Files.");
            return;
        }
        if (!Files.exists(FileSystems.getDefault().getPath(file))) {
            System.out.println("Your input file does not exist");
            return;
        }
        List<String> runtimeArgs = new LinkedList<>();
        if (FILE_MODE == 0 && DEBUG_MODE) {
            System.out.println("Only reading, not writing.");
        }
        try {
            if (FILE_MODE == 1 || FILE_MODE == 0) {
                if (DEBUG_MODE) {
                    System.out.println("Using Buffered Solution");
                }
                BufferedReader in = null;
                    in = new BufferedReader(new FileReader(file));
                    String line = in.readLine();
                    while (line != null) {
                        runtimeArgs.add(line);
                        line = in.readLine();
                    }
                    in.close();
                    run(runtimeArgs);
            } else if (FILE_MODE == 2) {
                if (DEBUG_MODE) {
                    System.out.println("Using Files. Solution");
                }
                runtimeArgs = Files.readAllLines(FileSystems.getDefault().getPath(file));
                run(runtimeArgs);
            }
        } catch (FileNotFoundException  e) {
            System.err.println(e);
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    /**
     * Reads the arguments and performs the tests.
     * First line should be output file.
     * Following lines will be in the form "type numThreads numElements"
     * @param args
     */
    private static void run(List<String> args) {
        total_time = System.nanoTime();
        String outputLocation = args.get(0);
        createFile(outputLocation);
        int type, numThreads, numElements;
        String[] vals;
        Controller controller;
        double result;
        for (int i = 1; i < args.size(); i++) {
            vals = args.get(i).split(" ");
            type = Integer.parseInt(vals[0]);
            numThreads = Integer.parseInt(vals[1]);
            numElements = Integer.parseInt(vals[2]);
            controller = new Controller(numThreads, numElements, type, outputLocation);
            result = controller.doCalculation();
            writeResult(outputLocation, "Result from pass " + i + ": " + result);
            if (DEBUG_MODE) {
                System.out.println("CALCULATION " + i);
                System.out.println("The calculated value for Pi is: " + result);
            }
        }
        total_time = System.nanoTime() - total_time;
        if (DEBUG_MODE) {
            writeResult(outputLocation, "Total Time: " + total_time);
            System.out.println("Total Time: " + total_time);
        }
    }

    /**
     * Creates a new file at the given location, erasing any previous file.
     * @param filePath
     */
    public static void createFile(String filePath) {
        try {
            Files.deleteIfExists(FileSystems.getDefault().getPath(filePath));
            Files.createFile(FileSystems.getDefault().getPath(filePath));
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    /**
     * Writes the given value to the given file, with the FILE_MODE selected at the beginning.
     * @param filename
     * @param value
     */
    public static void writeResult(String filename, String value) {
        if (FILE_MODE == 0) return;
        try {
            if (FILE_MODE == 1) {
                BufferedWriter out = new BufferedWriter(new FileWriter(filename, true));
                out.write(value);
                out.write(System.lineSeparator());
                out.close();
            } else if (FILE_MODE == 2) {
                List<String> toWrite = new LinkedList<>();
                toWrite.add(value);
                Files.write(FileSystems.getDefault().getPath(filename), toWrite, StandardOpenOption.APPEND);
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }
}
