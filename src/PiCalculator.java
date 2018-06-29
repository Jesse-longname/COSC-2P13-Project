import java.io.BufferedWriter;
import java.nio.Buffer;

public abstract class PiCalculator implements Runnable {
    double calculated = 0;
    long current;
    long max;
    int id;
    int beforeId;
    String filename;
    Object[] controlVariables;
    BufferedWriter writer;

    public PiCalculator() {
        this.id = -1;
        this.beforeId = -1;
    }

    /**
     * Creates a PiCalculator to calculate the terms of the formula within the given range
     * @param controlVariables The control variables which will be used for synchronization.
     * @param id Identifier assigned by the Controller.
     * @param from Term of formula to calculate from (Inclusive).
     * @param to Term of formula to calculate to (Exclusive).
     */
    public PiCalculator(Object[] controlVariables, int id, long from, long to, String filename, BufferedWriter writer) {
        this.filename = filename;
        this.controlVariables = controlVariables;
        this.id = id;
        this.writer = writer;
        beforeId = id == 0 ? controlVariables.length - 1 : id - 1;
        current = from;
        max = to;
    }

    protected void calcPi() {
        int numerator = current % 2 == 0 ? 1 : -1;
        double denominator;
        int i = 0;
        while (current < max) {
            denominator = 2 * current + 1;
            calculated += numerator / denominator;
            numerator *= -1;
            current += 1;
            Main.writeResult(writer, filename, this.id + " " + i + " " + calculated);
            i++;
        }
    }

    /**
     * The doAction method from the assignment description.
     */
    protected void doAction() {
        calculate((int)(Math.random() * 4 + 36));
    }

    /**
     * Calculates nth fibonacci term.
     * @param n
     * @return
     */
    protected static long calculate(int n) {
        if (n <= 1) return n;
        else return calculate(n-1) + calculate(n-2);
    }
}
