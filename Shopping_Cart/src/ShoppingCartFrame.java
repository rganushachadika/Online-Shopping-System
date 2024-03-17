import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ShoppingCartFrame extends JFrame {
    private static JFrame shoppingCartFrame;
    private static JTable shoppingCartTable;
    private static JPanel detailPanel, detailPanel1, detailPanel2;
    private static JLabel totalLabelCart, finalTotalLabelCart, firstDiscountLabelCart, threeItemsDiscountLabelCart,
            totalValueLabel, finalTotalValueLabel, firstDiscountValueLabel, threeItemsDiscountValueLabel;
    private JButton removeItemButton; // Remove Items button

    public ShoppingCartFrame() { // Constructor to initialize the shopping cart frame

        // Initialization of the JFrame
        shoppingCartFrame = new JFrame("Shopping Cart");
        shoppingCartFrame.setSize(600, 500);
        shoppingCartFrame.setLayout(new GridLayout(2, 1));

        // Create the shopping cart table
        shoppingCartTable = createTable(WestminsterFrame.usersShoppingCart.getProductList());
        JScrollPane shoppingCartPane = new JScrollPane(shoppingCartTable);
        shoppingCartPane.setBorder(new EmptyBorder(28, 28, 7, 28));

        // Information panels for displaying total cost, discounts, and final total
        detailPanel = new JPanel(new GridLayout(1, 2));
        detailPanel1 = new JPanel();
        detailPanel2 = new JPanel();

        detailPanel1.setLayout(new BoxLayout(detailPanel1, BoxLayout.Y_AXIS));
        detailPanel2.setLayout(new BoxLayout(detailPanel2, BoxLayout.Y_AXIS));

        // Labels for displaying information
        totalLabelCart = new JLabel("Total: ");
        finalTotalLabelCart = new JLabel("Final Total: ");
        firstDiscountLabelCart = new JLabel("First Purchase Discount (10%)");
        threeItemsDiscountLabelCart = new JLabel("Three Items from the same Category Discount (20%)");
        totalValueLabel = new JLabel("99.99£ ");
        finalTotalValueLabel = new JLabel("88.88£");
        firstDiscountValueLabel = new JLabel("77.77£");
        threeItemsDiscountValueLabel = new JLabel("66.66£");

        // Add labels to detailPanel1 and detailPanel2
        detailPanel1.add(totalLabelCart);
        detailPanel1.add(firstDiscountLabelCart);
        detailPanel1.add(threeItemsDiscountLabelCart);
        detailPanel1.add(finalTotalLabelCart);
        detailPanel1.add(new JLabel("")); // Placeholder for spacing

        detailPanel2.add(totalValueLabel);
        detailPanel2.add(firstDiscountValueLabel);
        detailPanel2.add(threeItemsDiscountValueLabel);
        detailPanel2.add(finalTotalValueLabel);
        detailPanel2.add(new JLabel("")); // Placeholder for spacing

        // Add Remove Item button
        removeItemButton = new JButton("Remove from Cart");
        removeItemButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeSelectedItem();
            }
        });

        detailPanel1.add(removeItemButton); // Add the Remove Item button to the detailPanel1

        detailPanel.add(detailPanel1); // Add information panels to the main detailPanel
        detailPanel.add(detailPanel2);

        shoppingCartFrame.add(shoppingCartPane); // Add shopping cart table and detail panel to the shopping cart frame
        shoppingCartTable.getModel().addTableModelListener(e -> {
            int row = e.getFirstRow();
            int column = e.getColumn();

            if (column == 1) { // Check if the edited column is the Quantity column
                DefaultTableModel model1 = (DefaultTableModel) shoppingCartTable.getModel();
                Object quantity = model1.getValueAt(row, column);
                System.out.println(quantity.toString());

                // Get the relative Product object and update the quantity using the setter method
                Product product = WestminsterFrame.usersShoppingCart.getProductList().get(row);
                product.setNoOfItems(Integer.parseInt(quantity.toString()));
                updateTableModel();
                updateInformation();
            }
        });
        // Add the detail panel to the shopping cart frame
        shoppingCartFrame.add(detailPanel);
        shoppingCartFrame.setVisible(true); // Set the frame as visible
    }

    private void removeSelectedItem() { // Method to remove the selected item from the cart
        int selectedRow = shoppingCartTable.getSelectedRow();

        if (selectedRow != -1) {
            DefaultTableModel model = (DefaultTableModel) shoppingCartTable.getModel();
            Product selectedProduct = WestminsterFrame.usersShoppingCart.getProductList().get(selectedRow);

            // Update the quantity in the cart
            int newQuantity = selectedProduct.getNumberOfAvailableItems() - 1;
            if (newQuantity <= 0) {
                // If the quantity becomes zero or negative, remove the product from the cart
                WestminsterFrame.usersShoppingCart.removeProduct(selectedProduct);
            } else {
                // Update the quantity
                selectedProduct.setNoOfItems(newQuantity);
            }

            // Update the table model and information
            updateTableModel();
            updateInformation();
        } else {
            JOptionPane.showMessageDialog(this, "Select an item to remove from the cart.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static JTable createTable(ArrayList<Product> productList) {  // Method to create a JTable from a list of products

        String[] columnNames = {"Product", "Quantity", "Price (£)"}; // Define column names for the table

        // Create a DefaultTableModel with no initial data and the specified column name
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        // Loop through each product in the provided productList
        for (Product product : productList) {
            switch (product.getProductType()) { // Check the type of product using getProductType() and add the product to the table model
                case "Electronics":
                    // If it's Electronics, create a row of data for the JTable
                    Object[] rowDataElectronic = {
                            product.getProductId() + " " +
                                    product.getProductName() + " " +
                                    product.getBrand() + ", " + product.getWarrantyPeriod(),
                            product.getNumberOfAvailableItems(),
                            product.getPrice(),};
                    // Add the created row to the table model
                    model.addRow(rowDataElectronic);
                    break;
                case "Clothing":
                    // If it's Clothing, create a row of data for the JTable
                    Object[] rowDataClothing = {
                            product.getProductId() + " " +
                                    product.getProductName() + " " +
                                    product.getSize() + ", " + product.getColour(),
                            product.getNumberOfAvailableItems(),
                            product.getPrice() * product.getNumberOfAvailableItems(),
                    };
                    // Add the created row to the table model
                    model.addRow(rowDataClothing);
                    break;
            }
        }
        // Create a JTable using the generated DefaultTableModel
        JTable table = new JTable(model);
        return table;
    }

    public static void updateTableModel() { // Method to update the table model based on the current product list
        DefaultTableModel model = (DefaultTableModel) shoppingCartTable.getModel(); // Obtain the table model associated with the shoppingCartTable
        model.setRowCount(0); // Clear the existing rows in the table model

        // Retrieve the current list of products from the shopping cart
        ArrayList<Product> productList = WestminsterFrame.usersShoppingCart.getProductList();

        // Loop through each product in the list and add a corresponding row to the table model
        for (int i = 0; i < productList.size(); i++) {
            Product product = productList.get(i);
            switch (product.getProductType()) {
                case "Electronics":
                    // If it's Electronics, create a row of data for the JTable
                    Object[] rowDataElectronic = {
                            product.getProductId() + " " +
                                    product.getProductName() + " " +
                                    product.getBrand() + ", " + product.getWarrantyPeriod(),
                            product.getNumberOfAvailableItems(),
                            product.getPrice(),
                    };
                    // Add the created row to the table model
                    model.addRow(rowDataElectronic);
                    break;
                case "Clothing":
                    // If it's Clothing, create a row of data for the JTable
                    Object[] rowDataClothing = {
                            product.getProductId() + " " +
                                    product.getProductName() + " " +
                                    product.getSize() + ", " + product.getColour(),
                            product.getNumberOfAvailableItems(),
                            product.getPrice() * product.getNumberOfAvailableItems(),
                    };
                    // Add the created row to the table model
                    model.addRow(rowDataClothing);
                    break;
            }
        }
    }

    /**
     * Updates the information labels based on the current state of the shopping cart.
     * It sets the text of JLabels to reflect the total cost, first purchase discount,
     * three items discount, and final total of the shopping cart.
     */
    public static void updateInformation() {
        // Update total cost label
        totalValueLabel.setText(Double.toString(WestminsterFrame.usersShoppingCart.totalCost()));
        // Update first purchase discount label
        firstDiscountValueLabel.setText(Double.toString(WestminsterFrame.usersShoppingCart.firstDiscount(User.isNewUser)));
        // Update three items discount label
        threeItemsDiscountValueLabel.setText(Double.toString(WestminsterFrame.usersShoppingCart.threeItemsDiscount()));
        // Update final total label
        finalTotalValueLabel.setText(Double.toString(WestminsterFrame.usersShoppingCart.finalTotalValue()));
    }
}
