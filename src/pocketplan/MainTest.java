package pocketplan;

import student.TestCase;

public class MainTest extends TestCase {

    public void testMainExitImmediately() {
        systemIn("8\n");

        Main.main(new String[] {});

        String output = systemOut().getHistory();

        assertTrue(output.contains("PocketPlan"));
        assertTrue(output.contains("Goodbye."));
    }

    public void testMainEmptyInputDoesNotCrash() {
        systemIn("");
        boolean crashed = false;

        try {
            Main.main(new String[] {});
        }
        catch (Exception e) {
            crashed = true;
        }

        assertFalse(crashed);
    }
}
