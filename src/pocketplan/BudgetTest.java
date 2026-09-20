package pocketplan;

import student.TestCase;

public class BudgetTest extends TestCase{
    private Budget budget;
    
    public void setUp() {
        budget = new Budget(500.00, 400.00);
    }
    
    public void testGetIncome() {
        assertEquals(500.00, budget.getIncome());
    }
    
    public void testGetTotalBudget() {
        assertEquals(400.00, budget.getTotalBudget());
    }
    
    public void testGetRemaining() {
        double totalSpent = new Double(300.00);
        expected = 400.00 - 300.00;
        result = getRemaining(300.00);
        assertEquals(result, expected);
    }
    
    public void testWouldExceedBudget() {
        double totalSpent = new Double(300.00);
        double newAmount = new Double(150.00);
        result = wouldExceedBudget(totalSpent, newAmount);
        
        assertTrue(result);
    }
    
    public void testWouldExceedBudgetFalse() {
        double totalSpent = new Double(300.00);
        double newAmount = new Double(10.00);
        result = wouldExceedBudget(totalSpent, newAmount);
        
        assertFalse(result);
    }
    
    public void testWouldExceedBudgetFalseBoundary() {
        double totalSpent = new Double(300.00);
        double newAmount = new Double(100.00);
        result = wouldExceedBudget(totalSpent, newAmount);
        
        assertFalse(result);
    }
    
}


