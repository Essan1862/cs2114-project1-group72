package pocketplan;

import java.time.LocalDate;
import student.TestCase;

/**
 * Tests for Expense.
 */
public class ExpenseTest extends TestCase {

    private LocalDate sampleDate;

    public void setUp() {
        sampleDate = LocalDate.of(2026, 9, 13);
    }

    // Expense(): normal case
    public void testConstructorValidValuesStoresAllFields() {
        Expense expense = new Expense(
            1, 45.50, Category.FOOD, "Groceries", sampleDate, true);

        assertEquals(1, expense.getId());
        assertEquals(45.50, expense.getAmount());
        assertEquals(Category.FOOD, expense.getCategory());
        assertEquals("Groceries", expense.getDescription());
        assertEquals(sampleDate, expense.getDate());
        assertTrue(expense.isHabitual());
    }

    // Expense(): bad-input case
    public void testConstructorInvalidAmountThrowsException() {
        boolean exceptionWasThrown = false;

        try {
            Expense expense = new Expense(
                1, -5.0, Category.FOOD, "Groceries", sampleDate, true);
        }
        catch (IllegalArgumentException e) {
            exceptionWasThrown = true;
        }

        assertTrue(exceptionWasThrown);
    }

    // update(): normal case
    public void testUpdateChangesFieldsExceptId() {
        Expense expense = new Expense(
            1, 45.50, Category.FOOD, "Groceries", sampleDate, true);

        expense.update(60.00, Category.ENTERTAINMENT, "New purchase",
            sampleDate, false);

        assertEquals(1, expense.getId());
        assertEquals(60.00, expense.getAmount());
        assertEquals(Category.ENTERTAINMENT, expense.getCategory());
        assertFalse(expense.isHabitual());
    }

    // update(): bad-input case
    public void testUpdateWithInvalidAmountLeavesFieldsUnchanged() {
        Expense expense = new Expense(
            1, 45.50, Category.FOOD, "Groceries", sampleDate, true);
        boolean exceptionWasThrown = false;

        try {
            expense.update(-5.0, Category.ENTERTAINMENT, "New purchase",
                sampleDate, false);
        }
        catch (IllegalArgumentException e) {
            exceptionWasThrown = true;
        }

        assertTrue(exceptionWasThrown);
        assertEquals(45.50, expense.getAmount()); // unchanged
    }

    // equals(): normal case
    public void testEqualsSameValuesReturnsTrue() {
        Expense expenseA = new Expense(
            1, 45.50, Category.FOOD, "Groceries", sampleDate, true);
        Expense expenseB = new Expense(
            1, 45.50, Category.FOOD, "Groceries", sampleDate, true);

        assertTrue(expenseA.equals(expenseB));
    }

    // equals(): bad case
    public void testEqualsDifferentValuesReturnsFalse() {
        Expense expenseA = new Expense(
            1, 45.50, Category.FOOD, "Groceries", sampleDate, true);
        Expense expenseB = new Expense(
            2, 99.99, Category.ENTERTAINMENT, "Movie", sampleDate, false);

        assertFalse(expenseA.equals(expenseB));
    }

    // hashCode(): consistency with equals
    public void testHashCodeMatchesForEqualExpenses() {
        Expense expenseA = new Expense(
            1, 45.50, Category.FOOD, "Groceries", sampleDate, true);
        Expense expenseB = new Expense(
            1, 45.50, Category.FOOD, "Groceries", sampleDate, true);

        assertEquals(expenseA.hashCode(), expenseB.hashCode());
    }
}
