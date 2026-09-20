import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.time.LocalDate;
import java.util.Optional;

/**
 * Test class for ExpenseTracker. Focuses on normal operations, edge-case
 * invalid states, and CSV persistence.
 * 
 * @author Wenxin Zhang
 * @verison 2026.09.18
 */
public class ExpenseTrackerTest
{

    private ExpenseTracker tracker;
    private LocalDate testDate;
    private final String TEST_FILE = "test_expenses_temp.csv";

    @Before
    public void setUp()
    {
        tracker = new ExpenseTracker();
        testDate = LocalDate.of(2026, 9, 18);
    }


    @Test
    public void testSetBudgetAndAddExpense()
        throws Exception
    {
        tracker.setBudget(3000.0, 2000.0);
        assertTrue(tracker.hasBudget());

        tracker.addExpense(
            50.0,
            Category.FOOD,
            "Kroger groceries",
            testDate,
            true);

        assertEquals(1, tracker.getExpenses().size());
        assertEquals(50.0, tracker.getTotalSpent(), 0.001);
        assertEquals(1950.0, tracker.getRemaining(), 0.001);
    }


    @Test
    public void testGetHighestSpendingCategory()
        throws Exception
    {
        tracker.setBudget(5000.0, 4000.0);
        tracker.addExpense(100.0, Category.FOOD, "Food", testDate, true);
        tracker.addExpense(500.0, Category.HOUSING, "Rent", testDate, true);
        tracker
            .addExpense(50.0, Category.ENTERTAINMENT, "Game", testDate, false);

        Optional<Category> highest = tracker.getHighestSpendingCategory();
        assertTrue(highest.isPresent());
        assertEquals(Category.HOUSING, highest.get());
    }


    @Test
    public void testSaveAndLoadCSV()
        throws Exception
    {
        tracker.setBudget(5000.0, 3000.0);
        tracker.addExpense(100.0, Category.FOOD, "Lunch", testDate, true);
        tracker
            .addExpense(200.0, Category.TRANSPORTATION, "Gas", testDate, false);

        tracker.saveToCSV(TEST_FILE);
        File savedFile = new File(TEST_FILE);
        assertTrue("CSV file should be created", savedFile.exists());

        ExpenseTracker loadedTracker = new ExpenseTracker();
        loadedTracker.setBudget(5000.0, 3000.0);
        loadedTracker.loadFromCSV(TEST_FILE);

        assertEquals(2, loadedTracker.getExpenses().size());
        assertEquals(
            100.0,
            loadedTracker.getExpenses().get(0).getAmount(),
            0.001);
        assertEquals(
            "Lunch",
            loadedTracker.getExpenses().get(0).getDescription());
        assertEquals(
            Category.FOOD,
            loadedTracker.getExpenses().get(0).getCategory());
        assertTrue(loadedTracker.getExpenses().get(0).isHabitual());

        var newExpense = loadedTracker
            .addExpense(50.0, Category.ENTERTAINMENT, "Movie", testDate, false);
        assertEquals(3, newExpense.getId());

        if (savedFile.exists())
        {
            savedFile.delete();
        }
    }


    @Test
    public void testAddExpenseWithoutBudgetThrowsException()
    {
        try
        {
            tracker.addExpense(50.0, Category.FOOD, "Food", testDate, true);
            fail(
                "Expected an InvalidInputException to be thrown because budget is not set.");
        }
        catch (Exception e)
        {
            assertTrue(e.getMessage().contains("Budget is not set"));
        }
    }


    @Test
    public void testAddExpenseExceedsBudgetThrowsException()
        throws Exception
    {
        tracker.setBudget(1000.0, 500.0);

        try
        {
            tracker.addExpense(
                600.0,
                Category.ENTERTAINMENT,
                "Concert",
                testDate,
                false);
            fail(
                "Expected an InvalidInputException because amount exceeds budget.");
        }
        catch (Exception e)
        {
            assertTrue(e.getMessage().contains("exceed the total budget"));
        }

        assertEquals(0, tracker.getExpenses().size());
    }


    @Test
    public void testDeleteExpenseWithInvalidIdThrowsException()
        throws Exception
    {
        tracker.setBudget(1000.0, 1000.0);
        tracker.addExpense(50.0, Category.FOOD, "Food", testDate, true);

        try
        {
            tracker.deleteExpense(99);
            fail("Expected InvalidInputException for non-existent ID.");
        }
        catch (Exception e)
        {
            assertTrue(e.getMessage().contains("ID not found"));
        }
    }
}
