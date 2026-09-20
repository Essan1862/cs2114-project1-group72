import java.util.Scanner;
import student.TestCase;

public class PocketPlanAppTest extends TestCase {

    private ExpenseTracker tracker;

    public void setUp() {
        tracker = new ExpenseTracker();
    }

    public void testConstructorValidScannerAndTracker() {
        Scanner scanner = new Scanner("8\n");

        PocketPlanApp app = new PocketPlanApp(scanner, tracker);

        assertNotNull(app);
    }

    public void testConstructorNullScannerThrowsException() {
        boolean exceptionWasThrown = false;

        try {
            PocketPlanApp app = new PocketPlanApp(null, tracker);
        }
        catch (IllegalArgumentException e) {
            exceptionWasThrown = true;
        }

        assertTrue(exceptionWasThrown);
    }

    public void testConstructorNullTrackerThrowsException() {
        Scanner scanner = new Scanner("8\n");
        boolean exceptionWasThrown = false;

        try {
            PocketPlanApp app = new PocketPlanApp(scanner, null);
        }
        catch (IllegalArgumentException e) {
            exceptionWasThrown = true;
        }

        assertTrue(exceptionWasThrown);
    }

    public void testRunSetBudgetAndAddExpenseUpdatesTracker() {
        // Menu path: 1 (set budget) -> 2 (add expense) -> 8 (exit)
        String scriptedInput = "1\n3000\n2000\n2\n45\nGROCERIES\nWeekly groceries\n2026-09-13\nyes\n8\n";
        Scanner scanner = new Scanner(scriptedInput);
        PocketPlanApp app = new PocketPlanApp(scanner, tracker);

        app.run();

        assertTrue(tracker.hasBudget());
        assertEquals(1, tracker.getExpenses().size());
        assertEquals(45.0, tracker.getTotalSpent(), 0.001);
        assertEquals(1955.0, tracker.getRemaining(), 0.001);
    }

    public void testRunInvalidMenuChoiceThenExitLeavesStateUnchanged() {
        String scriptedInput = "abc\n8\n";
        Scanner scanner = new Scanner(scriptedInput);
        PocketPlanApp app = new PocketPlanApp(scanner, tracker);

        app.run();

        assertFalse(tracker.hasBudget());
        assertEquals(0, tracker.getExpenses().size());
    }
}
