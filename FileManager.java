import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class FileManager {

    private static final String PANTRY_FILE = "pantry.txt";
    private static final String SHOPPING_FILE = "shopping_list.txt";

    // Save pantry items
    public void savePantry(ArrayList<PantryItem> items) {

        try (PrintWriter writer = new PrintWriter(new FileWriter(PANTRY_FILE))) {

            for (PantryItem item : items) {

                writer.println(
                    item.getId() + "," +
                    item.getName() + "," +
                    item.getCategory() + "," +
                    item.getQuantity() + "," +
                    item.getUnit() + "," +
                    item.getExpiryDate() + "," +
                    item.getMinimumStockLevel()
                );
            }

        } catch (IOException e) {
            System.out.println("Error saving pantry: " + e.getMessage());
        }
    }

    // Load pantry items
    public ArrayList<PantryItem> loadPantry() {

        ArrayList<PantryItem> items = new ArrayList<>();

        File file = new File(PANTRY_FILE);

        if (!file.exists()) {
            return items;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

            String line;

            while ((line = reader.readLine()) != null) {

                try {
                    String[] data = line.split(",", -1);

                    if (data.length != 7) {
                        continue;
                    }

                    PantryItem item = new PantryItem(
                        Integer.parseInt(data[0]),
                        data[1],
                        data[2],
                        Double.parseDouble(data[3]),
                        data[4],
                        LocalDate.parse(data[5]),
                        Double.parseDouble(data[6])
                    );

                    items.add(item);

                } catch (RuntimeException e) {
                    System.out.println("Skipping invalid pantry record: " + line);
                }
            }

        } catch (IOException e) {
            System.out.println("Error loading pantry: " + e.getMessage());
        }

        return items;
    }

    // Save shopping list
    public void saveShoppingList(ArrayList<ShoppingListItem> items) {
        System.out.println("Shopping list saved at: "
            + new File(SHOPPING_FILE).getAbsolutePath());

        try (PrintWriter writer = new PrintWriter(new FileWriter(SHOPPING_FILE))) {

            for (ShoppingListItem item : items) {

                writer.println(
                    item.getId() + "," +
                    item.getName() + "," +
                    item.getQuantity() + "," +
                    item.getUnit() + "," +
                    item.isPurchased()
                );
            }

        } catch (IOException e) {
            System.out.println("Error saving shopping list: " + e.getMessage());
        }
    }

    // Load shopping list
    public ArrayList<ShoppingListItem> loadShoppingList() {

        ArrayList<ShoppingListItem> items = new ArrayList<>();

        File file = new File(SHOPPING_FILE);

        if (!file.exists()) {
            return items;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

            String line;

            while ((line = reader.readLine()) != null) {

                try {
                    String[] data = line.split(",", -1);

                    if (data.length != 5) {
                        continue;
                    }

                    ShoppingListItem item = new ShoppingListItem(
                        Integer.parseInt(data[0]),
                        data[1],
                        Double.parseDouble(data[2]),
                        data[3]
                    );

                    item.setPurchased(Boolean.parseBoolean(data[4]));

                    items.add(item);

                } catch (RuntimeException e) {
                    System.out.println("Skipping invalid shopping record: " + line);
                }
            }

        } catch (IOException e) {
            System.out.println("Error loading shopping list: " + e.getMessage());
        }

        return items;
    }
}