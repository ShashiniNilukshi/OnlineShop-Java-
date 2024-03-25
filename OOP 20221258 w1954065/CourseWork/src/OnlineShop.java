import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class OnlineShop extends JFrame {
    private ArrayList<String> displayedDataList = new ArrayList<>();
    private ShoppingCart shoppingCart1 = new ShoppingCart();
    private JTextArea productDetailsArea;
    private JTable productTable;
    private DefaultTableModel tableModel;
    private DefaultTableModel tableModel1 = new DefaultTableModel();
    private Map<String, Integer> productQuantities = new HashMap<>();
    private User currentCustomer;
    private double totalCostFromUpdateShoppingCart = 0.0;
    private JButton increaseButton;
    private JButton decreaseButton;
    private JTextField quantityTextField;
    private JDialog cartDialog;
    private JLabel label1;
    private JLabel label2;
    private JLabel label3;
    private JLabel label4;

    public OnlineShop(User currentCustomer) {
        this.currentCustomer = currentCustomer;
        initializeUI();
    }

    public OnlineShop() {
        initializeUI();
    }

    private void initializeUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel1 = new JPanel(new FlowLayout());
        JPanel panel2 = new JPanel(new BorderLayout());

        String[] productTypes = {"All", "Electronics", "Clothing"};
        JComboBox<String> typeComboBox = new JComboBox<>(productTypes);
        JButton visualizeButton = new JButton("Show");

        tableModel = new DefaultTableModel();
        productTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(productTable);
        productTable.setRowHeight(45);

        JButton addToCart = new JButton("Add to Cart");

        JButton shoppingCartButton = new JButton("Shopping Cart");

// Add columns to the table
        tableModel.addColumn("Product ID");
        tableModel.addColumn("Name");
        tableModel.addColumn("Category");
        tableModel.addColumn("Price");
        tableModel.addColumn("Info");

        if (productTable.getColumnCount() > 4) {
            TableColumn infoColumn = productTable.getColumnModel().getColumn(4);
            infoColumn.setCellRenderer(new CustomCellRenderer());
            infoColumn.setPreferredWidth(300);
        }

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(800, 600));

// Upper left panel
        JPanel upperLeftPanel = new JPanel(new FlowLayout());
        upperLeftPanel.add(new JLabel("Select Product Category: "));
        upperLeftPanel.add(typeComboBox);
        upperLeftPanel.add(visualizeButton);
        panel1.add(upperLeftPanel);

// Upper right panel
        JPanel upperRightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        upperRightPanel.add(shoppingCartButton);
        panel1.add(upperRightPanel);

// Center panel with JTable
        panel2.add(tableScrollPane, BorderLayout.CENTER);

// Lower center panel
        JPanel lowerCenterPanel = new JPanel(new BorderLayout());

// Quantity panel
        increaseButton = new JButton("+");
        decreaseButton = new JButton("-");
        quantityTextField = new JTextField("1", 5);

        JPanel quantityPanel = new JPanel();
        quantityPanel.add(decreaseButton);
        quantityPanel.add(quantityTextField);
        quantityPanel.add(increaseButton);
        lowerCenterPanel.add(quantityPanel, BorderLayout.EAST);

        JLabel selectedProductsLabel = new JLabel("Selected Products - Details");
        lowerCenterPanel.add(selectedProductsLabel, BorderLayout.WEST);

// Add the product details area
        productDetailsArea = new JTextArea(6, 20);
        productDetailsArea.setEditable(false);
        lowerCenterPanel.add(productDetailsArea, BorderLayout.CENTER);

// Add to Cart button
        JPanel addToCartPanel = new JPanel();
        addToCartPanel.add(addToCart);
        lowerCenterPanel.add(addToCartPanel, BorderLayout.SOUTH);

        panel2.add(lowerCenterPanel, BorderLayout.SOUTH);

// Add panels to the main frame
        add(panel1, BorderLayout.NORTH);
        add(panel2, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null); // Center the frame on the screen


        // Add action listeners for increment and decrement buttons
        increaseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int quantity = Integer.parseInt(quantityTextField.getText()) + 1;
                quantityTextField.setText(Integer.toString(quantity));
            }
        });

        decreaseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int quantity = Integer.parseInt(quantityTextField.getText()) - 1;
                if (quantity >= 1) {
                    quantityTextField.setText(Integer.toString(quantity));
                }
            }
        });

        add(panel1, BorderLayout.NORTH);
        add(panel2, BorderLayout.CENTER);

        setSize(800, 600);
        setLocationRelativeTo(null); // Center the frame
        setVisible(true);

        visualizeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedType = (String) typeComboBox.getSelectedItem();
                visualizeItems(selectedType);
            }
        });
        // Add action listeners for add to cart button
        addToCart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Parse the quantity from the JTextField
                int quantityToAdd = Integer.parseInt(quantityTextField.getText());

                // Get the selected product details
                String selectedData = productDetailsArea.getText();
                String productID = extractValue(selectedData, "Product ID");

                // Check if both selectedData and productID are non-empty
                if (!selectedData.isEmpty() && !productID.isEmpty()) {
                    // Load all products from file directly
                    WestminsterShoppingManager westminsterShoppingManager = new WestminsterShoppingManager();
                    List<Product> loadedProducts = westminsterShoppingManager.loadAllFromFile("All Products.txt");

                    // Find the selected product in the loaded products list
                    Product selectedProduct = null;
                    for (Product product : loadedProducts) {
                        if (product.getProductID().equals(productID)) {
                            selectedProduct = product;
                            break;
                        }
                    }

                    // Check if the selected product is found
                    if (selectedProduct != null) {
                        double available = selectedProduct.getNoOfAvailableItems();

                        // Check if the available quantity is sufficient
                        if (quantityToAdd <= available) {
                            // Update the available quantity in the loaded products list
                            selectedProduct.setNoOfAvailableItems((int) available - quantityToAdd);
                            // Add the selected product to the shopping cart with the specified quantity
                            SwingUtilities.invokeLater(() -> {
                                createShoppingCartDialog();
                                updateShoppingCart(selectedData, quantityToAdd);
                                updateShoppingCartLabels(totalCostFromUpdateShoppingCart);
                                tableModel1.fireTableDataChanged();
                            });
                        } else {
                            JOptionPane.showMessageDialog(null, "Only " + available + " items available!");
                        }
                    }
                }
            }
        });





        //Add action listeners for shopping cart button
        shoppingCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(() -> {

                    // Show the shopping cart dialog with updated labels
                    showShoppingCartDialog();
                });
            }
        });
        //Add action listeners for the selected row
        productTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = productTable.getSelectedRow();
                    if (selectedRow != -1) {

                        displayRowData(selectedRow);
                    }
                }
            }
        });

    }
    //Add action listeners for show button
    public void visualizeItems(String selectedType) {
        tableModel.setRowCount(0);

        // Load all products from file directly
        WestminsterShoppingManager westminsterShoppingManager = new WestminsterShoppingManager();
        List<Product> loadedProducts = westminsterShoppingManager.loadAllFromFile("All Products.txt");

        DefaultTableModel tableModel = (DefaultTableModel) productTable.getModel();

        for (Product product : loadedProducts) {
            if ("All".equals(selectedType) || selectedType.equals(product.getProductType())) {
                Object[] rowData = createRowData(product);
                tableModel.addRow(rowData);
            }
        }

        // Set the renderer for the "Info" column after adding rows
        if (productTable.getColumnCount() > 4) {
            CustomCellRenderer customCellRenderer = new CustomCellRenderer();
            TableColumn infoColumn = productTable.getColumnModel().getColumn(4);
            infoColumn.setCellRenderer(customCellRenderer);
            infoColumn.setPreferredWidth(500);
        }
    }



    private Object[] createRowData(Product product) {
        // Check if the product is of type Electronics
        if ("Electronics".equals(product.getProductType())) {
            Electronics electronicsProduct = (Electronics) product;
            // Create an array with the product details
            return new Object[]{
                    electronicsProduct.getProductID(),
                    electronicsProduct.getProductName(),
                    electronicsProduct.getProductType(),
                    electronicsProduct.getPrice(),
                    String.format(
                            "Number of Available Items: %s," +
                                    "Manufacturer: %s," +
                                    " Brand: %s," +
                                    " Warranty: %s",
                            electronicsProduct.getNoOfAvailableItems(),
                            electronicsProduct.getManufacturer(),
                            electronicsProduct.getBrand(),
                            electronicsProduct.getWarrantyPeriod())
            };
        }
        // Check if the product is of type Clothing
        else if ("Clothing".equals(product.getProductType())) {
            Clothing clothingProduct = (Clothing) product;
            // Create an array with the product details
            return new Object[]{
                    clothingProduct.getProductID(),
                    clothingProduct.getProductName(),
                    clothingProduct.getProductType(),
                    clothingProduct.getPrice(),
                    String.format(
                            "Number of Available Items: %s," +
                                    "Manufacturer: %s," +
                                    " Size: %s," +
                                    " Color: %s",
                            clothingProduct.getNoOfAvailableItems(),
                            clothingProduct.getManufacturer(),
                            clothingProduct.getSize(),
                            clothingProduct.getColor())
            };
        }
        // Add conditions for other product types if needed
        else {
            return new Object[]{}; // Return an empty array if the product type is not recognized
        }
    }
    //Method to update the shopping cart
    private double updateShoppingCart(String selectedData, int quantityToAdd) {
        double totalCost = 0.0;
        String productID = extractValue(selectedData, "Product ID");

        // Check if the product already exists in the shopping cart
        Integer existingRowIndex = productQuantities.get(productID);

        if (existingRowIndex != null) {
            // Update the quantity and price for the existing row
            int existingQuantity = (int) tableModel1.getValueAt(existingRowIndex, 1);
            tableModel1.setValueAt(existingQuantity + quantityToAdd, existingRowIndex, 1);

            double updatedPrice = (existingQuantity + quantityToAdd) * getPricePerItem(productID);
            tableModel1.setValueAt(formatPrice(updatedPrice), existingRowIndex, 2);
        } else {
            // Add a new row to the table with the selected data, quantity, and price
            double price = getPricePerItem(productID);
            double updatedPrice = quantityToAdd * price;

            tableModel1.addRow(new Object[]{selectedData, quantityToAdd, formatPrice(updatedPrice)});

            // Update the productQuantities map with the new row index
            productQuantities.put(productID, tableModel1.getRowCount() - 1);
        }

        // Calculate the total cost for all items in the shopping cart
        for (int i = 0; i < tableModel1.getRowCount(); i++) {
            Object value = tableModel1.getValueAt(i, 2);
            if (value != null) {
                double rowTotal = Double.parseDouble(value.toString().substring(1));
                totalCost += rowTotal;
            }
        }

        // Set the total cost in the shopping cart
        shoppingCart1.setTotalCost(totalCost);
        totalCostFromUpdateShoppingCart = totalCost;

        // Notify the table of the changes
        tableModel1.fireTableDataChanged();

        return totalCost;
    }

    private String formatPrice(double price) {
        // Use String.format to format the price with two decimal places
        return String.format("$%.2f", price);
    }
    private double getPricePerItem(String productID) {
        for (int i = 0; i < displayedDataList.size(); i++) {
            String displayedData = displayedDataList.get(i);
            String displayedProductID = extractValue(displayedData, "Product ID");
            if (displayedProductID.equals(productID)) {
                String priceStr = extractValue(displayedData, "Price");
                if (!priceStr.isEmpty()) {
                    // Fix the substring to include the entire numeric part of the price
                    if (priceStr.charAt(0) == '$') {
                        priceStr = priceStr.substring(1);
                    }
                    return Double.parseDouble(priceStr);
                }
            }
        }
        return 0.0; // Default value if the price is not found or cannot be parsed
    }
    private String extractValue(String rowData, String columnName) {
        // Split the row data into lines
        String[] lines = rowData.split("\n");

        // Iterate over lines to find the one containing the columnName
        for (String line : lines) {
            if (line.startsWith(columnName)) {
                // Extract the value after the ":"
                return line.substring(columnName.length() + 2);  // +2 to skip the ": " part
            }
        }

        // Return an empty string if the columnName is not found
        return "";
    }
    private boolean hasAtLeastThreeProductsOfSameCategory() {
        // Map to store the count of products for each category
        Map<String, Integer> categoryCountMap = new HashMap<>();

        // Iterate through the displayed data list
        for (String displayedData : displayedDataList) {
            // Extract the category from the displayed data
            String category = extractValue(displayedData, "Category");

            // Check if the category is not empty
            if (!category.isEmpty()) {
                // Update the count for the current category in the map
                categoryCountMap.put(category, categoryCountMap.getOrDefault(category, 0) + 1);




                // Check if the count for the current category is greater than or equal to 3
                if (categoryCountMap.get(category) >= 3) {
                    return true;
                }
            }
        }

        // Return false if no category has at least three products
        return false;
    }
    private void displayRowData(int rowIndex) {
        DefaultTableModel model = (DefaultTableModel) productTable.getModel();
        StringBuilder rowData = new StringBuilder(); // Reset rowData

        // Iterate over each column and append the data of the selected row to rowData
        for (int columnIndex = 0; columnIndex < model.getColumnCount(); columnIndex++) {
            String columnName = model.getColumnName(columnIndex);
            Object cellValue = model.getValueAt(rowIndex, columnIndex);

            if (columnName.equals("Info")) {
                // Handle the last column (Info) differently
                String[] infoLines = cellValue.toString().split(", ");
                for (String infoLine : infoLines) {
                    // Check for "Manufacturer" and always display on a new line
                    if (infoLine.startsWith("Manufacturer")) {
                        rowData.append("\n"); // Add a new line before "Manufacturer"
                    }
                    rowData.append(infoLine).append("\n");
                }
            } else {
                // Handle other columns normally
                rowData.append(columnName).append(": ").append(cellValue).append("\n");
            }
        }

        // Set the JTextArea text with the combined data of all selected rows
        productDetailsArea.setText(rowData.toString());

        // Add the new data to the displayedDataList without clearing
        displayedDataList.add(rowData.toString());
    }





    // Create the shopping cart dialog
    private void createShoppingCartDialog() {
        if (cartDialog == null) {
            // Create a new dialog to show the shopping cart
            cartDialog = new JDialog(this, "Shopping Cart", true);

            // Create a table model with columns: "Product", "Quantity", "Price"
            tableModel1.setColumnIdentifiers(new Object[]{"Product", "Quantity", "Price"});

            // Create a table with the above model
            JTable cartTable = new JTable(tableModel1);
            JScrollPane scrollPane = new JScrollPane(cartTable);

            // Add the cart table to the dialog
            cartDialog.add(scrollPane);

            // Calculate the total price and discounts
            label1 = new JLabel("Total                                    : ");
            label2 = new JLabel("First purchase Discount(10%)             : ");
            label3 = new JLabel("Three items in Same category Discount    : ");
            label4 = new JLabel("Final Total                              : ");

            // Set initial values for labels
            updateShoppingCartLabels(totalCostFromUpdateShoppingCart);

            // Create a panel to hold the labels with a GridLayout
            JPanel labelsPanel = new JPanel(new GridLayout(0, 1));
            labelsPanel.add(label1);
            labelsPanel.add(label2);
            labelsPanel.add(label3);
            labelsPanel.add(label4);

            // Create a panel to hold the table and labels
            JPanel mainPanel = new JPanel(new BorderLayout());
            mainPanel.add(scrollPane, BorderLayout.CENTER);
            mainPanel.add(labelsPanel, BorderLayout.SOUTH);

            // Create buttons panel
            JPanel buttonsPanel = new JPanel(new FlowLayout());

            // Create Remove button
            JButton removeButton = new JButton("Remove");

            buttonsPanel.add(removeButton);
            removeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Get the selected row index
                    int selectedRow = cartTable.getSelectedRow();

                    if (selectedRow != -1) {
                        // Get the quantity and price of the item to be removed
                        int quantityToRemove = (int) tableModel1.getValueAt(selectedRow, 1);

                        // Handle the case where the value is a String with a dollar sign
                        Object priceObject = tableModel1.getValueAt(selectedRow, 2);
                        double priceToRemove;
                        if (priceObject instanceof Double) {
                            priceToRemove = (double) priceObject;
                        } else if (priceObject instanceof String) {
                            // Remove the dollar sign and parse the remaining string to a double
                            String priceString = ((String) priceObject).replace("$", "");
                            try {
                                priceToRemove = Double.parseDouble(priceString);
                            } catch (NumberFormatException ex) {
                                // Handle the case where the String cannot be parsed to a double
                                ex.printStackTrace();
                                return;
                            }
                        } else {
                            // Handle other types if needed
                            return;
                        }

                        // Update total cost and labels
                        totalCostFromUpdateShoppingCart -= (quantityToRemove * priceToRemove);
                        updateShoppingCartLabels(totalCostFromUpdateShoppingCart);

                        // Remove the selected row from the table
                        tableModel1.removeRow(selectedRow);
                    } else {
                        JOptionPane.showMessageDialog(cartDialog, "Please select a row to remove.");
                    }
                }
            });

            // Create Purchase button
            JButton purchaseButton = new JButton("Purchase");
            buttonsPanel.add(purchaseButton);

            purchaseButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (currentCustomer != null) {
                        currentCustomer.incrementPurchaseCount();
                        currentCustomer. savePurchaseToFile();
                        updateAvailableItemsInFile();

                    }


                        // Clear the shopping cart table
                        clearShoppingCartTable();


                }
            });


            // Add the main panel and buttons panel to the dialog
            cartDialog.add(mainPanel);
            cartDialog.add(buttonsPanel, BorderLayout.SOUTH);

            // Set the size and make the dialog visible
            cartDialog.setSize(500, 500);
            cartDialog.setLocationRelativeTo(this);
        }
    }

    // Show the shopping cart dialog with updated labels
    private void showShoppingCartDialog() {
        if (cartDialog != null) {
            updateShoppingCartLabels(totalCostFromUpdateShoppingCart);
            cartDialog.setVisible(true);
        }else{
            //Put a code to show error msg
        }
    }
    private void updateShoppingCartLabels(double totalCost) {
        // Calculate the final price with discounts
        double firstPurchaseDiscount = 0.0;
        double categoryDiscount = hasAtLeastThreeProductsOfSameCategory() ? totalCost * 0.2 : 0.0;

        // Update labels with placeholders
        label1.setText("Total                                    : " + String.format("%.2f", totalCost));

        // Load purchase count from file and check for first purchase discount
        if (currentCustomer != null) {
            String usernameToCheck = currentCustomer.getUserName(); // Assuming getUsername() is a method to get the username
            if (!currentCustomer.isUserInFile(usernameToCheck)) {
                // User exists in the file
                firstPurchaseDiscount = totalCost * 0.1;


            }

            label2.setText("First purchase Discount(10%)             : " + String.format("%.2f", firstPurchaseDiscount));
            label3.setText("Three items in Same category Discount    : " + String.format("%.2f", categoryDiscount));

            double finalPrice = totalCost - firstPurchaseDiscount - categoryDiscount;
            label4.setText("Final Total                              : " + String.format("%.2f", finalPrice));


        }
    }


    // Method to clear the shopping cart table
    private void clearShoppingCartTable() {
        tableModel1.setRowCount(0);
        totalCostFromUpdateShoppingCart = 0.0; // Assuming totalCostFromUpdateShoppingCart is a double
        updateShoppingCartLabels(totalCostFromUpdateShoppingCart);

    }
    private static class CustomCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            try {
                // Retrieve the "Number of available Items" from the file
                WestminsterShoppingManager westminsterShoppingManager = new WestminsterShoppingManager();
                List<Product> loadedProducts = westminsterShoppingManager.loadAllFromFile("All Products.txt");

                // Find the product for the current row
                Product currentProduct = loadedProducts.get(row);

                // Extract the "Number of available Items" from the product
                int availability = currentProduct.getNoOfAvailableItems();

                // Check if the availability is less than 3
                if (availability < 3) {
                    // Set the background color for the entire row
                    for (int i = 0; i < table.getColumnCount(); i++) {
                        table.getColumnModel().getColumn(i).setCellRenderer(new CustomCellRenderer());
                    }

                    cellComponent.setBackground(Color.RED);
                    cellComponent.setForeground(Color.WHITE);  // Optionally set text color for better visibility
                } else {
                    // Reset background color if availability is not less than 3
                    cellComponent.setBackground(table.getBackground());
                    cellComponent.setForeground(table.getForeground());
                }
            } catch (Exception e) {
                // Handle exceptions
                e.printStackTrace();
            }

            return cellComponent;
        }
    }
    private void updateAvailableItemsInFile() {
        try {
            WestminsterShoppingManager westminsterShoppingManager = new WestminsterShoppingManager();
            List<Product> loadedProducts = westminsterShoppingManager.loadAllFromFile("All Products.txt");

            // Iterate through the rows in the shopping cart table
            for (int i = 0; i < tableModel1.getRowCount(); i++) {
                String selectedData = (String) tableModel1.getValueAt(i, 0);
                int quantityToRemove = (int) tableModel1.getValueAt(i, 1);
                String productID = extractValue(selectedData, "Product ID");

                // Find the corresponding product in the loaded products list
                for (Product product : loadedProducts) {
                    if (product.getProductID().equals(productID)) {
                        // Update the number of available items
                        int newQuantity = product.getNoOfAvailableItems() - quantityToRemove;
                        product.setNoOfAvailableItems(newQuantity);
                        break;
                    }
                }
            }

            // Save the updated number of available items back to the file
            saveAvailableItemsToFile("All Products.txt", loadedProducts);
        } catch (Exception ex) {
            System.out.println("Can't save to file");
            ex.printStackTrace();
        }
    }


    public void saveAvailableItemsToFile(String filename, List<Product> products) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (Product product : products) {
                // Write only the product ID and the updated number of available items to the file
                if (product.getNoOfAvailableItems() > 0) {
                    writer.println(product.toFileString());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }





    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new OnlineShop();
        });
    }
}
