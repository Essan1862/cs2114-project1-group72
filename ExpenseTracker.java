package pocketplan;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

/**
 * Manages the collection of expenses and checks operations against the budget.
 * Handles persisting data to and loading from a CSV file.
 * 
 * @author Wenxin Zhang
 * @version 2026.09.18
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
        throws InvalidInputException
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
     * Edits an existing expense atomically.
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
        if (!hasBudget())
        {
            throw new InvalidInputException(
                "Cannot edit expense: Budget is not set.");
        }

        Expense expense = findExpenseById(id);
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
     * Saves all current expenses to a CSV file.
     * 
     * @param filename
     *            the name of the file to save to
     * @throws FileNotFoundException
     *             if the file cannot be created
     */
    public void saveToCSV(String filename)
        throws FileNotFoundException
    {
        try (PrintWriter writer = new PrintWriter(new File(filename)))
        {
            for (Expense e : expenses)
            {
                String safeDescription = e.getDescription().replace(",", " ");
                writer.println(
                    e.getId() + "," + e.getAmount() + ","
                        + e.getCategory().name() + "," + safeDescription + ","
                        + e.getDate().toString() + "," + e.isHabitual());
            }
        }
    }


    /**
     * Loads expenses from a CSV file and restores nextId.
     * 
     * @param filename
     *            the name of the file to read from
     * @throws FileNotFoundException
     *             if the file does not exist
     */
    public void loadFromCSV(String filename)
        throws FileNotFoundException
    {
        expenses.clear();
        int maxId = 0;

        try (Scanner scanner = new Scanner(new File(filename)))
        {
            while (scanner.hasNextLine())
            {
                String line = scanner.nextLine().trim();
                if (line.isEmpty())
                    continue;

                String[] parts = line.split(",");
                if (parts.length == 6)
                {
                    int id = Integer.parseInt(parts[0]);
                    double amount = Double.parseDouble(parts[1]);
                    Category category = Category.valueOf(parts[2]);
                    String description = parts[3];
                    LocalDate date = LocalDate.parse(parts[4]);
                    boolean habitual = Boolean.parseBoolean(parts[5]);

                    Expense e = new Expense(
                        id,
                        amount,
                        category,
                        description,
                        date,
                        habitual);
                    expenses.add(e);

                    if (id > maxId)
                    {
                        maxId = id;
                    }
                }
            }
        }
        this.nextId = maxId + 1;
    }


    /**
     * Saves the current budget (income and total budget) to a CSV file. Writes
     * nothing if no budget has been set.
     * 
     * @param filename
     *            the name of the file to save to
     * @throws FileNotFoundException
     *             if the file cannot be created
     */
    public void saveBudgetToCSV(String filename)
        throws FileNotFoundException
    {
        try (PrintWriter writer = new PrintWriter(new File(filename)))
        {
            if (hasBudget())
            {
                writer.println(
                    budget.getIncome() + "," + budget.getTotalBudget());
            }
        }
    }


    /**
     * Loads the budget (income and total budget) from a CSV file, if present.
     * 
     * @param filename
     *            the name of the file to read from
     * @throws FileNotFoundException
     *             if the file does not exist
     */
    public void loadBudgetFromCSV(String filename)
        throws FileNotFoundException
    {
        try (Scanner scanner = new Scanner(new File(filename)))
        {
            if (scanner.hasNextLine())
            {
                String line = scanner.nextLine().trim();
                String[] parts = line.split(",");
                if (parts.length == 2)
                {
                    double income = Double.parseDouble(parts[0]);
                    double totalBudget = Double.parseDouble(parts[1]);
                    try
                    {
                        setBudget(income, totalBudget);
                    }
                    catch (InvalidInputException e)
                    {
                        System.out.println(
                            "Saved budget was invalid and could not be restored: "
                                + e.getMessage());
                    }
                }
            }
        }
    }


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


    public void resetForNewMonth(double income, double totalBudget)
        throws InvalidInputException
    {
        this.expenses.clear();
        this.nextId = 1;
        setBudget(income, totalBudget);
    }
}
