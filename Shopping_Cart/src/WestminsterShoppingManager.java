import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;

public class WestminsterShoppingManager { // Main class representing the shopping manager application

    // ShoppingCart object to hold the list of products
    public static ShoppingCart productList = new ShoppingCart();
    private static Scanner input = new Scanner(System.in);


    public static void main(String[] args) {
        // Load products from a file when the program starts
        loadFile();
        while(true){
            try{
                while (true) { // Display menu options
                    System.out.println("1.Add Product");
                    System.out.println("2.Remove Product");
                    System.out.println("3.Display all Products");
                    System.out.println("4.Save");
                    System.out.println("5.GUI");
                    System.out.println("0.Exit");

                    // Get user input for menu selection
                    int number = input.nextInt();

                    switch (number) {

                        case 1: // Add a product to the shopping cart
                            addProduct();
                            break;
                        case 2: // Remove a product from the shopping cart
                            deleteProduct();
                            break;
                        case 3: // Display all the products in the shopping cart
                            displayProduct();
                            break;
                        case 4: // Save the products to a file
                            savefile();
                            break;
                        case 5: // Launch GUI for user login
                            SwingUtilities.invokeLater(() -> {
                                HashMap<String, User> userCredentials = User.loadCredentialsFromFile(); // Load user credentials from the file
                                // Create an instance of the LoginFrame and make it visible
                                LoginFrame loginFrame = new LoginFrame(userCredentials);
                                loginFrame.setVisible(true);
                            });
                            break;
                        case 0: // Exit the program
                            System.exit(0);
                            break;

                        default:
                            System.out.println("Invalid input");
                    }
                }
            }catch (NumberFormatException | InputMismatchException e){ // Catch any exceptions and display an error message
                System.out.println("Invalid input. Please enter a valid number.");
                input.nextLine(); // Clear the input buffer
            }
        }
    }


    private static void savefile() { // Method to save the products to a file
        try {
            FileWriter productFile = new FileWriter("savedProduct.txt");
            // Iterate through products and write details to the file
            for (int i = 0; i < productList.getProductList().size(); i++) {
                productFile.write(productList.getProductList().get(i).displayProducts() + "\n");
            }
            productFile.close();
            System.out.println("Products saved successfully in saveProducts.txt");
        } catch (IOException e) {
            System.out.println("An error occurred!");
        }
    }


    private static void deleteProduct() { // Method to remove a product from the shopping cart

        System.out.println("Choose the product type");
        System.out.println("1.Clothing");
        System.out.println("2.Electronics");

        int productType;
        while (true) { // Validate and ask for input until a valid integer (1 or 2) is entered
            try {
                productType = input.nextInt();
                if (productType == 1 || productType == 2) {
                    break;
                } else {
                    System.out.println("Invalid input. Please enter 1 or 2 for product type.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter 1 or 2 for product type.");
                input.nextLine(); // Clear the input buffer
            }
        }

        String productToDelete;
        while (true) { // Validate and ask for input until a valid productId is entered
            System.out.println("Enter Product ID : ");
            productToDelete = input.next();

            // Check if the productId starts with "C" or "E"
            if (productToDelete.startsWith("E") || productToDelete.startsWith("C")) {
                break; // Exit the loop if a valid productId is entered
            } else {
                System.out.println("Invalid input. Product ID must start with 'E' or 'C'.");
            }
        }

        boolean deletedItem = false;

        // Switch statement to handle different product types
        switch (productType) {
            case 1:
                // Iterate through products to find and remove the specified clothing product
                for (int i = 0; i < productList.getProductList().size(); i++) {
                    if (productToDelete.equals(productList.getProductList().get(i).getProductId()) && productList.getProductList().get(i).getProductType().equals("Clothing")) {
                        System.out.println(productList.getProductList().get(i).displayProducts());
                        productList.removeProduct(productList.getProductList().get(i));
                        deletedItem = true;
                    }
                }
                break;
            case 2:
                // Iterate through products to find and remove the specified electronics product
                for (int i = 0; i < productList.getProductList().size(); i++) {
                    if (productToDelete.equals(productList.getProductList().get(i).getProductId()) && productList.getProductList().get(i).getProductType().equals("Electronics")) {
                        System.out.println(productList.getProductList().get(i).displayProducts());
                        productList.removeProduct(productList.getProductList().get(i));
                        deletedItem = true;
                    }
                }
                break;
            default:
                System.out.println("invalid product type");
        }

        if (deletedItem) { // Display success message if the product was deleted
            System.out.println("Product deleted successfully!");
            System.out.println("Items Left in the Cart : " + productList.getProductList().size());
        } else {
            System.out.println("Product not found!");
        }
    }

    private static void addProduct() { // Method to add a product to the shopping cart

         if (productList.getProductList().size() <= 50) { // Allow adding products until the maximum limit (50) is reached
            try {
                String productId;
                while (true) { // Keep asking for input until a unique productId is entered
                    System.out.println("Enter the productId (must start with 'E' or 'C'): ");
                    productId = input.next();

                    // Check if the productId starts with 'E' or 'C'
                    if (!(productId.startsWith("E") || productId.startsWith("C"))) {
                        System.out.println("Invalid input. The productId must start with 'E' or 'C'.");
                        continue;
                    }

                    // Check if the productId is unique
                    boolean isUnique = true;
                    for (Product existingProduct : productList.getProductList()) {
                        if (existingProduct.getProductId().equals(productId)) {
                            System.out.println("Product with this productId already exists. Please enter a unique productId.");
                            isUnique = false;
                            break;
                        }
                    }

                    if (isUnique) {
                        break; // Exit the loop if a unique productId is entered
                    }
                }

                System.out.println("Enter the product Name: ");
                String productName = input.next();

                int numberOfAvailableItems;
                while (true) { // Validate and ask for input until a valid positive integer is entered
                    System.out.println("Enter the NoOfItems: ");
                    try {
                        numberOfAvailableItems = input.nextInt();
                        if (numberOfAvailableItems <= 0) {
                            System.out.println("Invalid input. Please enter a valid positive integer for NoOfItems.");
                            continue;
                        }
                        break;
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid input. Please enter a valid integer for NoOfItems.");
                        input.nextLine();
                    }
                }

                double price;
                while (true) { // Validate and ask for input until a valid price is entered
                    System.out.println("Enter the Price: ");
                    try {
                        price = input.nextDouble();
                        if (price <= 0) {
                            System.out.println("Invalid input. Please enter a valid positive integer for price.");
                            continue;
                        }
                        break;
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid input. Please enter a valid Price.");
                        input.nextLine();
                    }
                }

                System.out.println("Enter the product type: ");
                System.out.println("1.Clothing");
                System.out.println("2.Electronics");

                int productType;
                while (true) { // Validate and ask for input until a valid integer (1 or 2) is entered
                    try {
                        productType = input.nextInt();
                        if (productType == 1 || productType == 2) {
                            break;
                        } else {
                            System.out.println("Invalid input. Please enter 1 or 2 for product type.");
                        }
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid input. Please enter 1 or 2 for product type.");
                        input.nextLine();
                    }
                }

                if (productType == 1) {
                    System.out.println("Enter the size");
                    String size = input.next();
                    System.out.println("Enter the color");
                    String color = input.next();

                    // Create Clothing object and add to the shopping cart
                    Clothing clothing = new Clothing(productId, productName, numberOfAvailableItems, price, "Clothing", size, color);
                    productList.addProduct(clothing);
                    System.out.println("Product Added successfully!");

                } else if (productType == 2) {
                    System.out.println("Enter the brand");
                    String brand = input.next();
                    System.out.println("Enter the warranty Period in no.of months");

                    int warrantyPeriod;
                    while (true) { // Validate and ask for input until a valid integer is entered
                        try {
                            warrantyPeriod = input.nextInt();
                            break;
                        } catch (InputMismatchException e) {
                            System.out.println("Invalid input. Please enter a valid input for warranty period.");
                            input.nextLine(); // Clear the input buffer
                        }
                    }

                    // Create Electronics object and add to the shopping cart
                    Electronics electronics = new Electronics(productId, productName, numberOfAvailableItems, price, "Electronics", brand, warrantyPeriod);
                    productList.addProduct(electronics);
                    System.out.println("Product Added successfully!");
                } else {
                    System.out.println("Invalid product type");
                }

            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter valid data types.");
                input.nextLine(); // Clear the input buffer
            }
        } else {
            System.out.println("Maximum limit (50) of products reached. Cannot add more products.");
        }
    }


    private static void displayProduct() { // Method to display all products in the shopping cart

        if (productList.getProductList().isEmpty()) { // Check if the shopping cart is empty
            System.out.println("Product cart is Empty");
        } else { // Iterate through products and display details
            for (int i = 0; i < productList.getProductList().size(); i++) {
                System.out.println(productList.getProductList().get(i).displayProducts());
            }
        }
    }

    public static void loadFile() { // Method to load products from the file
        try {
            // Create a File object representing the "savedProduct.txt" file
            File saveFile = new File("savedProduct.txt");

            Scanner fileReader = new Scanner(saveFile);

            // Iterate through each line in the file
            while (fileReader.hasNext()){
                // Read a line of data from the file
                String dataLine = fileReader.nextLine();
                // Split the data using the "|" delimiter and store it in an array
                String[] dataArray = dataLine.split("\\|");

                // Check the length of the array to determine the product type
                if (dataArray.length == 7){
                    // Check the product type and create the corresponding object
                    if (dataArray[6].equals("Clothing")){
                        // Create a Clothing object using the data and add it to the productList
                        Clothing clothing = new Clothing(dataArray[0],dataArray[1],Integer.parseInt(dataArray[2]),Double.parseDouble(dataArray[3]),"Clothing",dataArray[4],dataArray[5]);
                        productList.addProduct(clothing);
                    } else if (dataArray[6].equals("Electronics")) {
                        // Create an Electronics object using the data and add it to the productList
                        Electronics electronics = new Electronics(dataArray[0],dataArray[1],Integer.parseInt(dataArray[2]),Double.parseDouble(dataArray[3]),"Electronics",dataArray[4],Integer.parseInt(dataArray[5]));
                        productList.addProduct(electronics);
                    }
                }else{ // Display error message if the file format is invalid
                    System.out.println("Invalid file format.");
                }
            }
            System.out.println("Loaded products from the saved File");
            fileReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
        }
    }
}