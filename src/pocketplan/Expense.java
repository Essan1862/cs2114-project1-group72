package pocketplan;

import java.time.LocalDate;

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
     *            the unique number for this expense, assigned by ExpenseTracker
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
        if (id < 0)
        {
            throw new IllegalArgumentException("Id cannot be negative.");
        }
        this.id = id;
        update(amount, category, description, date, habitual);
    }


    /**
     * Changes the amount of this expense.
     *
     * @param amount
     *            the new amount, must be greater than zero
     * @throws IllegalArgumentException
     *            if the value is not allowed
     */
    public void setAmount(double amount)
    {
        checkAmount(amount);
        this.amount = amount;
    }


    /**
     * Changes the category of this expense.
     *
     * @param category
     *            the new category, cannot be null
     * @throws IllegalArgumentException
     *            if the value is not allowed
     */
    public void setCategory(Category category)
    {
        checkCategory(category);
        this.category = category;
    }


    /**
     * Changes the description of this expense.
     *
     * @param description
     *            the new description, cannot be blank
     * @throws IllegalArgumentException
     *            if the value is not allowed
     */
    public void setDescription(String description)
    {
        checkDescription(description);
        this.description = description.trim();
    }


    /**
     * Changes the date of this expense.
     *
     * @param date
     *            the new date, cannot be null
     * @throws IllegalArgumentException
     *            if the value is not allowed
     */
    public void setDate(LocalDate date)
    {
        checkDate(date);
        this.date = date;
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


    /**
     * Compares this expense to another object.
     *
     * @param obj
     *            the object to compare against
     * @return true if the object is an Expense with the same values
     */
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }

        if (obj == null || getClass() != obj.getClass())
        {
            return false;
        }

        Expense other = (Expense)obj;

        return id == other.id && amount == other.amount
            && category == other.category
            && description.equals(other.description) && date.equals(other.date)
            && habitual == other.habitual;
    }


    /**
     * Builds a hash code so equal expenses hash the same way.
     *
     * @return the hash code for this expense
     */
    @Override
    public int hashCode()
    {
        int result = Integer.hashCode(id);
        result = 31 * result + Double.hashCode(amount);
        result = 31 * result + category.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + date.hashCode();
        result = 31 * result + Boolean.hashCode(habitual);

        return result;
    }
}
