import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Test class for ExpenseTracker.
 * Focuses on both normal operations and edge-case invalid states.
 */
public class ExpenseTrackerTest {

    private ExpenseTracker tracker;
    private LocalDate testDate;

    @Before
    public void setUp() {
        tracker = new ExpenseTracker();
        testDate = LocalDate.of(2026, 9, 18);
    }

    // ---------------------------------------------------------

    @Test
    public void testSetBudgetAndAddExpense() throws Exception {

        tracker.setBudget(3000.0, 2000.0);
        assertTrue(tracker.hasBudget());

        tracker.addExpense(50.0, Category.FOOD, "Kroger groceries", testDate, true);
        
        assertEquals(1, tracker.getExpenses().size());
        assertEquals(50.0, tracker.getTotalSpent(), 0.001);
        assertEquals(1950.0, tracker.getRemaining(), 0.001); // 2000 - 50 = 1950
    }

    @Test
    public void testGetHighestSpendingCategory() throws Exception {
        tracker.setBudget(5000.0, 4000.0);
        tracker.addExpense(100.0, Category.FOOD, "Food", testDate, true);
        tracker.addExpense(500.0, Category.HOUSING, "Rent", testDate, true);
        tracker.addExpense(50.0, Category.ENTERTAINMENT, "Game", testDate, false);

        Optional<Category> highest = tracker.getHighestSpendingCategory();
        assertTrue(highest.isPresent());
        assertEquals(Category.HOUSING, highest.get());
    }

    // ---------------------------------------------------------

    @Test
    public void testAddExpenseWithoutBudgetThrowsException() {
        try {
            tracker.addExpense(50.0, Category.FOOD, "Food", testDate, true);
            fail("Expected an InvalidInputException to be thrown because budget is not set.");
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("Budget is not set"));
        }
    }

    @Test
    public void testAddExpenseExceedsBudgetThrowsException() throws Exception {
        tracker.setBudget(1000.0, 500.0); 
        
        try {
            tracker.addExpense(600.0, Category.ENTERTAINMENT, "Concert", testDate, false);
            fail("Expected an InvalidInputException because amount exceeds budget.");
        } catch (Exception e) {

            assertTrue(e.getMessage().contains("exceed the total budget"));
        }
        
        assertEquals(0, tracker.getExpenses().size());
    }

    @Test
    public void testDeleteExpenseWithInvalidIdThrowsException() throws Exception {
        tracker.setBudget(1000.0, 1000.0);
        tracker.addExpense(50.0, Category.FOOD, "Food", testDate, true); 

        try {

            tracker.deleteExpense(99);
            fail("Expected InvalidInputException for non-existent ID.");
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("ID not found"));
        }
    }
}
