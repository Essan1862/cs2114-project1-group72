package pocketplan;

import student.TestCase;

public class CategoryTest extends TestCase {

    public void testValuesReturnsAllSixCategories() {
        Category[] all = Category.values();

        assertEquals(6, all.length);
    }

    public void testFoodExists() {
        Category category = Category.valueOf("FOOD");

        assertEquals(Category.FOOD, category);
    }

    public void testTransportationExists() {
        Category category = Category.valueOf("TRANSPORTATION");

        assertEquals(Category.TRANSPORTATION, category);
    }

    public void testEntertainmentExists() {
        Category category = Category.valueOf("ENTERTAINMENT");

        assertEquals(Category.ENTERTAINMENT, category);
    }

    public void testHousingExists() {
        Category category = Category.valueOf("HOUSING");

        assertEquals(Category.HOUSING, category);
    }

    public void testUtilitiesExists() {
        Category category = Category.valueOf("UTILITIES");

        assertEquals(Category.UTILITIES, category);
    }

    public void testOtherExists() {
        Category category = Category.valueOf("OTHER");

        assertEquals(Category.OTHER, category);
    }

    public void testValueOfInvalidNameThrowsException() {
        boolean exceptionWasThrown = false;

        try {
            Category category = Category.valueOf("GROCERIES");
        }
        catch (IllegalArgumentException e) {
            exceptionWasThrown = true;
        }

        assertTrue(exceptionWasThrown);
    }
}
