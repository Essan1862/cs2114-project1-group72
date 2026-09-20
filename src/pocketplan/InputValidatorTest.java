package pocketplan;

import student.TestCase;
import java.time.LocalDate;

/**
 * Unit tests for InputValidator using the student.TestCase framework.
 */
public class InputValidatorTest extends TestCase {

    // ==========================================
    // 1. validateAmount Tests
    // ==========================================

    /**
     * Test normal valid inputs for validateAmount.
     */
    public void testValidateAmountValid() {
        try {
            assertEquals(45.75, InputValidator.validateAmount("45.75"), 0.001);
            assertEquals(100.0, InputValidator.validateAmount("100"), 0.001);
            assertEquals(50.0, InputValidator.validateAmount("$50"), 0.001);
            assertEquals(12.34, InputValidator.validateAmount("  12.34  "), 0.001);
        } catch (InvalidInputException e) {
            fail("Valid input threw an unexpected InvalidInputException: " + e.getMessage());
        }
    }

    /**
     * Test zero amount throws an InvalidInputException.
     */
    public void testValidateAmountZero() {
        try {
            InputValidator.validateAmount("0");
            fail("Expected InvalidInputException was not thrown for amount = 0");
        } catch (InvalidInputException e) {
            assertTrue(e.getMessage().contains("greater than 0"));
        }
    }

    /**
     * Test negative amount throws an InvalidInputException.
     */
    public void testValidateAmountNegative() {
        try {
            InputValidator.validateAmount("-50.00");
            fail("Expected InvalidInputException was not thrown for negative amount");
        } catch (InvalidInputException e) {
            assertTrue(e.getMessage().contains("greater than 0"));
        }
    }

    /**
     * Test non-numeric string throws an InvalidInputException.
     */
    public void testValidateAmountNonNumeric() {
        try {
            InputValidator.validateAmount("twenty");
            fail("Expected InvalidInputException was not thrown for non-numeric text");
        } catch (InvalidInputException e) {
            assertTrue(e.getMessage().contains("Invalid numeric amount"));
        }
    }

    /**
     * Test empty and null strings throw InvalidInputException.
     */
    public void testValidateAmountEmptyAndNull() {
        try {
            InputValidator.validateAmount("");
            fail("Expected InvalidInputException for empty string");
        } catch (InvalidInputException e) {
            assertEquals("Amount cannot be empty.", e.getMessage());
        }

        try {
            InputValidator.validateAmount(null);
            fail("Expected InvalidInputException for null input");
        } catch (InvalidInputException e) {
            assertEquals("Amount cannot be empty.", e.getMessage());
        }
    }

    // ==========================================
    // 2. validateCategory Tests
    // ==========================================

    /**
     * Test normal valid category matching (case-insensitive).
     */
    public void testValidateCategoryValid() {
        try {
            assertEquals(Category.FOOD, InputValidator.validateCategory("FOOD"));
            assertEquals(Category.FOOD, InputValidator.validateCategory("food"));
            assertEquals(Category.TRANSPORTATION, InputValidator.validateCategory("Transportation"));
            assertEquals(Category.OTHER, InputValidator.validateCategory("  other  "));
        } catch (InvalidInputException e) {
            fail("Valid category threw an unexpected InvalidInputException");
        }
    }

    /**
     * Test unknown category name throws an InvalidInputException listing options.
     */
    public void testValidateCategoryInvalidName() {
        try {
            InputValidator.validateCategory("shopping");
            fail("Expected InvalidInputException for unknown category 'shopping'");
        } catch (InvalidInputException e) {
            assertTrue(e.getMessage().contains("Unknown category"));
            assertTrue(e.getMessage().contains("Allowed categories"));
        }
    }

    /**
     * Test empty string throws an InvalidInputException.
     */
    public void testValidateCategoryEmpty() {
        try {
            InputValidator.validateCategory("   ");
            fail("Expected InvalidInputException for blank category");
        } catch (InvalidInputException e) {
            assertTrue(e.getMessage().contains("Category cannot be empty"));
        }
    }

    // ==========================================
    // 3. validateDate Tests
    // ==========================================

    /**
     * Test valid dates in both YYYY-MM-DD and MM/DD/YYYY formats.
     */
    public void testValidateDateValid() {
        try {
            LocalDate expected = LocalDate.of(2026, 4, 15);
            assertEquals(expected, InputValidator.validateDate("2026-04-15"));
            assertEquals(expected, InputValidator.validateDate("4/15/2026"));
            assertEquals(expected, InputValidator.validateDate("04/15/2026"));
        } catch (InvalidInputException e) {
            fail("Valid date threw an unexpected InvalidInputException: " + e.getMessage());
        }
    }

    /**
     * Test unparsable and bad calendar dates throw InvalidInputException.
     */
    public void testValidateDateInvalid() {
        // Invalid calendar date (Feb 30th)
        try {
            InputValidator.validateDate("2026-02-30");
            fail("Expected InvalidInputException for non-existent date Feb 30");
        } catch (InvalidInputException e) {
            assertTrue(e.getMessage().contains("Invalid date format"));
        }

        // Nonsense text
        try {
            InputValidator.validateDate("yesterday");
            fail("Expected InvalidInputException for text 'yesterday'");
        } catch (InvalidInputException e) {
            assertTrue(e.getMessage().contains("Invalid date format"));
        }
    }

    // ==========================================
    // 4. validateMenuChoice Tests
    // ==========================================

    /**
     * Test valid choices inside allowed bounds.
     */
    public void testValidateMenuChoiceValid() {
        try {
            assertEquals(1, InputValidator.validateMenuChoice("1", 1, 7));
            assertEquals(4, InputValidator.validateMenuChoice("4", 1, 7));
            assertEquals(7, InputValidator.validateMenuChoice("7", 1, 7));
        } catch (InvalidInputException e) {
            fail("Valid menu choice threw an unexpected InvalidInputException");
        }
    }

    /**
     * Test choices outside the allowed min and max bounds.
     */
    public void testValidateMenuChoiceOutOfRange() {
        // Below min
        try {
            InputValidator.validateMenuChoice("0", 1, 7);
            fail("Expected InvalidInputException for choice 0 below min 1");
        } catch (InvalidInputException e) {
            assertTrue(e.getMessage().contains("Choice out of range"));
        }

        // Above max
        try {
            InputValidator.validateMenuChoice("8", 1, 7);
            fail("Expected InvalidInputException for choice 8 above max 7");
        } catch (InvalidInputException e) {
            assertTrue(e.getMessage().contains("Choice out of range"));
        }
    }

    /**
     * Test non-integer menu input.
     */
    public void testValidateMenuChoiceNonInteger() {
        try {
            InputValidator.validateMenuChoice("abc", 1, 7);
            fail("Expected InvalidInputException for text choice 'abc'");
        } catch (InvalidInputException e) {
            assertTrue(e.getMessage().contains("Please enter a valid whole number"));
        }
    }
}
