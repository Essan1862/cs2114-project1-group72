package pocketplan;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

public class PocketPlanApp
{
    private final Scanner scanner;
    private final ExpenseTracker tracker;

    public PocketPlanApp(Scanner scanner, ExpenseTracker tracker)
    {
        if (scanner == null || tracker == null)
        {
            throw new IllegalArgumentException(
                "Scanner and ExpenseTracker must not be null.");
        }

        this.scanner = scanner;
        this.tracker = tracker;
    }


    public void run()
    {
        boolean exit = false;

        while (!exit)
        {
            printMenu();

            // Check whether there is actually input available
            if (!scanner.hasNextLine())
            {
                break;
            }

            int choice;

            try
            {
                choice =
                    InputValidator.validateMenuChoice(scanner.nextLine(), 1, 8);
            }
            catch (InvalidInputException e)
            {
                System.out.println(e.getMessage());
                continue;
            }

            switch (choice)
            {
                case 1:
                    promptForIncomeAndBudget();
                    break;
                case 2:
                    handleAddExpense();
                    break;
                case 3:
                    handleEditExpense();
                    break;
                case 4:
                    handleDeleteExpense();
                    break;
                case 5:
                    displayExpenseList();
                    break;
                case 6:
                    displaySummary();
                    break;
                case 7:
                    displayCategoryAndHabitualReport();
                    break;
                case 8:
                    exit = true;
                    break;
            }
        }
        System.out.println("Thank you for budgeting with us.");
    }


    private void printMenu()
    {
        System.out.println();
        System.out.println("=== PocketPlan ===");
        System.out.println("1. Set income and budget");
        System.out.println("2. Add expense");
        System.out.println("3. Edit expense");
        System.out.println("4. Delete expense");
        System.out.println("5. View expenses");
        System.out.println("6. View spending summary");
        System.out.println("7. View category and habitual reports");
        System.out.println("8. Exit");
        System.out.print("Choose an option: ");
    }


    private void promptForIncomeAndBudget()
    {
        double income = askForAmount("Enter monthly income: ", "income");
        while (true)
        {
            double totalBudget = askForAmount("Enter total budget: ",
                "budget");
            try
            {
                tracker.setBudget(income, totalBudget);
                System.out.println("Budget set.");
                return;
            }
            catch (InvalidInputException e)
            {
                System.out.println(e.getMessage());
            }
        }
    }


    /**
     * Keeps asking for an amount until the user types a valid one.
     *
     * @param label
     *            the prompt shown to the user
     * @param fieldName
     *            the field name used in the error message
     * @return the amount the user entered
     */
    private double askForAmount(String label, String fieldName)
    {
        while (true)
        {
            System.out.print(label);
            try
            {
                return InputValidator
                    .validatePositiveAmount(scanner.nextLine(), fieldName);
            }
            catch (InvalidInputException e)
            {
                System.out.println(e.getMessage());
            }
        }
    }


    /**
     * Keeps asking for a category until the user types a valid one.
     *
     * @param label
     *            the prompt shown to the user
     * @return the category the user chose
     */
    private Category askForCategory(String label)
    {
        while (true)
        {
            System.out.print(label);
            try
            {
                return InputValidator.validateCategory(scanner.nextLine());
            }
            catch (InvalidInputException e)
            {
                System.out.println(e.getMessage());
            }
        }
    }


    /**
     * Keeps asking for a description until the user types a valid one.
     *
     * @param label
     *            the prompt shown to the user
     * @return the description the user entered
     */
    private String askForDescription(String label)
    {
        while (true)
        {
            System.out.print(label);
            try
            {
                return InputValidator.validateDescription(scanner.nextLine());
            }
            catch (InvalidInputException e)
            {
                System.out.println(e.getMessage());
            }
        }
    }


    /**
     * Keeps asking for a date until the user types a valid one.
     *
     * @param label
     *            the prompt shown to the user
     * @return the date the user entered
     */
    private LocalDate askForDate(String label)
    {
        while (true)
        {
            System.out.print(label);
            try
            {
                return InputValidator.validateDate(scanner.nextLine());
            }
            catch (InvalidInputException e)
            {
                System.out.println(e.getMessage());
            }
        }
    }


    /**
     * Keeps asking for the habitual tag until the user types a valid one.
     *
     * @param label
     *            the prompt shown to the user
     * @return true if the expense is habitual, false if not
     */
    private boolean askForHabitual(String label)
    {
        while (true)
        {
            System.out.print(label);
            try
            {
                return InputValidator.validateHabitualTag(scanner.nextLine());
            }
            catch (InvalidInputException e)
            {
                System.out.println(e.getMessage());
            }
        }
    }


    /**
     * Keeps asking for an expense id until the user types a valid number.
     *
     * @param label
     *            the prompt shown to the user
     * @return the id the user entered
     */
    private int askForExpenseId(String label)
    {
        while (true)
        {
            System.out.print(label);
            try
            {
                return InputValidator.validateExpenseId(scanner.nextLine());
            }
            catch (InvalidInputException e)
            {
                System.out.println(e.getMessage());
            }
        }
    }


    private void handleAddExpense()
    {
        if (!tracker.hasBudget())
        {
            System.out.println("Enter income and budget first.");
            return;
        }
        double amount = askForAmount("Amount: ", "amount");
        Category category = askForCategory(
            "Category " + java.util.Arrays.toString(Category.values()) + ": ");
        String description = askForDescription("Description: ");
        LocalDate date = askForDate("Date (YYYY-MM-DD): ");
        boolean habitual = askForHabitual("Habitual? (yes/no): ");

        try
        {
            Expense added = tracker
                .addExpense(amount, category, description, date, habitual);
            System.out.println("Added expense #" + added.getId() + ".");
        }
        catch (InvalidInputException e)
        {
            System.out.println(e.getMessage());
        }
    }


    private void handleEditExpense()
    {
        if (tracker.getExpenses().isEmpty())
        {
            System.out.println("No expenses available.");
            return;
        }
        displayExpenseList();
        int id = askForExpenseId("Enter the ID of the expense to edit: ");
        double amount = askForAmount("New amount: ", "amount");
        Category category = askForCategory("New category "
            + java.util.Arrays.toString(Category.values()) + ": ");
        String description = askForDescription("New description: ");
        LocalDate date = askForDate("New date (YYYY-MM-DD): ");
        boolean habitual = askForHabitual("Habitual? (yes/no): ");

        try
        {
            tracker
                .editExpense(id, amount, category, description, date, habitual);
            System.out.println("Expense #" + id + " updated.");
        }
        catch (InvalidInputException e)
        {
            System.out.println(e.getMessage());
        }
    }


    private void handleDeleteExpense()
    {
        if (tracker.getExpenses().isEmpty())
        {
            System.out.println("No expenses available.");
            return;
        }
        displayExpenseList();
        try
        {
            System.out.print("Enter the ID of the expense to delete: ");
            int id = InputValidator.validateExpenseId(scanner.nextLine());
            tracker.deleteExpense(id);
            System.out.println("Expense #" + id + " deleted.");
        }
        catch (InvalidInputException e)
        {
            System.out.println(e.getMessage());
        }
    }


    private void displayExpenseList()
    {
        List<Expense> expenses = tracker.getExpenses();
        if (expenses.isEmpty())
        {
            System.out.println("No expenses recorded yet.");
            return;
        }
        System.out.println();
        for (Expense e : expenses)
        {
            System.out.printf(
                "#%d | $%.2f | %s | %s | %s | %s%n",
                e.getId(),
                e.getAmount(),
                e.getCategory(),
                e.getDescription(),
                e.getDate(),
                e.isHabitual() ? "habitual" : "non-habitual");
        }
    }


    private void displaySummary()
    {
        if (!tracker.hasBudget())
        {
            System.out.println("Enter income and budget first.");
            return;
        }
        Budget budget = tracker.getBudget();
        System.out.println();
        System.out.printf("Monthly income: $%.2f%n", budget.getIncome());
        System.out.printf("Total budget:   $%.2f%n", budget.getTotalBudget());
        System.out.printf("Total spent:    $%.2f%n", tracker.getTotalSpent());
        System.out.printf("Remaining:      $%.2f%n", tracker.getRemaining());
    }


    private void displayCategoryAndHabitualReport()
    {
        if (!tracker.hasBudget())
        {
            System.out.println("Enter income and budget first.");
            return;
        }
        Map<Category, Double> breakdown = tracker.getCategoryBreakdown();
        System.out.println();
        if (breakdown.isEmpty())
        {
            System.out.println("No expenses recorded yet.");
            return;
        }
        System.out.println("Category breakdown:");
        breakdown.forEach(
            (category, total) -> System.out
                .printf("  %-14s $%.2f%n", category, total));

        Optional<Category> highest = tracker.getHighestSpendingCategory();
        highest.ifPresent(
            category -> System.out
                .println("Highest spending category: " + category));

        System.out
            .printf("Habitual total:     $%.2f%n", tracker.getHabitualTotal());
        System.out.printf(
            "Non-habitual total: $%.2f%n",
            tracker.getNonHabitualTotal());
    }


    private void handleNewBudget()
    {
        try
        {
            System.out.print("Enter monthly income: ");
            double income = InputValidator
                .validatePositiveAmount(scanner.nextLine(), "income");
            System.out.print("Enter total budget: ");
            double totalBudget = InputValidator
                .validatePositiveAmount(scanner.nextLine(), "budget");
            tracker.resetForNewMonth(income, totalBudget);
            System.out
                .println("Budget reset. Previous expenses have been cleared.");
        }
        catch (InvalidInputException e)
        {
            System.out.println(e.getMessage());
        }
    }
}
