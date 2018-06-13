/**
 * Reserves the locks by first reserving an Object that represents both locks.
 */
public class PiCalculatorV1 extends PiCalculator {
    /**
     * Creates a PiCalculator to calculate the terms of the formula within the given range
     * @param controlVariables The control variables which will be used for synchronization.
     * @param id Identifier assigned by the Controller.
     * @param from Term of formula to calculate from (Inclusive).
     * @param to Term of formula to calculate to (Exclusive).
     */
    public PiCalculatorV1(Object[] controlVariables, int id, long from, long to, String filename) {
        this.filename = filename;
        this.controlVariables = controlVariables;
        this.id = id;
        beforeId = id == 0 ? controlVariables.length - 1 : id - 1;
        current = from;
        max = to;
        if (Main.DEBUG_MODE) System.out.println("Using PiCalculatorV1");
    }

    /**
     * Does the Pi calculations. This method is called by Thread.start();
     * The final result will be available in calculated after execution.
     */
    @Override
    public void run() {
        // Try to acquire the locks
        if (Main.DEBUG_MODE) System.out.println("Try acquire beforeId: " + beforeId);

        doAction();

        Object firstLock = new Object(), secondLock = new Object();
        if(beforeId == id){
            try {
                throw new Exception("Please use more than one thread");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if(beforeId < id){
            firstLock = controlVariables[beforeId];
            secondLock = controlVariables[id];
        } else {
            secondLock = controlVariables[beforeId];
            firstLock = controlVariables[id];
        }


        synchronized (firstLock) {
            if (Main.DEBUG_MODE) {
                System.out.println("Did acquire beforeId: " + beforeId);
                System.out.println("Try acquire id: " + beforeId);
            }

            doAction();
            synchronized (secondLock) {
                if (Main.DEBUG_MODE) System.out.println("Did acquire beforeId: " + beforeId);

                // Both locks are acquired, calculate terms in the designated range.
                calcPi();

                if (Main.DEBUG_MODE) System.out.println("Try release id: " + id);
            }

            if (Main.DEBUG_MODE) {
                System.out.println("Did release id: " + id);
                System.out.println("Try release beforeId: " + beforeId);
            }
            // After first release, so doAction
            doAction();
        }
        if (Main.DEBUG_MODE) System.out.println("Did release beforeId: " + beforeId);

        // After second release, so doAction
        doAction();
    }
}
