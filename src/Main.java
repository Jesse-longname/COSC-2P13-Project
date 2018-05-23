import java.util.Scanner;

/**
 * Calculates Pi using the formula given in the assignment. Asks for a number of
 *  elements to calculate, then asks for a number of Threads to use.
 * After a;; Threads are finished, prints the calculated value to the console.
 */
public class Main {



    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            Scanner s = new Scanner(System.in);
            System.out.println("How many elements to calculate?");
//        long numElements = s.nextLong();
            long numElements = 100000;
            System.out.println("How many threads to use?");
//        int numThreads = s.nextInt();
            int numThreads = 10;

            Controller controller = new Controller(numThreads, numElements);
            double result = controller.doCalculation();
            System.out.println("The calculated value for Pi is: " + result);
        }
    }
}
