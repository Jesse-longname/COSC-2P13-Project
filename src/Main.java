package main;

import java.util.Scanner;

/**
 * Asks the user to enter some runtime variables, and passes them to the Controller to start running everything.
 */
public class Main {
    public static boolean DEBUG_MODE = true;

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        System.out.println("How many elements to calculate?:");
        long numElements = s.nextLong();
        System.out.println("How many threads to use?:");
        int numThreads = s.nextInt();
        System.out.println("Pick Solution (1/2):");
        int numSolution = s.nextInt();
        Controller controller = new Controller(numThreads, numElements, numSolution);
        double result = controller.doCalculation();
        System.out.println("The calculated value for Pi is: " + result);
    }
    
}
