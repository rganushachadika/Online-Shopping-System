public abstract class Product implements Comparable<Product>{
    private  String productId;
    private  String  productName;
    private int numberOfAvailableItems;
    private  double price;
    private  String productType;

    public Product(String productId, String productName, int numberOfAvailableItems, double price, String productType){
        this.productId = productId;
        this.productName = productName;
        this.numberOfAvailableItems = numberOfAvailableItems;
        this.price = price;
        this.productType = productType;
    }
    public double getPrice(){
        return price;
    }
    public String getProductId(){
        return productId;
    }
    public String getProductType(){
        return productType;
    }
    public String getProductName(){
        return productName;
    }
    public int getNumberOfAvailableItems(){
        return numberOfAvailableItems;
    }

    public String getBrand(){
        return getBrand();
    }
    public int getWarrantyPeriod(){
        return getWarrantyPeriod();
    }
    public String getSize(){
        return getSize();
    }
    public String getColour(){
        return getColour();
    }
    abstract String displayProducts();

    public int compareTo(Product product){
        return productId.compareTo(product.productId);
    }

    public void setNoOfItems(int quantity) {    // Setter method to update the quantity of available items.
        this.numberOfAvailableItems = quantity;
    }
}
