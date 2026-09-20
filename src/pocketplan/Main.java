import java.util.Scanner;

public class Main
{
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ExpenseTracker tracker = new ExpenseTracker();
        PocketPlanApp app = new PocketPlanApp(scanner, tracker);

        app.run();

        scanner.close();
    }
}
