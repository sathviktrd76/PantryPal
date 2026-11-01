import java.time.LocalDate;

public class PantryItem {

    // Item information
    private int id;
    private String name;
    private String category;
    private double quantity;
    private String unit;
    private LocalDate expiryDate;
    private double minimumStockLevel;

    // Constructor
    public PantryItem(int id, String name, String category,
                      double quantity, String unit,
                      LocalDate expiryDate, double minimumStockLevel) {

        this.id = id;
        this.name = name;
        this.category = category;
        this.quantity = quantity;
        this.unit = unit;
        this.expiryDate = expiryDate;
        this.minimumStockLevel = minimumStockLevel;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public double getQuantity() {
        return quantity;
    }

    public String getUnit() {
        return unit;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public double getMinimumStockLevel() {
        return minimumStockLevel;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public void setMinimumStockLevel(double minimumStockLevel) {
        this.minimumStockLevel = minimumStockLevel;
    }

    // Check whether stock is low
    public boolean isLowStock() {
        return quantity > 0 && quantity <= minimumStockLevel;
    }

    // Check whether item is out of stock
    public boolean isOutOfStock() {
        return quantity <= 0;
    }

    // Check whether item has expired
    public boolean isExpired() {
        return expiryDate.isBefore(LocalDate.now());
    }

    // Check whether item expires within 3 days
    public boolean isExpiringSoon() {

        long daysLeft = java.time.temporal.ChronoUnit.DAYS.between(
            LocalDate.now(), expiryDate);

        return daysLeft >= 0 && daysLeft <= 3;
    }

    // Get item status
    public String getStatus() {

        if (isExpired()) {
            return "Expired";
        }

        if (isOutOfStock()) {
            return "Out of Stock";
        }

        if (isExpiringSoon()) {
            return "Expiring Soon";
        }

        if (isLowStock()) {
            return "Low Stock";
        }

        return "In Stock";
    }
}