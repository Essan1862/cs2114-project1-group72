package pocketplan;

import student.TestCase;

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
    extends TestCase
{

    private ExpenseTracker tracker;
    private LocalDate testDate;
    private final String TEST_FILE = "test_expenses_temp.csv";

    public void setUp()
    {
        tracker = new ExpenseTracker();
        testDate = LocalDate.of(2026, 9, 18);
    }


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


    public void testEditExpense()
        throws Exception
    {
        tracker.setBudget(3000.0, 2000.0);

        Expense expense = tracker
            .addExpense(50.0, Category.FOOD, "Groceries", testDate, true);

        tracker.editExpense(
            expense.getId(),
            75.0,
            Category.ENTERTAINMENT,
            "Movie",
            testDate,
            false);

        assertEquals(75.0, expense.getAmount(), 0.001);
        assertEquals(Category.ENTERTAINMENT, expense.getCategory());
        assertEquals("Movie", expense.getDescription());
        assertFalse(expense.isHabitual());
    }


    public void testEditExpenseExceedsBudget()
        throws Exception
    {
        tracker.setBudget(1000.0, 500.0);

        Expense expense =
            tracker.addExpense(100.0, Category.FOOD, "Food", testDate, true);

        try
        {
            tracker.editExpense(
                expense.getId(),
                600.0,
                Category.FOOD,
                "Expensive food",
                testDate,
                true);

            fail("Expected InvalidInputException.");
        }
        catch (InvalidInputException e)
        {
            assertTrue(e.getMessage().contains("exceed the total budget"));
        }
    }


    public void testDeleteExpense()
        throws Exception
    {
        tracker.setBudget(1000.0, 1000.0);

        Expense expense =
            tracker.addExpense(50.0, Category.FOOD, "Food", testDate, true);

        Expense deleted = tracker.deleteExpense(expense.getId());

        assertEquals(expense, deleted);
        assertEquals(0, tracker.getExpenses().size());
        assertEquals(0.0, tracker.getTotalSpent(), 0.001);
    }


    public void testGetCategoryBreakdown()
        throws Exception
    {
        tracker.setBudget(3000.0, 2000.0);

        tracker.addExpense(50.0, Category.FOOD, "Lunch", testDate, true);

        tracker.addExpense(75.0, Category.FOOD, "Dinner", testDate, false);

        tracker.addExpense(
            100.0,
            Category.ENTERTAINMENT,
            "Concert",
            testDate,
            false);

        assertEquals(
            125.0,
            tracker.getCategoryBreakdown().get(Category.FOOD),
            0.001);

        assertEquals(
            100.0,
            tracker.getCategoryBreakdown().get(Category.ENTERTAINMENT),
            0.001);
    }


    public void testHabitualAndNonHabitualTotals()
        throws Exception
    {
        tracker.setBudget(3000.0, 2000.0);

        tracker.addExpense(100.0, Category.FOOD, "Groceries", testDate, true);

        tracker
            .addExpense(50.0, Category.ENTERTAINMENT, "Movie", testDate, false);

        tracker
            .addExpense(25.0, Category.TRANSPORTATION, "Gas", testDate, true);

        assertEquals(125.0, tracker.getHabitualTotal(), 0.001);

        assertEquals(50.0, tracker.getNonHabitualTotal(), 0.001);
    }


    public void testResetForNewMonth()
        throws Exception
    {
        tracker.setBudget(3000.0, 2000.0);

        tracker.addExpense(100.0, Category.FOOD, "Food", testDate, true);

        tracker.resetForNewMonth(4000.0, 2500.0);

        assertEquals(0, tracker.getExpenses().size());
        assertEquals(0.0, tracker.getTotalSpent(), 0.001);
        assertEquals(4000.0, tracker.getBudget().getIncome(), 0.001);
        assertEquals(2500.0, tracker.getBudget().getTotalBudget(), 0.001);

        Expense newExpense = tracker
            .addExpense(50.0, Category.FOOD, "New month", testDate, false);

        assertEquals(1, newExpense.getId());
    }


    public void testSaveAndLoadBudgetCSV()
        throws Exception
    {
        String budgetFile = "test_budget_temp.csv";

        tracker.setBudget(5000.0, 3000.0);
        tracker.saveBudgetToCSV(budgetFile);

        ExpenseTracker loadedTracker = new ExpenseTracker();

        loadedTracker.loadBudgetFromCSV(budgetFile);

        assertTrue(loadedTracker.hasBudget());
        assertEquals(5000.0, loadedTracker.getBudget().getIncome(), 0.001);
        assertEquals(3000.0, loadedTracker.getBudget().getTotalBudget(), 0.001);

        File file = new File(budgetFile);

        if (file.exists())
        {
            file.delete();
        }
    }
}
