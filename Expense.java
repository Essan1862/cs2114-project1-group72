public class Expense
{

    private int id;
    private double amount;
    private Category category;
    private String desrciption;
    private LocalDate date;

    public Expense(
        int id,
        double amount,
        Category category,
        String description,
        LocalDate date)
    {
        this.id = id;
        this.amount = amount;
        this.category = category;
        this.description = description;
        this.date = date;
    }

}
