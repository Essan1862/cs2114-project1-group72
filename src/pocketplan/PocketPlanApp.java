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
            throw new IllegalArgumentException("Scanner and ExpenseTracker must not be null.");
        }

        this.scanner = scanner;
        this.tracker = tracker;
    }


    public void run() {
        boolean exit = false;
        while (!exit) {
            printMenu();
            int choice;
            try {
                choice = InputValidator.validateMenuChoice(scanner.nextLine(), 1, 8);
            } catch (InvalidInputException e) {
                System.out.println(e.getMessage());
                continue;
            }
            switch (choice) {
                case 1 -> promptForIncomeAndBudget();
                case 2 -> handleAddExpense();
                case 3 -> handleEditExpense();
                case 4 -> handleDeleteExpense();
                case 5 -> displayExpenseList();
                case 6 -> displaySummary();
                case 7 -> displayCategoryAndHabitualReport();
                case 8 -> exit = true;
            }
        }
        System.out.println("Goodbye.");
    }

    private void printMenu() {
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

        private void promptForIncomeAndBudget() {
        while (true) {
            try {
                System.out.print("Enter monthly income: ");
                double income = InputValidator.validatePositiveAmount(scanner.nextLine(), "income");
                System.out.print("Enter total budget: ");
                double totalBudget = InputValidator.validatePositiveAmount(scanner.nextLine(), "budget");
                tracker.setBudget(income, totalBudget);
                System.out.println("Budget set.");
                return;
            } catch (InvalidInputException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void handleAddExpense() {
        if (!tracker.hasBudget()) {
            System.out.println("Enter income and budget first.");
            return;
        }
        try {
            System.out.print("Amount: ");
            double amount = InputValidator.validatePositiveAmount(scanner.nextLine(), "amount");
            System.out.print("Category " + java.util.Arrays.toString(Category.values()) + ": ");
            Category category = InputValidator.validateCategory(scanner.nextLine());
            System.out.print("Description: ");
            String description = InputValidator.validateDescription(scanner.nextLine());
            System.out.print("Date (YYYY-MM-DD): ");
            LocalDate date = InputValidator.validateDate(scanner.nextLine());
            System.out.print("Habitual? (yes/no): ");
            boolean habitual = InputValidator.validateHabitualTag(scanner.nextLine());
            Expense added = tracker.addExpense(amount, category, description, date, habitual);
            System.out.println("Added expense #" + added.getId() + ".");
        } catch (InvalidInputException e) {
            System.out.println(e.getMessage());
        }
    }

    private void handleEditExpense() {
        if (tracker.getExpenses().isEmpty()) {
            System.out.println("No expenses available.");
            return;
        }
        displayExpenseList();
        try {
            System.out.print("Enter the ID of the expense to edit: ");
            int id = InputValidator.validateExpenseId(scanner.nextLine());
            System.out.print("New amount: ");
            double amount = InputValidator.validatePositiveAmount(scanner.nextLine(), "amount");
            System.out.print("New category " + java.util.Arrays.toString(Category.values()) + ": ");
            Category category = InputValidator.validateCategory(scanner.nextLine());
            System.out.print("New description: ");
            String description = InputValidator.validateDescription(scanner.nextLine());
            System.out.print("New date (YYYY-MM-DD): ");
            LocalDate date = InputValidator.validateDate(scanner.nextLine());
            System.out.print("Habitual? (yes/no): ");
            boolean habitual = InputValidator.validateHabitualTag(scanner.nextLine());
            tracker.editExpense(id, amount, category, description, date, habitual);
            System.out.println("Expense #" + id + " updated.");
        } catch (InvalidInputException e) {
            System.out.println(e.getMessage());
        }
    }

    private void handleDeleteExpense() {
        if (tracker.getExpenses().isEmpty()) {
            System.out.println("No expenses available.");
            return;
        }
        displayExpenseList();
        try {
            System.out.print("Enter the ID of the expense to delete: ");
            int id = InputValidator.validateExpenseId(scanner.nextLine());
            tracker.deleteExpense(id);
            System.out.println("Expense #" + id + " deleted.");
        } catch (InvalidInputException e) {
            System.out.println(e.getMessage());
        }
    }

    private void displayExpenseList() {
        List<Expense> expenses = tracker.getExpenses();
        if (expenses.isEmpty()) {
            System.out.println("No expenses recorded yet.");
            return;
        }
        System.out.println();
        for (Expense e : expenses) {
            System.out.printf("#%d | $%.2f | %s | %s | %s | %s%n",
                    e.getId(), e.getAmount(), e.getCategory(), e.getDescription(),
                    e.getDate(), e.isHabitual() ? "habitual" : "non-habitual");
        }
    }

    private void displaySummary() {
        if (!tracker.hasBudget()) {
            System.out.println("Enter income and budget first.");
            return;
        }
        Budget budget = tracker.getBudget();
        System.out.println();
        System.out.printf("Monthly income: $%.2f%n", budget.getMonthlyIncome());
        System.out.printf("Total budget:   $%.2f%n", budget.getTotalBudget());
        System.out.printf("Total spent:    $%.2f%n", tracker.getTotalSpent());
        System.out.printf("Remaining:      $%.2f%n", tracker.getRemaining());
    }

    private void displayCategoryAndHabitualReport() {
        if (!tracker.hasBudget()) {
            System.out.println("Enter income and budget first.");
            return;
        }
        Map<Category, Double> breakdown = tracker.getCategoryBreakdown();
        System.out.println();
        if (breakdown.isEmpty()) {
            System.out.println("No expenses recorded yet.");
            return;
        }
        System.out.println("Category breakdown:");
        breakdown.forEach((category, total) -> System.out.printf("  %-14s $%.2f%n", category, total));

        Optional<Category> highest = tracker.getHighestSpendingCategory();
        highest.ifPresent(category -> System.out.println("Highest spending category: " + category));

        System.out.printf("Habitual total:     $%.2f%n", tracker.getHabitualTotal());
        System.out.printf("Non-habitual total: $%.2f%n", tracker.getNonHabitualTotal());
    }

    private void handleNewBudget() 
    {
        List<Expense> oldExpenses = tracker.resetForNewMonth(income, totalBudget);
        if (!oldExpenses.isEmpty()) 
        {
            String periodLabel = LocalDate.now().toString(); // or ask the user to name the month
            archiveStorage.archiveExpenses(ARCHIVE_FILEPATH, oldExpenses, periodLabel);
        }
    }
}
