package pocketplan;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Represents one expense in PocketPlan. An expense records how much was spent,
 * what category it belongs to, a short description, the date it happened, and
 * whether it is habitual spending. An Expense checks its own values, so an
 * object of this class is never in an invalid state.
 *
 * @author Angelo Pinillos-Sternberg
 * @version 2026.09.16
 */
public class Expense
{
    private final int id;
    private double amount;
    private Category category;
    private String description;
    private LocalDate date;
    private boolean habitual;

    /**
     * Creates a new expense.
     *
     * @param id
     *            the unique number for this expense, assigned by
     *            ExpenseTracker, must be greater than zero
     * @param amount
     *            how much was spent, must be greater than zero
     * @param category
     *            the category this expense belongs to
     * @param description
     *            a short description, cannot be blank
     * @param date
     *            the date the money was spent
     * @param habitual
     *            true if this is habitual spending, false if not
     * @throws IllegalArgumentException
     *             if any value is missing or out of range
     */
    public Expense(
        int id,
        double amount,
        Category category,
        String description,
        LocalDate date,
        boolean habitual)
    {
        if (id <= 0)
        {
            throw new IllegalArgumentException(
                "Id must be greater than zero.");
        }
        this.id = id;
        update(amount, category, description, date, habitual);
    }


    /**
     * Replaces every editable field at once. The id never changes. If any value
     * is invalid nothing is changed, so a failed edit leaves the expense
     * exactly as it was.
     *
     * @param newAmount
     *            how much was spent, must be greater than zero
     * @param newCategory
     *            the category this expense belongs to
     * @param newDescription
     *            a short description, cannot be blank
     * @param newDate
     *            the date the money was spent
     * @param newHabitual
     *            true if this is habitual spending, false if not
     * @throws IllegalArgumentException
     *             if any value is missing or out of range
     */
    public void update(
        double newAmount,
        Category newCategory,
        String newDescription,
        LocalDate newDate,
        boolean newHabitual)
    {
        checkAmount(newAmount);
        checkCategory(newCategory);
        checkDescription(newDescription);
        checkDate(newDate);

        this.amount = newAmount;
        this.category = newCategory;
        this.description = newDescription.trim();
        this.date = newDate;
        this.habitual = newHabitual;
    }


    /**
     * Gets the unique number for this expense.
     *
     * @return the id
     */
    public int getId()
    {
        return id;
    }


    /**
     * Gets how much was spent.
     *
     * @return the amount
     */
    public double getAmount()
    {
        return amount;
    }


    /**
     * Gets the category this expense belongs to.
     *
     * @return the category
     */
    public Category getCategory()
    {
        return category;
    }


    /**
     * Gets the short description of this expense.
     *
     * @return the description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Gets the date the money was spent.
     *
     * @return the date
     */
    public LocalDate getDate()
    {
        return date;
    }


    /**
     * Tells whether this expense is habitual spending.
     *
     * @return true if habitual, false if not
     */
    public boolean isHabitual()
    {
        return habitual;
    }


    /**
     * Builds a line describing this expense for the console.
     *
     * @return the expense as text
     */
    public String toString()
    {
        String tag;
        if (habitual)
        {
            tag = "habitual";
        }
        else
        {
            tag = "non-habitual";
        }
        return String.format(
            "#%d  %s  $%.2f  %s  %s  (%s)",
            id,
            date,
            amount,
            category,
            description,
            tag);
    }


    /**
     * Compares this expense to another object. Two expenses are equal when
     * every field matches.
     *
     * @param other
     *            the object to compare against
     * @return true if the object is an Expense with the same values
     */
    public boolean equals(Object other)
    {
        if (this == other)
        {
            return true;
        }
        if (other == null || other.getClass() != this.getClass())
        {
            return false;
        }
        Expense that = (Expense)other;
        return this.id == that.id
            && Double.compare(this.amount, that.amount) == 0
            && this.habitual == that.habitual
            && this.category == that.category
            && this.description.equals(that.description)
            && this.date.equals(that.date);
    }


    /**
     * Builds a hash code so equal expenses hash the same way.
     *
     * @return the hash code for this expense
     */
    public int hashCode()
    {
        return Objects.hash(id, amount, category, description, date, habitual);
    }


    private void checkAmount(double value)
    {
        if (Double.isNaN(value) || Double.isInfinite(value))
        {
            throw new IllegalArgumentException("Amount must be a number.");
        }
        if (value <= 0)
        {
            throw new IllegalArgumentException(
                "Amount must be greater than zero.");
        }
    }


    private void checkCategory(Category value)
    {
        if (value == null)
        {
            throw new IllegalArgumentException("Category cannot be empty.");
        }
    }


    private void checkDescription(String value)
    {
        if (value == null || value.trim().isEmpty())
        {
            throw new IllegalArgumentException("Description cannot be blank.");
        }
    }


    private void checkDate(LocalDate value)
    {
        if (value == null)
        {
            throw new IllegalArgumentException("Date cannot be empty.");
        }
    }
}
