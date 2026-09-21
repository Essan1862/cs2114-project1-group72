package pocketplan;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import student.TestCase;

/**
 * Tests for Main.
 *
 * @author Essan Salem
 * @version 2026.09.20
 */
public class MainTest
    extends TestCase
{

    public void testMainExitImmediately()
    {
        setSystemIn("8\n");

        String output =
            capturePrintedOutput(() -> Main.main(new String[] {}));

        assertTrue(output.contains("PocketPlan"));
        assertTrue(output.contains("Thank you for budgeting with us."));
    }


    public void testMainEmptyInputDoesNotCrash()
    {
        setSystemIn("");
        boolean crashed = false;

        try
        {
            Main.main(new String[] {});
        }
        catch (Exception e)
        {
            crashed = true;
        }

        assertFalse(crashed);
    }


    /**
     * Records everything the program prints while the given code runs. The
     * recorder built into the test library does not clear itself between
     * tests, so the output is captured here instead.
     *
     * @param action
     *            the code to run while output is being recorded
     * @return everything that was printed
     */
    private String capturePrintedOutput(Runnable action)
    {
        PrintStream realOut = System.out;
        ByteArrayOutputStream recorded = new ByteArrayOutputStream();
        System.setOut(new PrintStream(recorded));
        try
        {
            action.run();
        }
        finally
        {
            System.setOut(realOut);
        }
        return recorded.toString();
    }
}
