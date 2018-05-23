/**
 * Sets up multiple PiCalculators and has any control variables used by the PiCalculators.
 * Has a method to calculate the value of Pi.
 */
public class Controller {
    long numElements;
    int numThreads;

    PiCalculator[] calculators;
    Thread[] threads;
    Object[] controlVariables; // Using general Object, because it is only there to be locked.

    /**
     * Creates the controller class and does basic setup for the threads and calculators.
     * @param numThreads The number of threads to use.
     * @param numElements How many elements of the Pi equation to calculate in total.
     */
    public Controller(int numThreads, long numElements) {
        this.numElements = numElements;
        this.numThreads = numThreads;

        // Set up threads
        calculators = new PiCalculator[numThreads];
        threads = new Thread[numThreads];
        // Set up the control variables
        controlVariables = new Object[numThreads];
        for (int i = 0; i < numThreads; i++) {
            controlVariables[i] = new Object();
        }
    }

    /**
     * Starts of the PiCalculators, waits for them to finish, and returns to cumulative value that they calculated.
     * @return The value of Pi
     */
    public double doCalculation() {
        // Set up all but the last thread, since it will have the remaining elements.
        long elementsPerThread = numElements / numThreads;
        for (int i = 0; i < numThreads-1; i++) {
            calculators[i] = new PiCalculator(controlVariables, i, i*elementsPerThread, ((i+1)*elementsPerThread));
            threads[i] = new Thread(calculators[i]);
        }

        // Set up final thread with the remaining elements
        calculators[numThreads - 1] = new PiCalculator(controlVariables,numThreads-1, (numThreads-1)*elementsPerThread, numElements);
        threads[numThreads - 1] = new Thread(calculators[numThreads - 1]);

        // Start all of the Threads
        for (int i = 0; i < numThreads; i++) {
            threads[i].start();
        }

        // Wait for all of the threads to finish
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
            total += calculators[i].calculated;
        }
        return total*4;
    }
}
