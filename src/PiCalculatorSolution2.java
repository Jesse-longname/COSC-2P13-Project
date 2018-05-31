/**
 * Calculates a portion of Pi along with other instances of this class.
 * Uses the equation given in the assignment, to calculate the _first_ to _last_ terms
 *  given in the constructor (inclusive, exclusive).
 */
public class PiCalculatorSolution2 implements Runnable {
    double calculated = 0;
    long current;
    long max; // Calculate to this term
    final int id;
    final int beforeId; // Used to lock the control variable before id.
    Object[] controlVariables;

    /**
     * Creates a PiCalculator to calculate the terms of the formula within the given range
     * @param controlVariables The control variables which will be used for synchronization.
     * @param id Identifier assigned by the Controller.
     * @param from Term of formula to calculate from (Inclusive).
     * @param to Term of formula to calculate to (Exclusive).
     */
    public PiCalculatorSolution2(Object[] controlVariables, int id, long from, long to) {
        this.controlVariables = controlVariables;
        this.id = id;
        beforeId = id == 0 ? controlVariables.length - 1 : id - 1;
        current = from;
        max = to;
    }

    /**
     * Does the Pi calculations. This method is called by Thread.start();
     * The final result will be available in calculated after execution.
     */
    @Override
    public void run() {
        int firstSynchronize = id % 2 == 0 ? id : beforeId;
        int secondSynchronize = id % 2 == 1 ? id : beforeId;
        // Try to acquire the locks
        if (Main.DEBUG_MODE) System.out.println("Try acquire id: " + firstSynchronize);

        doAction();
        synchronized (controlVariables[firstSynchronize]) {
            if (Main.DEBUG_MODE) {
                System.out.println("Did acquire id: " + firstSynchronize);
                System.out.println("Try acquire id: " + secondSynchronize);
            }

            doAction();
            synchronized (controlVariables[secondSynchronize]) {
                if (Main.DEBUG_MODE) System.out.println("Did acquire beforeId: " + secondSynchronize);

                // Both locks are acquired, calculate terms in the designated range.
                int numerator = current % 2 == 0 ? 1 : -1;
                double denominator;
                while (current < max) {
                    denominator = 2 * current + 1;
                    calculated += numerator / denominator;
                    numerator *= -1; // Save computation by just multiplying by -1 each iteration?
                    current += 1;
                }

                if (Main.DEBUG_MODE) System.out.println("Try release id: " + secondSynchronize);
            }

            if (Main.DEBUG_MODE) {
                System.out.println("Did release id: " + secondSynchronize);
                System.out.println("Try release id: " + firstSynchronize);
            }
            // After first release, so doAction
            doAction();
        }
        if (Main.DEBUG_MODE) System.out.println("Did release id: " + firstSynchronize);

        // After second release, so doAction
        doAction();
    }

    /**
     * The doAction method from the assignment description.
     */
    private void doAction() {
        calculate((int)(Math.random() * 4 + 36));
    }

    /**
     * Calculates nth fibonacci term.
     * @param n
     * @return
     */
    private static long calculate(int n) {
        if (n <= 1) return n;
        else return calculate(n-1) + calculate(n-2);
    }
}
