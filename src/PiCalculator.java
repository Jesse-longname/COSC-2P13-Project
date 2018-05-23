/**
 * Calculates a portion of Pi along with other instances of this class.
 * Uses the equation given in the assignment, to calculate the _first_ to _last_ terms
 *  given in the constructor (inclusive, exclusive).
 */
public class PiCalculator implements Runnable {
    double calculated = 0;
    long current;
    long max; // Calculate to this term
    final int id;
    Controller controller;

    // Set the range of terms to calculate pi for. (inclusive, exclusive).
    public PiCalculator(Controller controller, int id, long from, long to) {
        this.controller = controller;
        this.id = id;
        current = from;
        max = to;
    }

    /**
     * Does the Pi calculations. This method is called by Thread.start();
     * The final result will be available in calculated after execution.
     */
    @Override
    public void run() {
        // Try to acquire the locks
        controller.acquireLocks(this.id);
        // Wait until they're acquired
        int numerator = current % 2 == 0 ? 1 : -1;
        double denominator;
        while (current < max) {
            denominator = 2 * current + 1; // Double so our division below will be right.
            calculated += numerator / denominator;
            numerator *= -1; // Save computation by just multiplying by -1 each iteration?
            current += 1;
        }
        // Release the lock
        controller.releaseLocks(id);
    }
}
