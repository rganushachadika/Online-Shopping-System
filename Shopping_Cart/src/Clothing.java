public class Clothing extends Product {
    private String size;
    private String colour;

    public Clothing(String productId, String productName, int numberofavailableitems, double price, String productType, String size, String colour) {
        super(productId, productName, numberofavailableitems, price,productType);
        this.colour = colour;
        this.size = size;
    }

    public String getSize() {
        return size;
    }

    public String getColour() {
        return colour;
    }

    @Override
    public String displayProducts() {
        return (getProductId()+"|"+getProductName()+"|"+ getNumberOfAvailableItems()+"|"+getPrice()+"|"+getSize()+"|"+getColour()+"|"+getProductType());
    }
}
