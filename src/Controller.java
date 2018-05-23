import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class Controller {
    long numElements;
    int numThreads;
    PiCalculator[] calculators;
    Thread[] threads;
    Semaphore[] semaphores;
    ArrayList<Integer> acquired;

    public Controller(int numThreads, long numElements) {
        this.numElements = numElements;
        this.numThreads = numThreads;
        // Set up threads
        calculators = new PiCalculator[numThreads];
        threads = new Thread[numThreads];
        // Set up semaphores
        semaphores = new Semaphore[numThreads];
        for (int i = 0; i < numThreads; i++) {
            semaphores[i] = new Semaphore(1);
        }

        // Debugging
        acquired = new ArrayList<>();
    }

    public double doCalculation() {
        // Set up all but the last thread, since it will have the remaining elements.
        long elementsPerThread = numElements / numThreads;
        for (int i = 0; i < numThreads-1; i++) {
            calculators[i] = new PiCalculator(this, i, i*elementsPerThread, ((i+1)*elementsPerThread));
            threads[i] = new Thread(calculators[i]);
        }

        // Set up final thread with the remaining elements
        calculators[numThreads - 1] = new PiCalculator(this,numThreads-1, (numThreads-1)*elementsPerThread, numElements);
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

    public void acquireLocks(int id) {
        int beforeId = id == 0 ? numThreads - 1 : id - 1;
        try {
            doAction();
            System.out.println("Try Acquire before id: " + beforeId);
            semaphores[beforeId].acquire();
            System.out.println("Did Acquire before id: " + beforeId);
            doAction();
            System.out.println("Try Acquire id: " + id);
            semaphores[id].acquire();
            System.out.println("Did Acquire id: " + id);
        } catch(InterruptedException e) {
            System.out.println("Interrupted Exception: ");
            e.printStackTrace();
        }
    }

    public void releaseLocks(int id) {
        int beforeId = id == 0 ? numThreads - 1 : id - 1;
        //release
        System.out.println("Try Release before id: " + beforeId);
        semaphores[beforeId].release();
        System.out.println("Did Release before id: " + beforeId);
        doAction();
        System.out.println("Try Release id: " + id);
        semaphores[id].release();
        System.out.println("Did Release id: " + id);
        doAction();
        doAction();
    }

    public void doAction() {
        calculate((int)(Math.random() * 4 + 36));
    }

    private static long calculate(int n) {
        if (n <= 1) return n;
        else return calculate(n-1) + calculate(n-2);
    }

    private synchronized void printAcquired() {
        for (int a: acquired) {
            System.out.print(a + " ");
        }
        System.out.println();
    }
}
