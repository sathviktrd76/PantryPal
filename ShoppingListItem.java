public class ShoppingListItem {

    private int id;
    private String name;
    private double quantity;
    private String unit;
    private boolean purchased;

    // Constructor
    public ShoppingListItem(int id, String name,
                            double quantity, String unit) {

        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.purchased = false;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getQuantity() {
        return quantity;
    }

    public String getUnit() {
        return unit;
    }

    public boolean isPurchased() {
        return purchased;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setPurchased(boolean purchased) {
        this.purchased = purchased;
    }

    // Display shopping list item
    @Override
    public String toString() {
        return name + " - " + quantity + " " + unit;
    }
}