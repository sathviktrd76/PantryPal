import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class SwingMain extends JFrame {

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("dd-MM-yyyy");

    private PantryManager pantryManager;
    private ShoppingListManager shoppingManager;
    private FileManager fileManager;

    private DefaultTableModel tableModel;
    private JTable pantryTable;

    public SwingMain() {

        fileManager = new FileManager();
        pantryManager = new PantryManager();
        shoppingManager = new ShoppingListManager();

        // Load saved pantry items
        for (PantryItem item : fileManager.loadPantry()) {
            pantryManager.addItem(item);
        }

        setTitle("PantryPal - Smart Pantry Management");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        createInterface();
        refreshTable();
    }

    private void createInterface() {

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 247, 250));

        // Header
        JLabel heading = new JLabel("  PantryPal");
        heading.setFont(new Font("Arial", Font.BOLD, 28));
        heading.setForeground(Color.WHITE);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(25, 35, 65));
        header.setPreferredSize(new Dimension(1000, 70));
        header.add(heading, BorderLayout.WEST);

        // Navigation tabs
        JTabbedPane tabs = new JTabbedPane();

        tabs.addTab("Dashboard", createDashboard());
        tabs.addTab("Pantry", createPantryPanel());
        tabs.addTab("Shopping List", createShoppingPanel());
        tabs.addTab("Recipes", createRecipePanel());

        mainPanel.add(header, BorderLayout.NORTH);
        mainPanel.add(tabs, BorderLayout.CENTER);

        add(mainPanel);
    }

    // Dashboard
    private JPanel createDashboard() {

        JPanel panel = new JPanel(new GridLayout(2, 3, 15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        panel.setBackground(new Color(245, 247, 250));

        panel.add(createCard("Total Items",
                String.valueOf(pantryManager.getTotalItems())));

        panel.add(createCard("Low Stock",
                String.valueOf(pantryManager.getLowStockItems().size())));

        panel.add(createCard("Out of Stock",
                String.valueOf(pantryManager.getOutOfStockItems().size())));

        panel.add(createCard("Expired",
                String.valueOf(pantryManager.getExpiredItems().size())));

        panel.add(createCard("Expiring Soon",
                String.valueOf(pantryManager.getExpiringSoonItems().size())));

        panel.add(createCard("Categories",
                String.valueOf(pantryManager.getAllItems().stream()
                        .map(PantryItem::getCategory)
                        .distinct()
                        .count())));

        return panel;
    }

    private JPanel createCard(String title, String value) {

        JPanel card = new JPanel(new GridLayout(2, 1));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 225, 235)),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 30));
        valueLabel.setForeground(new Color(40, 90, 170));

        card.add(titleLabel);
        card.add(valueLabel);

        return card;
    }

    // Pantry management
    private JPanel createPantryPanel() {

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        String[] columns = {
                "ID", "Name", "Category", "Quantity",
                "Unit", "Expiry Date", "Status"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        pantryTable = new JTable(tableModel);
        panel.add(new JScrollPane(pantryTable), BorderLayout.CENTER);

        JPanel buttons = new JPanel();

        JButton addButton = new JButton("Add Item");
        JButton deleteButton = new JButton("Delete Item");
        JButton refreshButton = new JButton("Refresh");

        addButton.addActionListener(e -> addPantryItem());
        deleteButton.addActionListener(e -> deletePantryItem());
        refreshButton.addActionListener(e -> refreshTable());

        buttons.add(addButton);
        buttons.add(deleteButton);
        buttons.add(refreshButton);

        panel.add(buttons, BorderLayout.SOUTH);

        return panel;
    }

    private void addPantryItem() {

        try {

            String name = JOptionPane.showInputDialog(
                    this, "Enter item name:");

            if (name == null || name.trim().isEmpty()) return;

            String category = JOptionPane.showInputDialog(
                    this, "Enter category:");

            if (category == null || category.trim().isEmpty()) return;

            double quantity = Double.parseDouble(
                    JOptionPane.showInputDialog(this, "Enter quantity:"));

            String unit = JOptionPane.showInputDialog(
                    this, "Enter unit (kg, litre, pieces):");

            if (unit == null || unit.trim().isEmpty()) return;

            String expiryInput = JOptionPane.showInputDialog(
                    this, "Enter expiry date (DD-MM-YYYY):");

            if (expiryInput == null || expiryInput.trim().isEmpty()) return;

            LocalDate expiryDate = LocalDate.parse(
                    expiryInput.trim(), DATE_FORMATTER);

            double minimumStock = Double.parseDouble(
                    JOptionPane.showInputDialog(
                            this, "Enter minimum stock level:"));

            if (quantity < 0 || minimumStock < 0) {
                JOptionPane.showMessageDialog(this,
                        "Quantity and minimum stock cannot be negative.");
                return;
            }

            int newId = pantryManager.getAllItems().stream()
                    .mapToInt(PantryItem::getId)
                    .max()
                    .orElse(0) + 1;

            PantryItem item = new PantryItem(
                    newId, name, category, quantity,
                    unit, expiryDate, minimumStock
            );

            pantryManager.addItem(item);

            fileManager.savePantry(pantryManager.getAllItems());

            refreshTable();

            JOptionPane.showMessageDialog(
                    this, "Item added successfully!");

        } catch (DateTimeParseException ex) {

            JOptionPane.showMessageDialog(this,
                    "Invalid date. Please use DD-MM-YYYY format.");

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(this,
                    "Please enter valid numeric values.");

        }
    }

    private void deletePantryItem() {

        int selectedRow = pantryTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select an item.");
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);

        if (pantryManager.deleteItem(id)) {

            fileManager.savePantry(pantryManager.getAllItems());

            refreshTable();

            JOptionPane.showMessageDialog(
                    this, "Item deleted successfully!");
        }
    }

    private void refreshTable() {

        tableModel.setRowCount(0);

        for (PantryItem item : pantryManager.getAllItems()) {

            tableModel.addRow(new Object[]{
                    item.getId(),
                    item.getName(),
                    item.getCategory(),
                    item.getQuantity(),
                    item.getUnit(),
                    item.getExpiryDate() == null
                            ? ""
                            : DATE_FORMATTER.format(item.getExpiryDate()),
                    item.getStatus()
            });
        }
    }

    // Shopping list
    private JPanel createShoppingPanel() {

        JPanel panel = new JPanel(new BorderLayout());

        DefaultListModel<String> listModel = new DefaultListModel<>();

        for (ShoppingListItem item : shoppingManager.getAllItems()) {
            listModel.addElement(item.toString()
                    + (item.isPurchased() ? " (Purchased)" : ""));
        }

        JList<String> list = new JList<>(listModel);

        JButton refreshButton = new JButton("Refresh Shopping List");

        refreshButton.addActionListener(e -> {

            listModel.clear();

            for (ShoppingListItem item : shoppingManager.getAllItems()) {
                listModel.addElement(item.toString()
                        + (item.isPurchased() ? " (Purchased)" : ""));
            }
        });

        panel.add(new JScrollPane(list), BorderLayout.CENTER);
        panel.add(refreshButton, BorderLayout.SOUTH);

        return panel;
    }

    // Recipe suggestions
    private JPanel createRecipePanel() {

        JPanel panel = new JPanel(new BorderLayout());

        RecipeSuggestionService recipeService =
                new RecipeSuggestionService();

        DefaultListModel<String> recipeModel = new DefaultListModel<>();

        for (String recipe : recipeService.suggestRecipes(
                pantryManager.getAllItems())) {
            recipeModel.addElement(recipe);
        }

        if (recipeModel.isEmpty()) {
            recipeModel.addElement(
                    "No recipes available with your current ingredients.");
        }

        JList<String> recipeList = new JList<>(recipeModel);

        panel.add(new JScrollPane(recipeList), BorderLayout.CENTER);

        return panel;
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            SwingMain app = new SwingMain();
            app.setVisible(true);
        });
    }
}