package pocketplan;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Utility class to convert raw text into valid values for PocketPlan. Throws
 * InvalidInputException with clean user-facing error messages on failure.
 */
public class InputValidator
{

    // Flexible date parsers for user input
    private static final DateTimeFormatter ISO_FORMATTER =
        DateTimeFormatter.ISO_LOCAL_DATE; // YYYY-MM-DD
    private static final DateTimeFormatter SLASH_FORMATTER =
        DateTimeFormatter.ofPattern("M/d/yyyy"); // MM/DD/YYYY

    // Private constructor to prevent instantiation (Utility class)
    private InputValidator()
    {
    }


    /**
     * Parses a positive double amount. Rejects non-numeric, zero, or negative
     * input.
     *
     * @param input
     *            Raw text string from user console input
     * @return Validated positive double
     * @throws InvalidInputException
     *             if input is blank, non-numeric, zero, or negative
     */
    public static double validatePositiveAmount(String input, String fieldName)
        throws InvalidInputException
    {
        if (input == null || input.trim().isEmpty())
        {
            throw new InvalidInputException("Amount cannot be empty.");
        }

        // Clean out optional currency symbols if entered (e.g. "$50" -> "50")
        String cleaned = input.trim().replace("$", "");

        try
        {
            double value = Double.parseDouble(cleaned);
            if (value <= 0)
            {
                throw new InvalidInputException(
                    "Amount must be greater than 0. Received: " + value);
            }
            return value;
        }
        catch (NumberFormatException e)
        {
            throw new InvalidInputException(
                "Invalid numeric amount: '" + input
                    + "'. Please enter a valid number (e.g., 45.75).");
        }
    }


    /**
     * Compares user input to set of Category options. Throws an exception if
     * category does not exist or the input is null or empty.
     * 
     * @param input
     *            Raw string entered by the user
     * @return Matching Category enum value
     * @throws InvalidInputException
     *             if match fails or input is blank
     */
    public static Category validateCategory(String input)
        throws InvalidInputException
    {
        if (input == null || input.trim().isEmpty())
        {
            throw new InvalidInputException(
                "Category cannot be empty. Please enter a valid category.");
        }

        String target = input.trim().toUpperCase();

        for (Category category : Category.values())
        {
            if (category.name().equals(target))
            {
                return category;
            }
        }

        throw new InvalidInputException(
            "Unknown category: " + input + ". Allowed categories: "
                + getAllowedCategories());
    }


    // Helper method to put categories into a String
    private static String getAllowedCategories()
    {
        StringBuilder sb = new StringBuilder("[");
        Category[] categories = Category.values();
        for (int i = 0; i < categories.length; i++)
        {
            sb.append(categories[i].name());
            if (i < categories.length - 1)
            {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }


    public static LocalDate validateDate(String input)
        throws InvalidInputException
    {
        if (input == null || input.trim().isEmpty())
        {
            throw new InvalidInputException("Date can not be left empty.");
        }

        String trimmed = input.trim();

        // Try standard YYYY-MM-DD format
        try
        {
            return LocalDate.parse(trimmed, ISO_FORMATTER);
        }
        catch (DateTimeParseException ignored)
        {
            // Fall through to secondary format attempt
        }

        // Try standard M/D/YYYY or MM/DD/YYYY format
        try
        {
            return LocalDate.parse(trimmed, SLASH_FORMATTER);
        }
        catch (DateTimeParseException ignored)
        {
            // Fall through to error
        }

        throw new InvalidInputException(
            "Invalid date format: '" + input + "'. "
                + "Please use YYYY-MM-DD (e.g., 2026-04-15) or MM/DD/YYYY.");
    }


    /**
     * Parses an integer menu choice and ensures it falls within [min, max].
     *
     * @param input
     *            Raw choice string from user
     * @param min
     *            Minimum allowed menu integer
     * @param max
     *            Maximum allowed menu integer
     * @return Validated integer selection
     * @throws InvalidInputException
     *             if input is non-numeric or outside [min, max]
     */
    public static int validateMenuChoice(String input, int min, int max)
        throws InvalidInputException
    {
        if (input == null || input.trim().isEmpty())
        {
            throw new InvalidInputException(
                "Selection cannot be empty. Choose between " + min + " and "
                    + max + ".");
        }

        try
        {
            int choice = Integer.parseInt(input.trim());
            if (choice < min || choice > max)
            {
                throw new InvalidInputException(
                    "Choice out of range. Please enter a number between " + min
                        + " and " + max + ".");
            }
            return choice;
        }
        catch (NumberFormatException e)
        {
            throw new InvalidInputException(
                "Invalid selection: '" + input
                    + "'. Please enter a valid whole number.");
        }
    }


    /**
     * Validates description text is non-empty.
     */
    public static String validateDescription(String input)
        throws InvalidInputException
    {
        if (input == null || input.trim().isEmpty())
        {
            throw new InvalidInputException("Description cannot be empty.");
        }
        return input.trim();
    }


    /**
     * Converts "yes"/"no" (or "y"/"n") user input into a boolean.
     */
    public static boolean validateHabitualTag(String input)
        throws InvalidInputException
    {
        if (input == null || input.trim().isEmpty())
        {
            throw new InvalidInputException(
                "Habitual response cannot be empty. Please enter 'yes' or 'no'.");
        }
        String cleaned = input.trim().toLowerCase();
        if (cleaned.equals("yes") || cleaned.equals("y"))
        {
            return true;
        }
        else if (cleaned.equals("no") || cleaned.equals("n"))
        {
            return false;
        }
        throw new InvalidInputException(
            "Invalid input: '" + input + "'. Please enter 'yes' or 'no'.");
    }


    /**
     * Validates that an ID entered for editing or deleting is a positive
     * integer.
     */
    public static int validateExpenseId(String input)
        throws InvalidInputException
    {
        if (input == null || input.trim().isEmpty())
        {
            throw new InvalidInputException("Expense ID cannot be empty.");
        }
        try
        {
            int id = Integer.parseInt(input.trim());
            if (id <= 0)
            {
                throw new InvalidInputException(
                    "Expense ID must be a positive integer.");
            }
            return id;
        }
        catch (NumberFormatException e)
        {
            throw new InvalidInputException(
                "Invalid expense ID: '" + input
                    + "'. Please enter a valid number.");
        }
    }

}
