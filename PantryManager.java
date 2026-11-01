import java.util.ArrayList;
import java.util.List;

public class PantryManager {

    // Stores all pantry items
    private ArrayList<PantryItem> pantryItems;

    // Constructor
    public PantryManager() {
        pantryItems = new ArrayList<>();
    }

    // Add a new item
    public void addItem(PantryItem item) {

        if (item == null) {
            throw new IllegalArgumentException("Item cannot be empty.");
        }

        if (searchById(item.getId()) != null) {
            throw new IllegalArgumentException("Item ID already exists.");
        }

        pantryItems.add(item);
    }

    // View all pantry items
    public ArrayList<PantryItem> getAllItems() {
        return new ArrayList<>(pantryItems);
    }

    // Search item using ID
    public PantryItem searchById(int id) {

        for (PantryItem item : pantryItems) {

            if (item.getId() == id) {
                return item;
            }
        }

        return null;
    }

    // Search items using name
    public ArrayList<PantryItem> searchByName(String name) {

        ArrayList<PantryItem> results = new ArrayList<>();

        for (PantryItem item : pantryItems) {

            if (item.getName().toLowerCase()
                    .contains(name.toLowerCase())) {

                results.add(item);
            }
        }

        return results;
    }

    // Update an existing item
    public boolean updateItem(int id, PantryItem updatedItem) {

        PantryItem existingItem = searchById(id);

        if (existingItem == null || updatedItem == null) {
            return false;
        }

        if (updatedItem.getId() != id) {
            return false;
        }

        existingItem.setName(updatedItem.getName());
        existingItem.setCategory(updatedItem.getCategory());
        existingItem.setQuantity(updatedItem.getQuantity());
        existingItem.setUnit(updatedItem.getUnit());
        existingItem.setExpiryDate(updatedItem.getExpiryDate());
        existingItem.setMinimumStockLevel(
                updatedItem.getMinimumStockLevel());

        return true;
    }

    // Delete an item
    public boolean deleteItem(int id) {

        PantryItem item = searchById(id);

        if (item == null) {
            return false;
        }

        pantryItems.remove(item);
        return true;
    }

    // Get low-stock items
    public ArrayList<PantryItem> getLowStockItems() {

        ArrayList<PantryItem> results = new ArrayList<>();

        for (PantryItem item : pantryItems) {

            if (item.isLowStock()) {
                results.add(item);
            }
        }

        return results;
    }

    // Get out-of-stock items
    public ArrayList<PantryItem> getOutOfStockItems() {

        ArrayList<PantryItem> results = new ArrayList<>();

        for (PantryItem item : pantryItems) {

            if (item.isOutOfStock()) {
                results.add(item);
            }
        }

        return results;
    }

    // Get expired items
    public ArrayList<PantryItem> getExpiredItems() {

        ArrayList<PantryItem> results = new ArrayList<>();

        for (PantryItem item : pantryItems) {

            if (item.isExpired()) {
                results.add(item);
            }
        }

        return results;
    }

    // Get items expiring soon
    public ArrayList<PantryItem> getExpiringSoonItems() {

        ArrayList<PantryItem> results = new ArrayList<>();

        for (PantryItem item : pantryItems) {

            if (item.isExpiringSoon()) {
                results.add(item);
            }
        }

        return results;
    }

    // Get total number of pantry items
    public int getTotalItems() {
        return pantryItems.size();
    }
}