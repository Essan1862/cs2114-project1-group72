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

    /**
     * Creates a budget for one month.
     *
     * @param income
     *            the monthly income, must be greater than zero
     * @param totalBudget
     *            the spending limit, must be greater than zero and no
     *            larger than the income
     * @throws InvalidInputException
     *            if either value is not positive, or the budget is larger
     *            than the income
     */
    public Budget(double income, double totalBudget)
        throws InvalidInputException
    {
        if (income <= 0)
        {
            throw new InvalidInputException(
                "Monthly income must be greater than zero.");
        }
        if (totalBudget <= 0)
        {
            throw new InvalidInputException(
                "Total budget must be greater than zero.");
        }
        if (totalBudget > income)
        {
            throw new InvalidInputException(
                "Budget cannot exceed monthly income.");
        }
        this.monthlyIncome = income;
        this.totalBudget = totalBudget;
    }


    /**
     * Gets the monthly income stored in this budget.
     *
     * @return the monthly income
     */
    public double getMonthlyIncome()
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
        if (totalSpent < 0)
        {
            throw new IllegalArgumentException(
                "Amount spent cannot be negative.");
        }
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
        if (totalSpent < 0 || newAmount < 0)
        {
            throw new IllegalArgumentException(
                "Amounts cannot be negative.");
        }
        if ((totalSpent + newAmount) > totalBudget)
        {
            return true;
        }
        return false;
    }
}
