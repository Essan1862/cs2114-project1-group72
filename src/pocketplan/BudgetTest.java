package pocketplan;

import student.TestCase;

public class BudgetTest
    extends TestCase
{
    private Budget budget;

    public void setUp()
        throws InvalidInputException
    {
        budget = new Budget(500.00, 400.00);
    }


    public void testGetIncome()
    {
        assertEquals(500.00, budget.getIncome(), 0.001);
    }


    public void testGetTotalBudget()
    {
        assertEquals(400.00, budget.getTotalBudget(), 0.001);
    }


    public void testGetRemaining()
    {
        double expected = 400.00 - 300.00;
        double result = budget.getRemaining(300.00);
        assertEquals(expected, result, 0.001);
    }


    public void testWouldExceedBudget()
    {
        double totalSpent = 300.00;
        double newAmount = 150.00;
        boolean result = budget.wouldExceedBudget(totalSpent, newAmount);

        assertTrue(result);
    }


    public void testWouldExceedBudgetFalse()
    {
        double totalSpent = 300.00;
        double newAmount = 10.00;
        boolean result = budget.wouldExceedBudget(totalSpent, newAmount);

        assertFalse(result);
    }


    public void testWouldExceedBudgetFalseBoundary()
    {
        double totalSpent = 300.00;
        double newAmount = 100.00;
        boolean result = budget.wouldExceedBudget(totalSpent, newAmount);

        assertFalse(result);
    }


    public void testConstructorBudgetExceedsIncome()
    {
        boolean exceptionWasThrown = false;

        try
        {
            Budget invalidBudget = new Budget(500.00, 600.00);
        }
        catch (InvalidInputException e)
        {
            exceptionWasThrown = true;
        }

        assertTrue(exceptionWasThrown);
    }

}
