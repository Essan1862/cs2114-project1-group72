package pocketplan;

import student.TestCase;

/**
 * Tests for InvalidInputException.
 *
 * @author Angelo Pinillos-Sternberg
 * @version 2026.09.20
 */
public class InvalidInputExceptionTest
    extends TestCase
{

    public void testGetMessageReturnsTheMessagePassedIn()
    {
        InvalidInputException exception = new InvalidInputException(
            "Amount must be a positive number, such as 25.50");

        assertEquals(
            "Amount must be a positive number, such as 25.50",
            exception.getMessage());
    }


    public void testDifferentMessageIsStoredCorrectly()
    {
        InvalidInputException exception =
            new InvalidInputException("Category is unknown.");

        assertEquals("Category is unknown.", exception.getMessage());
    }


    public void testIsAnInstanceOfException()
    {
        InvalidInputException exception =
            new InvalidInputException("Bad input.");

        assertTrue(exception instanceof Exception);
    }


    public void testCanBeThrownAndCaught()
    {
        boolean caught = false;
        String caughtMessage = null;

        try
        {
            throw new InvalidInputException("Description cannot be blank.");
        }
        catch (InvalidInputException e)
        {
            caught = true;
            caughtMessage = e.getMessage();
        }

        assertTrue(caught);
        assertEquals("Description cannot be blank.", caughtMessage);
    }
}
