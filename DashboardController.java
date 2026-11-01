import javafx.util.StringConverter;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class DashboardController {

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("dd-MM-yyyy");

    // Navigation sections
    @FXML private VBox dashboardSection;
    @FXML private VBox pantrySection;
    @FXML private VBox shoppingSection;
    @FXML private VBox recipesSection;

    // Dashboard labels
    @FXML private Label totalItemsLabel;
    @FXML private Label lowStockLabel;
    @FXML private Label outOfStockLabel;
    @FXML private Label expiredLabel;
    @FXML private Label expiringSoonLabel;

    // Dashboard table
    @FXML private TableView<PantryItem> pantryTable;
    @FXML private TableColumn<PantryItem, Integer> idColumn;
    @FXML private TableColumn<PantryItem, String> nameColumn;
    @FXML private TableColumn<PantryItem, String> categoryColumn;
    @FXML private TableColumn<PantryItem, Double> quantityColumn;
    @FXML private TableColumn<PantryItem, String> unitColumn;
    @FXML private TableColumn<PantryItem, String> statusColumn;

    // Pantry management fields
    @FXML private TextField itemNameField;
    @FXML private TextField categoryField;
    @FXML private TextField quantityField;
    @FXML private TextField unitField;
    @FXML private DatePicker expiryDatePicker;
    @FXML private TextField minimumStockField;
    @FXML private TextField searchField;

    // Pantry management table
    @FXML private TableView<PantryItem> managementPantryTable;
    @FXML private TableColumn<PantryItem, Integer> manageIdColumn;
    @FXML private TableColumn<PantryItem, String> manageNameColumn;
    @FXML private TableColumn<PantryItem, String> manageCategoryColumn;
    @FXML private TableColumn<PantryItem, Double> manageQuantityColumn;
    @FXML private TableColumn<PantryItem, String> manageUnitColumn;
    @FXML private TableColumn<PantryItem, LocalDate> expiryColumn;
    @FXML private TableColumn<PantryItem, String> manageStatusColumn;

    // Shopping list fields
    @FXML private TextField shoppingNameField;
    @FXML private TextField shoppingQuantityField;
    @FXML private TextField shoppingUnitField;

    // Shopping list table
    @FXML private TableView<ShoppingListItem> shoppingTable;
    @FXML private TableColumn<ShoppingListItem, Integer> shoppingIdColumn;
    @FXML private TableColumn<ShoppingListItem, String> shoppingItemColumn;
    @FXML private TableColumn<ShoppingListItem, Double> shoppingQtyColumn;
    @FXML private TableColumn<ShoppingListItem, String> shoppingUnitColumn;
    @FXML private TableColumn<ShoppingListItem, Boolean> purchasedColumn;

    // Recipe list
    @FXML private ListView<String> recipeList;

    // Backend objects
    private PantryManager pantryManager;
    private ShoppingListManager shoppingManager;
    private FileManager fileManager;
    private RecipeSuggestionService recipeService;

    @FXML
    public void initialize() {

        fileManager = new FileManager();
        pantryManager = new PantryManager();
        shoppingManager = new ShoppingListManager();
        recipeService = new RecipeSuggestionService();

        configureExpiryDatePicker();

        // Load saved pantry items
        for (PantryItem item : fileManager.loadPantry()) {
            pantryManager.addItem(item);
        }

        setupTables();

        refreshDashboard();
        refreshPantry();
        refreshShoppingList();
    }

    // Configure DD-MM-YYYY date input
    private void configureExpiryDatePicker() {

        expiryDatePicker.setConverter(new StringConverter<LocalDate>() {

            @Override
            public String toString(LocalDate date) {
                return date == null ? "" : DATE_FORMATTER.format(date);
            }

            @Override
            public LocalDate fromString(String date) {

                if (date == null || date.trim().isEmpty()) {
                    return null;
                }

                return LocalDate.parse(date.trim(), DATE_FORMATTER);
            }
        });

        expiryDatePicker.setPromptText("DD-MM-YYYY");
    }

    // Configure table columns
    private void setupTables() {

        idColumn.setCellValueFactory(
                new PropertyValueFactory<>("id"));

        nameColumn.setCellValueFactory(
                new PropertyValueFactory<>("name"));

        categoryColumn.setCellValueFactory(
                new PropertyValueFactory<>("category"));

        quantityColumn.setCellValueFactory(
                new PropertyValueFactory<>("quantity"));

        unitColumn.setCellValueFactory(
                new PropertyValueFactory<>("unit"));

        statusColumn.setCellValueFactory(
                new PropertyValueFactory<>("status"));

        configureStatusColumn(statusColumn);

        manageIdColumn.setCellValueFactory(
                new PropertyValueFactory<>("id"));

        manageNameColumn.setCellValueFactory(
                new PropertyValueFactory<>("name"));

        manageCategoryColumn.setCellValueFactory(
                new PropertyValueFactory<>("category"));

        manageQuantityColumn.setCellValueFactory(
                new PropertyValueFactory<>("quantity"));

        manageUnitColumn.setCellValueFactory(
                new PropertyValueFactory<>("unit"));

        expiryColumn.setCellValueFactory(
                new PropertyValueFactory<>("expiryDate"));

        // Display expiry dates in DD-MM-YYYY format
        expiryColumn.setCellFactory(column ->
                new TableCell<PantryItem, LocalDate>() {

            @Override
            protected void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);

                setText(empty || date == null
                        ? null
                        : DATE_FORMATTER.format(date));
            }
        });

        manageStatusColumn.setCellValueFactory(
                new PropertyValueFactory<>("status"));

        configureStatusColumn(manageStatusColumn);

        shoppingIdColumn.setCellValueFactory(
                new PropertyValueFactory<>("id"));

        shoppingItemColumn.setCellValueFactory(
                new PropertyValueFactory<>("name"));

        shoppingQtyColumn.setCellValueFactory(
                new PropertyValueFactory<>("quantity"));

        shoppingUnitColumn.setCellValueFactory(
                new PropertyValueFactory<>("unit"));

        purchasedColumn.setCellValueFactory(
                new PropertyValueFactory<>("purchased"));

        purchasedColumn.setCellFactory(column ->
                new TableCell<ShoppingListItem, Boolean>() {

            @Override
            protected void updateItem(Boolean purchased, boolean empty) {
                super.updateItem(purchased, empty);

                if (empty || purchased == null) {
                    setText(null);
                    setGraphic(null);
                } else {

                    Label statusLabel =
                            new Label(purchased ? "True" : "False");

                    statusLabel.setStyle(
                            purchased
                                    ? "-fx-text-fill: #27864A; -fx-font-weight: bold;"
                                    : "-fx-text-fill: #DC2626; -fx-font-weight: bold;"
                    );

                    setText(null);
                    setGraphic(statusLabel);
                }
            }
        });

        // Select pantry item to edit
        managementPantryTable.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldItem, selectedItem) -> {

                    if (selectedItem != null) {

                        itemNameField.setText(selectedItem.getName());
                        categoryField.setText(selectedItem.getCategory());

                        quantityField.setText(
                                String.valueOf(selectedItem.getQuantity()));

                        unitField.setText(selectedItem.getUnit());

                        expiryDatePicker.setValue(
                                selectedItem.getExpiryDate());

                        minimumStockField.setText(
                                String.valueOf(
                                        selectedItem.getMinimumStockLevel()));
                    }
                });
    }

    // Navigation
    private void showSection(VBox selectedSection) {

        dashboardSection.setVisible(false);
        dashboardSection.setManaged(false);

        pantrySection.setVisible(false);
        pantrySection.setManaged(false);

        shoppingSection.setVisible(false);
        shoppingSection.setManaged(false);

        recipesSection.setVisible(false);
        recipesSection.setManaged(false);

        selectedSection.setVisible(true);
        selectedSection.setManaged(true);
    }

    @FXML
    public void showDashboard() {
        showSection(dashboardSection);
        refreshDashboard();
    }

    @FXML
    public void showPantry() {
        showSection(pantrySection);
        refreshPantry();
    }

    @FXML
    public void showShopping() {
        showSection(shoppingSection);
        refreshShoppingList();
    }

    @FXML
    public void showRecipes() {
        showSection(recipesSection);
        generateRecipes();
    }

    // Dashboard
    @FXML
    public void refreshDashboard() {

        ArrayList<PantryItem> items =
                pantryManager.getAllItems();

        totalItemsLabel.setText(
                String.valueOf(pantryManager.getTotalItems()));

        lowStockLabel.setText(
                String.valueOf(pantryManager.getLowStockItems().size()));

        outOfStockLabel.setText(
                String.valueOf(pantryManager.getOutOfStockItems().size()));

        expiredLabel.setText(
                String.valueOf(pantryManager.getExpiredItems().size()));

        expiringSoonLabel.setText(
                String.valueOf(pantryManager.getExpiringSoonItems().size()));

        pantryTable.setItems(
                FXCollections.observableArrayList(items));
    }

    // Pantry management
    @FXML
    public void addPantryItem() {

        try {

            String name = itemNameField.getText().trim();
            String category = categoryField.getText().trim();
            String unit = unitField.getText().trim();

            if (name.isEmpty() || category.isEmpty() || unit.isEmpty()
                    || quantityField.getText().trim().isEmpty()
                    || minimumStockField.getText().trim().isEmpty()
                    || expiryDatePicker.getValue() == null) {

                showAlert("Please fill in all pantry fields.");
                return;
            }

            double quantity =
                    Double.parseDouble(quantityField.getText().trim());

            double minimumStock =
                    Double.parseDouble(minimumStockField.getText().trim());

            if (quantity < 0 || minimumStock < 0) {
                showAlert("Quantity and minimum stock cannot be negative.");
                return;
            }

            PantryItem item = new PantryItem(
                    getNextPantryId(),
                    name,
                    category,
                    quantity,
                    unit,
                    expiryDatePicker.getValue(),
                    minimumStock
            );

            pantryManager.addItem(item);

            fileManager.savePantry(pantryManager.getAllItems());

            clearPantryFields();

            managementPantryTable.refresh();
            pantryTable.refresh();

            refreshPantry();
            refreshDashboard();

            showMessage("Pantry item added successfully!");

        } catch (NumberFormatException e) {
            showAlert("Please enter valid numeric values.");
        } catch (java.time.format.DateTimeParseException e) {
            showAlert("Please enter the expiry date in DD-MM-YYYY format.");
        }
    }

    @FXML
    public void updatePantryItem() {

        PantryItem selected =
                managementPantryTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert("Please select an item to update.");
            return;
        }

        try {

            String name = itemNameField.getText().trim();
            String category = categoryField.getText().trim();
            String unit = unitField.getText().trim();

            if (name.isEmpty() || category.isEmpty() || unit.isEmpty()
                    || quantityField.getText().trim().isEmpty()
                    || minimumStockField.getText().trim().isEmpty()
                    || expiryDatePicker.getValue() == null) {

                showAlert("Please fill in all fields.");
                return;
            }

            double quantity =
                    Double.parseDouble(quantityField.getText().trim());

            double minimumStock =
                    Double.parseDouble(minimumStockField.getText().trim());

            if (quantity < 0 || minimumStock < 0) {
                showAlert("Quantity and minimum stock cannot be negative.");
                return;
            }

            PantryItem updatedItem = new PantryItem(
                    selected.getId(),
                    name,
                    category,
                    quantity,
                    unit,
                    expiryDatePicker.getValue(),
                    minimumStock
            );

            boolean updated = pantryManager.updateItem(
                    selected.getId(), updatedItem);

            if (updated) {

                fileManager.savePantry(pantryManager.getAllItems());

                refreshPantry();
                refreshDashboard();

                managementPantryTable.refresh();
                pantryTable.refresh();

                clearPantryFields();

                showMessage("Item updated successfully!");

            } else {
                showAlert("Unable to update the selected item.");
            }

        } catch (NumberFormatException e) {
            showAlert("Please enter valid numeric values.");
        } catch (java.time.format.DateTimeParseException e) {
            showAlert("Please enter the expiry date in DD-MM-YYYY format.");
        }
    }

    @FXML
    public void deleteSelectedItem() {

        PantryItem selected =
                managementPantryTable.getSelectionModel()
                        .getSelectedItem();

        if (selected == null) {
            showAlert("Please select an item to delete.");
            return;
        }

        pantryManager.deleteItem(selected.getId());

        fileManager.savePantry(pantryManager.getAllItems());

        clearPantryFields();

        refreshPantry();
        refreshDashboard();

        showMessage("Pantry item deleted successfully!");
    }

    @FXML
    public void searchPantry() {

        String keyword = searchField.getText().trim().toLowerCase();

        if (keyword.isEmpty()) {
            refreshPantry();
            return;
        }

        ArrayList<PantryItem> results =
                pantryManager.getAllItems().stream()
                        .filter(item ->
                                item.getName().toLowerCase().contains(keyword)
                                || item.getCategory().toLowerCase()
                                        .contains(keyword))
                        .collect(Collectors.toCollection(ArrayList::new));

        managementPantryTable.setItems(
                FXCollections.observableArrayList(results));
    }

    @FXML
    public void refreshPantry() {

        ArrayList<PantryItem> items = pantryManager.getAllItems();

        managementPantryTable.setItems(
                FXCollections.observableArrayList(items));

        pantryTable.setItems(
                FXCollections.observableArrayList(items));
    }

    @FXML
    public void clearPantryFields() {

        itemNameField.clear();
        categoryField.clear();
        quantityField.clear();
        unitField.clear();
        minimumStockField.clear();
        expiryDatePicker.setValue(null);

        managementPantryTable.getSelectionModel().clearSelection();
    }

    private int getNextPantryId() {

        int maxId = 0;

        for (PantryItem item : pantryManager.getAllItems()) {
            maxId = Math.max(maxId, item.getId());
        }

        return maxId + 1;
    }

    // Shopping list
    @FXML
    public void addShoppingItem() {

        try {

            String name = shoppingNameField.getText().trim();
            String unit = shoppingUnitField.getText().trim();

            if (name.isEmpty() || unit.isEmpty()
                    || shoppingQuantityField.getText().trim().isEmpty()) {

                showAlert("Please fill in all shopping list fields.");
                return;
            }

            double quantity =
                    Double.parseDouble(shoppingQuantityField.getText().trim());

            if (quantity <= 0) {
                showAlert("Quantity must be greater than zero.");
                return;
            }

            shoppingManager.addItem(name, quantity, unit);

            shoppingNameField.clear();
            shoppingQuantityField.clear();
            shoppingUnitField.clear();

            refreshShoppingList();

            showMessage("Shopping list updated!");

        } catch (NumberFormatException e) {
            showAlert("Please enter a valid quantity.");
        }
    }

    @FXML
    public void addLowStockItems() {

        shoppingManager.addLowStockItems(pantryManager);

        refreshShoppingList();

        showMessage("Low-stock items added to shopping list!");
    }

    @FXML
    public void markPurchased() {

        ShoppingListItem selected =
                shoppingTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert("Please select a shopping item.");
            return;
        }

        boolean updated = shoppingManager.markAsPurchased(
                selected.getId(), !selected.isPurchased());

        if (updated) {

            refreshShoppingList();

            showMessage("Shopping item status updated successfully!");

        } else {
            showAlert("Unable to update shopping item. Please try again.");
        }
    }

    @FXML
    public void removeShoppingItem() {

        ShoppingListItem selected =
                shoppingTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert("Please select an item to remove.");
            return;
        }

        shoppingManager.removeItem(selected.getId());

        refreshShoppingList();

        showMessage("Shopping item removed!");
    }

    private void refreshShoppingList() {

        shoppingTable.setItems(
                FXCollections.observableArrayList(
                        shoppingManager.getAllItems()));

        shoppingTable.refresh();
    }

    // Recipe suggestions
    @FXML
    public void generateRecipes() {

        ArrayList<String> suggestions =
                recipeService.suggestRecipes(
                        pantryManager.getAllItems());

        recipeList.setItems(
                FXCollections.observableArrayList(suggestions));

        if (suggestions.isEmpty()) {
            recipeList.getItems().add(
                    "No recipes available with your current ingredients.");
        }
    }

    // Message dialogs
    private void showAlert(String message) {

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("PantryPal");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showMessage(String message) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("PantryPal");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Configure colour-coded pantry statuses
    private void configureStatusColumn(
            TableColumn<PantryItem, String> column) {

        column.setCellFactory(col ->
                new TableCell<PantryItem, String>() {

            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);

                getStyleClass().removeAll(
                        "status-available",
                        "status-in-stock",
                        "status-low-stock",
                        "status-out-of-stock",
                        "status-expired",
                        "status-expiring-soon"
                );

                if (empty || status == null) {
                    setText(null);
                } else {

                    setText(status);

                    switch (status.toLowerCase().trim()) {

                        case "in stock":
                            getStyleClass().add("status-in-stock");
                            break;

                        case "low stock":
                            getStyleClass().add("status-low-stock");
                            break;

                        case "out of stock":
                            getStyleClass().add("status-out-of-stock");
                            break;

                        case "expired":
                            getStyleClass().add("status-expired");
                            break;

                        case "expiring soon":
                            getStyleClass().add("status-expiring-soon");
                            break;
                    }
                }
            }
        });
    }
}