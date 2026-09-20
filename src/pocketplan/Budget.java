package pocketplan;

/**
 * // -------------------------------------------------------------------------
 * /** Stores the monthly income and budget added. Creates methods to determine
 * the total amount remaining from the budget and whether a new addition would
 * go over the budget.
 * 
 * @author mckennabryan
 * @version Sep 14, 2026
 */
public class Budget
{
    private double monthlyIncome;
    private double totalBudget;

    public Budget(double income, double totalBudget)
        throws InvalidInputException
    {
        if (totalBudget > income)
        {
            throw new InvalidInputException(
                "Budget cannot exceed monthly income.");
        }
        this.monthlyIncome = income;
        this.totalBudget = totalBudget;
    }


    /**
     * Gets the monthly income from the budget.
     */
    public double getIncome()
    {
        return monthlyIncome;
    }


    /**
     * Gets the total budget that the user creates.
     */
    public double getTotalBudget()
    {
        return totalBudget;
    }


    /**
     * Gets the total amount of money left in the budget by subtracting the
     * total budget by the amount spent
     * 
     * @param double
     *            totalSpent A double representing how much money was spent for
     *            the month
     */
    public double getRemaining(double totalSpent)
    {
        return totalBudget - totalSpent;
    }


    /**
     * A boolean that returns true if a new additional charge would go over the
     * budget set
     * 
     * @return boolean
     */
    public boolean wouldExceedBudget(double totalSpent, double newAmount)
    {
        if ((totalSpent + newAmount) > totalBudget)
        {
            return true;
        }
        return false;
    }
}
