import java.time.LocalDate;

public class Main {

    public static void main(String[] args) {

        // Initialize managers
        FileManager fileManager = new FileManager();
        PantryManager pantryManager = new PantryManager();
        ShoppingListManager shoppingManager = new ShoppingListManager();
        RecipeSuggestionService recipeService =
                new RecipeSuggestionService();

        // Load existing pantry data
        for (PantryItem item : fileManager.loadPantry()) {
            pantryManager.addItem(item);
        }

        System.out.println("================================");
        System.out.println("       WELCOME TO PANTRYPAL");
        System.out.println("================================");

        // Display pantry items
        System.out.println("\nPANTRY ITEMS:");

        for (PantryItem item : pantryManager.getAllItems()) {
            System.out.println(
                    item.getName() + " - "
                    + item.getQuantity() + " "
                    + item.getUnit() + " | "
                    + item.getStatus()
            );
        }

        // Display inventory summary
        System.out.println("\nINVENTORY SUMMARY:");
        System.out.println("Total Items: " + pantryManager.getTotalItems());
        System.out.println("Low Stock: " + pantryManager.getLowStockItems().size());
        System.out.println("Out of Stock: " + pantryManager.getOutOfStockItems().size());
        System.out.println("Expired: " + pantryManager.getExpiredItems().size());
        System.out.println("Expiring Soon: " + pantryManager.getExpiringSoonItems().size());

        // Automatically update shopping list
        shoppingManager.addLowStockItems(pantryManager);

        System.out.println("\nSHOPPING LIST:");

        for (ShoppingListItem item : shoppingManager.getAllItems()) {
            System.out.println(item);
        }

        // Display recipe suggestions
        System.out.println("\nRECIPE SUGGESTIONS:");

        for (String recipe : recipeService.suggestRecipes(
                pantryManager.getAllItems())) {
            System.out.println("- " + recipe);
        }

        System.out.println("\n================================");
        System.out.println("       PANTRYPAL READY!");
        System.out.println("================================");
    }
}