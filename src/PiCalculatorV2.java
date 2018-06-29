import java.io.BufferedWriter;

/**
 * Reserves locks in an Asymmetric fashion.
 */
public class PiCalculatorV2 extends PiCalculator {

    /**
     * Creates a PiCalculator to calculate the terms of the formula within the given range
     * @param controlVariables The control variables which will be used for synchronization.
     * @param id Identifier assigned by the Controller.
     * @param from Term of formula to calculate from (Inclusive).
     * @param to Term of formula to calculate to (Exclusive).
     */
    public PiCalculatorV2(Object[] controlVariables, int id, long from, long to, String filename, BufferedWriter writer) {
        this.filename = filename;
        this.controlVariables = controlVariables;
        this.id = id;
        this.writer = writer;
        beforeId = id == 0 ? controlVariables.length - 1 : id - 1;
        current = from;
        max = to;
        if (Main.DEBUG_MODE) System.out.println("Using PiCalculatorV2");
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
                calcPi();

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
}
