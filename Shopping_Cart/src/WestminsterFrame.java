import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class WestminsterFrame extends JFrame {

    // Instance variable to store the user's shopping cart
    public static ShoppingCart usersShoppingCart = new ShoppingCart();

    public static JPanel topPanel, bottomPanel,
            mediaPanel, mediaPanel1, mediaPanel2, mediaPanel3,
            bottomPanel1, bottomPanel2;

    public static JTable table;
    public static JScrollPane scrollPane;
    public static String dropDownOption;
    public static Product selectedProduct;
    public static JLabel idLabel, categoryLabel, nameLabel, availItemsLabel, extraLabel1, extraLabel2;

    public WestminsterFrame() {
        // Set the size and layout of the JFrame
        setSize(800, 700);
        setLayout(new GridLayout(2, 1));

        // Initialize Swing components and panels
        topPanel = new JPanel(new GridLayout(2, 1));
        bottomPanel = new JPanel(new GridLayout(1, 2));
        mediaPanel = new JPanel(new GridLayout(1, 3));

        mediaPanel1 = new JPanel(new GridBagLayout());
        mediaPanel2 = new JPanel(new GridBagLayout());
        mediaPanel3 = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        // Set the background color of the panels
        bottomPanel.setBackground(Color.BLUE);
        setVisible(true);

        bottomPanel1 = new JPanel();
        bottomPanel2 = new JPanel();

        bottomPanel1.setLayout(new BoxLayout(bottomPanel1, BoxLayout.Y_AXIS));

        // Set up border for JLabels
        int marginSize = 7;
        EmptyBorder emptyBorder = new EmptyBorder(marginSize, marginSize * 4, marginSize, 0);

        // Initialize Swing components and panels
        idLabel = new JLabel("Product ID ");
        nameLabel = new JLabel("Name ");
        categoryLabel = new JLabel("category ");
        availItemsLabel = new JLabel("available label ");
        extraLabel1 = new JLabel("Size ");
        extraLabel2 = new JLabel("Color ");

        // Set borders for JLabels
        bottomPanel1.setBorder(emptyBorder);
        idLabel.setBorder(emptyBorder);
        nameLabel.setBorder(emptyBorder);
        categoryLabel.setBorder(emptyBorder);
        availItemsLabel.setBorder(emptyBorder);
        extraLabel1.setBorder(emptyBorder);
        extraLabel2.setBorder(emptyBorder);

        // JLabel for top panel text
        JLabel topPanelText = new JLabel("Select Product Category");
        JLabel bottomPanelText = new JLabel("Selected Product - Details");

        // Dropdown menu for selecting product category
        String[] dropdownList = {"All", "Electronic", "Clothing"};
        JComboBox<String> dropdownMenu = new JComboBox<>(dropdownList);
        dropdownMenu.setPrototypeDisplayValue("NewStringHereText");

        // Initialize dropdownOption with the selected item
        dropDownOption = (String) dropdownMenu.getSelectedItem();

        // Buttons for shopping cart and adding to the cart
        JButton shoppingCartButton = new JButton("View Shopping Cart");
        JButton addToCartButton = new JButton("Add to Shopping Cart");
        bottomPanel2.add(addToCartButton);

        // Add components to mediaPanel
        mediaPanel1.add(topPanelText);
        mediaPanel2.add(dropdownMenu);
        mediaPanel3.add(shoppingCartButton);

        mediaPanel.add(mediaPanel1);
        mediaPanel.add(mediaPanel2);
        mediaPanel.add(mediaPanel3);

        // Create a table using the createTable method
        table = createTable(WestminsterShoppingManager.productList.getProductList());
        scrollPane = new JScrollPane(table);

        // Add an action listener to the dropdown menu to update the table
        dropdownMenu.addActionListener(e -> {
            dropDownOption = (String) dropdownMenu.getSelectedItem();
            System.out.println(dropDownOption);
            updateTableModel();
            topPanel.revalidate();
        });

        // Add components to topPanel
        topPanel.add(mediaPanel);
        topPanel.add(scrollPane);

        // Add a selection listener to the table for updating selected product details
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow != -1) {
                        int modelRow = table.convertRowIndexToModel(selectedRow);
                        selectedProduct = getProductList(WestminsterShoppingManager.productList.getProductList(), dropDownOption).get(modelRow);
                        updateDisplayPanel(selectedProduct);
                    }
                }
            }
        });

        // Add an action listener to the shopping cart button to open the shopping cart frame
        shoppingCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ShoppingCartFrame().updateInformation();
            }
        });

        // Add an action listener to the "Add to Shopping Cart" button to add the selected product to the shopping cart
        addToCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Check if a product is selected
                if (selectedProduct != null) {
                    boolean productExists = false;
                    // Check if the selected product already exists in the shopping cart
                    for (Product cartProduct : usersShoppingCart.getProductList()) {
                        // If it exists, update the quantity
                        if (cartProduct.getProductId().equals(selectedProduct.getProductId())) {
                            int newQuantity = cartProduct.getNumberOfAvailableItems() + 1;
                            cartProduct.setNoOfItems(newQuantity);
                            productExists = true;
                            break;
                        }
                    }

                    // If the product doesn't exist in the cart, add it with a quantity of 1
                    if (!productExists) {
                        selectedProduct.setNoOfItems(1);
                        usersShoppingCart.addProduct(selectedProduct);
                    }

                    try {
                        // Update the shopping cart information and table model in the ShoppingCartFrame
                        ShoppingCartFrame.updateInformation();
                        ShoppingCartFrame.updateTableModel();
                    } catch (NullPointerException ignored) { // Catch the NullPointerException thrown when the shopping cart frame is not open
                        new ShoppingCartFrame().updateInformation();
                    }
                }
            }
        });

        // Add components to the bottom panel
        bottomPanel1.add(bottomPanelText);
        bottomPanel1.add(idLabel);
        bottomPanel1.add(nameLabel);
        bottomPanel1.add(categoryLabel);
        bottomPanel1.add(extraLabel1);
        bottomPanel1.add(extraLabel2);
        bottomPanel1.add(availItemsLabel);

        bottomPanel.add(bottomPanel1);
        bottomPanel.add(bottomPanel2);

        // Add top and bottom panels to the JFrame
        add(topPanel);
        add(bottomPanel);

        setVisible(true);
    }


    // Method to create a JTable displaying product information
    public static JTable createTable(ArrayList<Product> productList) {
        // Define column names for the table
        String[] columnNames = {"Product ID", "Name", "Category", "Price (£)", "Info"};
        // Create a DefaultTableModel with the specified column names
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        // Iterate through the product list
        for (Product product : productList) {
            Object[] rowData;

            // Check if the item availability is less than 3 and set the color to red
            if (product.getNumberOfAvailableItems() < 3) { //referred from stackoverflow
                rowData = new Object[]{
                        "<html><font color='red'>" + product.getProductId() + "</font></html>",
                        "<html><font color='red'>" + product.getProductName() + "</font></html>",
                        "<html><font color='red'>" + product.getProductType() + "</font></html>",
                        "<html><font color='red'>" + product.getPrice() + "</font></html>",
                        "<html><font color='red'>" + product.displayProducts() + "</font></html>"
                };
            } else { //
                rowData = new Object[]{
                        product.getProductId(),
                        product.getProductName(),
                        product.getProductType(),
                        product.getPrice(),
                        product.displayProducts()
                };
            }
            // Add the row data to the table model
            model.addRow(rowData);
        }
        // Create a JTable using the defined model
        JTable table = new JTable(model);
        // Set the table to be non-editable
        table.setDefaultEditor(Object.class, null);
        return table;
    }

    /**
     * Updates the table model based on the selected product category and applies special formatting for low availability.
     */
    private void updateTableModel() {
        // Retrieve the table model
        DefaultTableModel model = (DefaultTableModel) table.getModel();

        // Clear the existing rows from the table
        model.setRowCount(0);

        // Get the list of products based on the selected category
        ArrayList<Product> productList = getProductList(WestminsterShoppingManager.productList.getProductList(), dropDownOption);

        // Iterate through the product list
        for (Product product : productList) {
            Object[] rowData;

            if (product.getNumberOfAvailableItems() < 3) {
                // Check if the item availability is less than 3 and set the color to red
                rowData = new Object[]{ //referred from stackoverflow
                        "<html><font color='red'>" + product.getProductId() + "</font></html>",
                        "<html><font color='red'>" + product.getProductName() + "</font></html>",
                        "<html><font color='red'>" + product.getProductType() + "</font></html>",
                        "<html><font color='red'>" + product.getPrice() + "</font></html>",
                        "<html><font color='red'>" + product.displayProducts() + "</font></html>"
                };
            } else {
                rowData = new Object[]{
                        product.getProductId(),
                        product.getProductName(),
                        product.getProductType(),
                        product.getPrice(),
                        product.displayProducts()
                };
            }

            model.addRow(rowData);
        }
    }

/**
 * Updates the display panel with information about the selected product.
 */
    public static void updateDisplayPanel(Product product){

        // Set the text of the labels to display product information
        idLabel.setText("Product ID : "+ product.getProductId());
        nameLabel.setText("Name : "+ product.getProductName());
        categoryLabel.setText("Category : "+ product.getProductType());
        availItemsLabel.setText("Items available : "+ product.getNumberOfAvailableItems());

        // Use Switch to display additional information according to the product type
        switch (product.getProductType()){
            case "Electronics":
                extraLabel1.setText("Brand : "+ product.getBrand());
                extraLabel2.setText("Warranty Period : "+ product.getWarrantyPeriod());
                break;
            case "Clothing":
                extraLabel1.setText("Size : "+ product.getSize());
                extraLabel2.setText("Colour : "+ product.getColour());
                break;
        }

    }

/**
 * Filters the given list of products based on the provided dropdown option.
 * */
    public static ArrayList<Product> getProductList(ArrayList<Product> productList, String dropdownOption){
        ArrayList<Product> selectedProductList = new ArrayList<>();
        switch (dropdownOption){
            case "All":
                selectedProductList = productList;
                break;
            case "Electronic":
                // If the option is "Electronic", filter and add only electronic products
                for (Product product: productList){
                    if (product.getProductType().equals("Electronics")){
                       selectedProductList.add(product);
                    }
                }
                break;
            case "Clothing":
                // If the option is "Clothing", filter and add only clothing products
                for (Product product: productList){
                    if (product.getProductType().equals("Clothing")){
                        selectedProductList.add(product);
                    }
                }
                break;
        }
        return selectedProductList;
    }
}
