import java.util.ArrayList;

public class ShoppingListManager {

    private ArrayList<ShoppingListItem> shoppingItems;
    private FileManager fileManager;

    public ShoppingListManager() {
        fileManager = new FileManager();
        shoppingItems = fileManager.loadShoppingList();
    }

    // Add an item to the shopping list
    public void addItem(String name, double quantity, String unit) {

        if (name == null || name.trim().isEmpty() || quantity <= 0) {
            System.out.println("Invalid shopping list item.");
            return;
        }

        for (ShoppingListItem item : shoppingItems) {
            if (item.getName().equalsIgnoreCase(name.trim())
                    && !item.isPurchased()) {
                System.out.println("Item already exists in shopping list.");
                return;
            }
        }

        int newId = getNextId();

        ShoppingListItem newItem =
                new ShoppingListItem(newId, name.trim(), quantity, unit);

        shoppingItems.add(newItem);
        save();
    }

    // Automatically add low-stock and out-of-stock pantry items
    public void addLowStockItems(PantryManager pantryManager) {

    for (PantryItem pantryItem : pantryManager.getAllItems()) {

        if (pantryItem.isLowStock() || pantryItem.isOutOfStock()) {

            boolean alreadyExists = false;

            for (ShoppingListItem shoppingItem : shoppingItems) {

                if (shoppingItem.getName().equalsIgnoreCase(
                        pantryItem.getName())) {

                    alreadyExists = true;
                    break;
                }
            }

            if (!alreadyExists) {

                double requiredQuantity = Math.max(
                        1,
                        pantryItem.getMinimumStockLevel()
                                - pantryItem.getQuantity()
                );

                addItem(
                        pantryItem.getName(),
                        requiredQuantity,
                        pantryItem.getUnit()
                );
            }
        }
    }
}

    // Get all shopping list items
    public ArrayList<ShoppingListItem> getAllItems() {
        return new ArrayList<>(shoppingItems);
    }

    // Remove an item using its ID
    public boolean removeItem(int id) {

        boolean removed = shoppingItems.removeIf(
                item -> item.getId() == id
        );

        if (removed) {
            save();
        }

        return removed;
    }

    // Mark an item as purchased or not purchased
    public boolean markAsPurchased(int id, boolean purchased) {

        for (ShoppingListItem item : shoppingItems) {

            if (item.getId() == id) {
                item.setPurchased(purchased);
                save();
                return true;
            }
        }

        return false;
    }

    // Generate the next available ID
    private int getNextId() {

        int maxId = 0;

        for (ShoppingListItem item : shoppingItems) {
            if (item.getId() > maxId) {
                maxId = item.getId();
            }
        }

        return maxId + 1;
    }

    // Save shopping list to file
    public void save() {
        fileManager.saveShoppingList(shoppingItems);
    }
}