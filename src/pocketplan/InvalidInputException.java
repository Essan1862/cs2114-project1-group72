package pocketplan;

/**
 * Thrown when input from the user cannot be accepted, such as an amount that is
 * not a positive number, an unknown category, a blank description, or a
 * malformed date. This is a recoverable error. The console loop catches it,
 * prints the message, and re-prompts, so the program keeps running and no data
 * is lost.
 *
 * Programming mistakes, such as a null dependency, use
 * IllegalArgumentException instead. This exception is only for input a user can
 * correct.
 *
 * @author Angelo Pinillos-Sternberg
 * @version 2026.09.16
 */
public class InvalidInputException extends Exception
{
    /**
     * Creates a new exception carrying the message shown to the user. The
     * message should say exactly what to fix, for example
     * "Amount must be a positive number, such as 25.50".
     *
     * @param message
     *            the explanation displayed by the console loop
     */
    public InvalidInputException(String message)
    {
        super(message);
    }
}
