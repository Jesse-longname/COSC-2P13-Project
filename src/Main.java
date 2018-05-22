import java.util.Scanner;

/**
 * Calculates Pi using the formula given in the assignment. Asks for a number of
 *  elements to calculate, then asks for a number of Threads to use.
 * After a;; Threads are finished, prints the calculated value to the console.
 */
public class Main {

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        System.out.println("How many elements to calculate?");
        long numElements = s.nextLong();
        System.out.println("How many threads to use?");
        int numThreads = s.nextInt();

        // Set up the Threads.
        long perThread = numElements / numThreads;
        PiCalculator[] calcs = new PiCalculator[numThreads];
        Thread[] threads = new Thread[numThreads];

        // Set up all but the last thread, because the last thread will have the remaining elements.
        for (int i = 0; i < numThreads-1; i++) {
            calcs[i] = new PiCalculator(i*perThread, ((i+1)*perThread));
            threads[i] = new Thread(calcs[i]);
        }
        // Set up final thread with remaining elements
        calcs[numThreads - 1] = new PiCalculator((numThreads-1)*perThread, numElements);
        threads[numThreads - 1] = new Thread(calcs[numThreads - 1]);

        // Start all of the Threads
        for (int i = 0; i < numThreads; i++) {
            threads[i].start();
        }

        // Wait for all of the threads to join
        for (int i = 0; i < numThreads; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Total up all of the terms
        double total = 0;
        for (int i = 0; i < numThreads; i++) {
            total += calcs[i].calculated;
        }
        System.out.println("The calculated total is: " + total * 4);
    }
}
