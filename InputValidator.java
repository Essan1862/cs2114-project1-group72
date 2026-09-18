import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Utility class to convert raw text into valid values for PocketPlan. Throws
 * InvalidInputException with clean user-facing error messages on failure.
 */
public class InputValidator
{

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


    public static Category validateCategory(String input)
        throws InvalidInputException
    {
        if (input == null || input.trim().isEmpty()) {
            throw new InvalidInputException("Please enter a valid category.")
        }
        
        
    }
}
