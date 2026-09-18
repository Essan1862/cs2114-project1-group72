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
    public static double validateAmount(String input)
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
     * @param input Raw string entered by the user
     * @return Matching Category enum value
     * @throws InvalidInputException if match fails or input is blank
     */
    public static Category validateCategory(String input)
        throws InvalidInputException
    {
        if (input == null || input.trim().isEmpty()) {
            throw new InvalidInputException("Please enter a valid category.")
        }
        
        String target = input.trim.toUpperCase();
        
        for (Category category : Category.values()) {
            if (category.name().equals(target)) {
                return category;
            }
        }
        
        throw new InvalidInputException("Unknown category: " + input + 
            "Please enter a valid category: " + getAllowedCategories());
    }


    public static LocalDate validateDate(String input)
        throws InvalidInputException
    {
        if (input == null || input.trim(.isEmpty())) {
            throw new InvalidInputException("Date can not be left empty.")
        }
        
        tring trimmed = input.trim();

        // Try standard YYYY-MM-DD format
        try {
            return LocalDate.parse(trimmed, ISO_FORMATTER);
        } catch (DateTimeParseException ignored) {
            // Fall through to secondary format attempt
        }

        // Try standard M/D/YYYY or MM/DD/YYYY format
        try {
            return LocalDate.parse(trimmed, SLASH_FORMATTER);
        } catch (DateTimeParseException ignored) {
            // Fall through to error
        }

        throw new InvalidInputException("Invalid date format: '" + input + "'. "
            + "Please use YYYY-MM-DD (e.g., 2026-04-15) or MM/DD/YYYY.");
    }

}

}
