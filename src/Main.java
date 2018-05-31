import java.util.Scanner;

/**
 * Asks the user to enter some runtime variables, and passes them to the Controller to start running everything.
 */
public class Main {
    public static boolean DEBUG_MODE = true;

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        System.out.println("Which PiCalculator do you want to use? (Select by typing number)\n1. Broken\t2. Single Control Variable\t3. Asymmetric\n");
        int choice = s.nextInt();
        System.out.println("How many elements to calculate?");
        long numElements = s.nextLong();
        System.out.println("How many threads to use?");
        int numThreads = s.nextInt();

        Controller controller = new Controller(numThreads, numElements, choice);
        double result = controller.doCalculation();
        System.out.println("The calculated value for Pi is: " + result);
//        for (int i = 0; i < 1000; i++) {
//            int numThreads = 9;
//            long numElements = 10000;
//
//            Controller controller = new Controller(numThreads, numElements);
//            double result = controller.doCalculation();
//            System.out.println("The calculated value for Pi is: " + result);
//        }
    }
}
