import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ShoppingCart {
    private ArrayList<Product> productList; // ArrayList to store all the products
    private double firstDiscountValue; // Value of the discount which given if the user is a new user
    private double threeItemDiscountValue; // Value of the discount which given if the user buys 3 or more items from the same category

    public ShoppingCart() {  // Constructor to initialize the shopping cart with an empty product list.
        this.productList = new ArrayList<>();
    }

    public void addProduct(Product product) { // Method to add a product to the shopping cart
        productList.add(product);
    }

    public void removeProduct(Product product) { // Method to remove a product from the shopping cart
        productList.remove(product);
    }

    public double totalCost() { // Method to calculate the total cost of all the products in the shopping cart
        double totalCost = 0;
        for (Product product : productList) {
            totalCost += product.getPrice() * product.getNumberOfAvailableItems();
        }
        return totalCost;
    }

    public double firstDiscount(boolean newAccount) {   // Method to calculate the value of the first discount
        if (newAccount) {
            firstDiscountValue = totalCost() * 0.1; // 10% discount for new users
        }
        return firstDiscountValue;
    }

    public double threeItemsDiscount() { // Method to calculate the value of the three items discount
        int electronicCount = getProductCount("Electronics");
        int clothingCount = getProductCount("Clothing");

        if (electronicCount + clothingCount >= 3) { // Check if the total count of Electronics or Clothing is greater than or equal to 3
            threeItemDiscountValue = totalCost() * 0.2; // 20% discount for purchasing three or more items.
        } else {
            threeItemDiscountValue = 0;
        }
        return threeItemDiscountValue;
    }

    public double finalTotalValue() { // Method to calculate the final total value after applying all the discounts
        double finalTotalValue = totalCost() - firstDiscountValue - threeItemDiscountValue;
        return finalTotalValue;
    }

    public ArrayList<Product> getProductList() { // Method to get the sorted list of products in the shopping cart.
        Collections.sort(productList, Comparator.comparing(Product::getProductName));
        return productList;
    }

    private int getProductCount(String category) { // Method to get the total count of products in a specified category.
        int count = 0;
        for (Product product : productList) {
            if (product.getProductType().equals(category)) {
                count += product.getNumberOfAvailableItems();
            }
        }
        return count;
    }
}
