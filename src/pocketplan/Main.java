package pocketplan;

import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Starting point for PocketPlan. Wires the pieces together and runs the app.
 *
 * @author Essan Salem
 * @version 2026.09.20
 */
public class Main
{
    /**
     * Creates the shared objects, restores any saved data, runs the menu,
     * then saves before closing.
     *
     * @param args
     *            command line arguments, not used
     */
    public static void main(String[] args)
    {
        String expensesFilepath = "expenses.csv";
        String budgetFilepath = "budget.csv";
        Scanner scanner = new Scanner(System.in);
        ExpenseTracker tracker = new ExpenseTracker();

        try
        {
            tracker.loadBudgetFromCSV(budgetFilepath);
        }
        catch (FileNotFoundException e)
        {
            System.out.println("No saved budget found - please set one.");
        }

        try
        {
            tracker.loadFromCSV(expensesFilepath);
        }
        catch (FileNotFoundException e)
        {
            System.out.println("No saved expenses found - starting fresh.");
        }

        PocketPlanApp app = new PocketPlanApp(scanner, tracker);
        app.run(); // user interacts with the menu here; when they pick Exit,
                   // run() returns

        try
        {
            tracker.saveBudgetToCSV(budgetFilepath);
        }
        catch (FileNotFoundException e)
        {
            System.out.println("Could not save budget: " + e.getMessage());
        }

        try
        {
            tracker.saveToCSV(expensesFilepath);
        }
        catch (FileNotFoundException e)
        {
            System.out.println("Could not save expenses: " + e.getMessage());
        }

        scanner.close();
    }
}
