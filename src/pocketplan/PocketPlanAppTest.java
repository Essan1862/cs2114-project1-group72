package pocketplan;

import java.util.Scanner;
import student.TestCase;

public class PocketPlanAppTest
    extends TestCase
{

    private ExpenseTracker tracker;

    public void setUp()
    {
        tracker = new ExpenseTracker();
    }


    public void testConstructorValidScannerAndTracker()
    {
        Scanner scanner = new Scanner("8\n");

        PocketPlanApp app = new PocketPlanApp(scanner, tracker);

        assertNotNull(app);
    }


    public void testConstructorNullScannerThrowsException()
    {
        boolean exceptionWasThrown = false;

        try
        {
            PocketPlanApp app = new PocketPlanApp(null, tracker);
        }
        catch (IllegalArgumentException e)
        {
            exceptionWasThrown = true;
        }

        assertTrue(exceptionWasThrown);
    }


    public void testConstructorNullTrackerThrowsException()
    {
        Scanner scanner = new Scanner("8\n");
        boolean exceptionWasThrown = false;

        try
        {
            PocketPlanApp app = new PocketPlanApp(scanner, null);
        }
        catch (IllegalArgumentException e)
        {
            exceptionWasThrown = true;
        }

        assertTrue(exceptionWasThrown);
    }


    public void testRunSetBudgetAndAddExpenseUpdatesTracker()
    {
        // Menu path: 1 (set budget) -> 2 (add expense) -> 8 (exit)
        String scriptedInput =
            "1\n3000\n2000\n2\n45\nFOOD\nWeekly groceries\n2026-09-13\nyes\n8\n";
        Scanner scanner = new Scanner(scriptedInput);
        PocketPlanApp app = new PocketPlanApp(scanner, tracker);

        app.run();

        assertTrue(tracker.hasBudget());
        assertEquals(1, tracker.getExpenses().size());
        assertEquals(45.0, tracker.getTotalSpent(), 0.001);
        assertEquals(1955.0, tracker.getRemaining(), 0.001);
    }


    public void testRunInvalidMenuChoiceThenExitLeavesStateUnchanged()
    {
        String scriptedInput = "abc\n8\n";
        Scanner scanner = new Scanner(scriptedInput);
        PocketPlanApp app = new PocketPlanApp(scanner, tracker);

        app.run();

        assertFalse(tracker.hasBudget());
        assertEquals(0, tracker.getExpenses().size());
    }


    public void testRunEditExpense()
    {
        String scriptedInput =
            "1\n3000\n2000\n" + "2\n45\nFOOD\nGroceries\n2026-09-13\nyes\n"
                + "3\n1\n75\nENTERTAINMENT\nMovie\n2026-09-14\nno\n" + "8\n";

        Scanner scanner = new Scanner(scriptedInput);
        PocketPlanApp app = new PocketPlanApp(scanner, tracker);

        app.run();

        Expense expense = tracker.getExpenses().get(0);

        assertEquals(75.0, expense.getAmount(), 0.001);
        assertEquals(Category.ENTERTAINMENT, expense.getCategory());
        assertEquals("Movie", expense.getDescription());
        assertFalse(expense.isHabitual());
    }


    public void testRunDeleteExpense()
    {
        String scriptedInput = "1\n3000\n2000\n"
            + "2\n45\nFOOD\nGroceries\n2026-09-13\nyes\n" + "4\n1\n" + "8\n";

        Scanner scanner = new Scanner(scriptedInput);
        PocketPlanApp app = new PocketPlanApp(scanner, tracker);

        app.run();

        assertEquals(0, tracker.getExpenses().size());
        assertEquals(0.0, tracker.getTotalSpent(), 0.001);
    }


    public void testRunDisplayExpenses()
    {
        String scriptedInput = "1\n3000\n2000\n"
            + "2\n45\nFOOD\nGroceries\n2026-09-13\nyes\n" + "5\n" + "8\n";

        Scanner scanner = new Scanner(scriptedInput);
        PocketPlanApp app = new PocketPlanApp(scanner, tracker);

        app.run();

        String output = systemOut().getHistory();

        assertTrue(output.contains("Groceries"));
        assertTrue(output.contains("FOOD"));
        assertTrue(output.contains("45.00"));
    }


    public void testRunDisplaySummary()
    {
        String scriptedInput = "1\n3000\n2000\n"
            + "2\n100\nFOOD\nGroceries\n2026-09-13\nyes\n" + "6\n" + "8\n";

        Scanner scanner = new Scanner(scriptedInput);
        PocketPlanApp app = new PocketPlanApp(scanner, tracker);

        app.run();

        String output = systemOut().getHistory();

        assertTrue(output.contains("Monthly income"));
        assertTrue(output.contains("3000.00"));
        assertTrue(output.contains("Total budget"));
        assertTrue(output.contains("2000.00"));
        assertTrue(output.contains("Total spent"));
        assertTrue(output.contains("100.00"));
        assertTrue(output.contains("Remaining"));
        assertTrue(output.contains("1900.00"));
    }


    public void testRunDisplayReports()
    {
        String scriptedInput = "1\n3000\n2000\n"
            + "2\n100\nFOOD\nGroceries\n2026-09-13\nyes\n"
            + "2\n50\nENTERTAINMENT\nMovie\n2026-09-14\nno\n" + "7\n" + "8\n";

        Scanner scanner = new Scanner(scriptedInput);
        PocketPlanApp app = new PocketPlanApp(scanner, tracker);

        app.run();

        String output = systemOut().getHistory();

        assertTrue(output.contains("Category breakdown"));
        assertTrue(output.contains("Highest spending category: FOOD"));
        assertTrue(output.contains("Habitual total"));
        assertTrue(output.contains("100.00"));
        assertTrue(output.contains("Non-habitual total"));
        assertTrue(output.contains("50.00"));
    }

}
