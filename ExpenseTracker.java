import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Manages the collection of expenses and checks operations against the budget.
 * This class is the only one allowed to modify the expense list.
 * 
 * @author Wenxin Zhang
 * @version 2026.9.17
 */
public class ExpenseTracker
{
    private List<Expense> expenses;
    private Budget budget;
    private int nextId;

    /**
     * Initializes an empty tracker with no budget and ID starting at 1.
     */
    public ExpenseTracker()
    {
        this.expenses = new ArrayList<>();
        this.budget = null;
        this.nextId = 1;
    }


    public void setBudget(double income, double totalBudget)
    {
        this.budget = new Budget(income, totalBudget);
    }


    public boolean hasBudget()
    {
        return this.budget != null;
    }


    public Budget getBudget()
    {
        if (!hasBudget())
        {
            throw new IllegalStateException("Budget has not been set yet.");
        }
        return this.budget;
    }


    /**
     * Adds a new expense if it does not exceed the budget.
     */
    public Expense addExpense(
        double amount,
        Category category,
        String description,
        LocalDate date,
        boolean habitual)
        throws InvalidInputException
    {

        if (!hasBudget())
        {
            throw new InvalidInputException(
                "Cannot add expense: Budget is not set.");
        }

        if (budget.wouldExceedBudget(getTotalSpent(), amount))
        {
            throw new InvalidInputException(
                "Adding this expense would exceed the total budget.");
        }

        Expense newExpense =
            new Expense(nextId, amount, category, description, date, habitual);
        expenses.add(newExpense);
        nextId++;
        return newExpense;
    }


    /**
     * Edits an existing expense atomically, ensuring the new amount doesn't
     * break the budget.
     */
    public Expense editExpense(
        int id,
        double amount,
        Category category,
        String description,
        LocalDate date,
        boolean habitual)
        throws InvalidInputException
    {

        Expense expense = findExpenseById(id);

        // Calculate what the total spent WOULD be if we replace the old amount
        // with the new amount
        double spentWithoutThisExpense = getTotalSpent() - expense.getAmount();

        if (budget.wouldExceedBudget(spentWithoutThisExpense, amount))
        {
            throw new InvalidInputException(
                "Editing this expense would exceed the total budget.");
        }

        expense.update(amount, category, description, date, habitual);
        return expense;
    }


    /**
     * Deletes an expense by its ID.
     */
    public Expense deleteExpense(int id)
        throws InvalidInputException
    {
        if (expenses.isEmpty())
        {
            throw new InvalidInputException("No expenses available to delete.");
        }

        Expense expense = findExpenseById(id);
        expenses.remove(expense);
        return expense;
    }


    /**
     * Returns an unmodifiable snapshot of the expenses.
     */
    public List<Expense> getExpenses()
    {
        return Collections.unmodifiableList(expenses);
    }


    public double getTotalSpent()
    {
        double total = 0.0;
        for (Expense e : expenses)
        {
            total += e.getAmount();
        }
        return total;
    }


    public double getRemaining()
    {
        return getBudget().getRemaining(getTotalSpent());
    }


    public Map<Category, Double> getCategoryBreakdown()
    {
        Map<Category, Double> breakdown = new EnumMap<>(Category.class);
        for (Category cat : Category.values())
        {
            breakdown.put(cat, 0.0);
        }

        for (Expense e : expenses)
        {
            double current = breakdown.get(e.getCategory());
            breakdown.put(e.getCategory(), current + e.getAmount());
        }
        return breakdown;
    }


    public Optional<Category> getHighestSpendingCategory()
    {
        if (expenses.isEmpty())
        {
            return Optional.empty();
        }

        Map<Category, Double> breakdown = getCategoryBreakdown();
        Category highestCat = null;
        double maxSpent = -1.0;

        for (Map.Entry<Category, Double> entry : breakdown.entrySet())
        {
            if (entry.getValue() > maxSpent)
            {
                maxSpent = entry.getValue();
                highestCat = entry.getKey();
            }
        }

        return maxSpent > 0 ? Optional.of(highestCat) : Optional.empty();
    }


    public double getHabitualTotal()
    {
        double total = 0.0;
        for (Expense e : expenses)
        {
            if (e.isHabitual())
            {
                total += e.getAmount();
            }
        }
        return total;
    }


    public double getNonHabitualTotal()
    {
        double total = 0.0;
        for (Expense e : expenses)
        {
            if (!e.isHabitual())
            {
                total += e.getAmount();
            }
        }
        return total;
    }


    /**
     * Private helper to locate an expense or throw an error.
     */
    private Expense findExpenseById(int id)
        throws InvalidInputException
    {
        for (Expense e : expenses)
        {
            if (e.getId() == id)
            {
                return e;
            }
        }
        throw new InvalidInputException("Expense ID not found.");
    }
}
