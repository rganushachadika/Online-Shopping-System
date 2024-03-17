public class Electronics extends Product{
    private String brand;
    private int warrantyperiod;

    public Electronics(String productId, String productName, int numberofavailableitems, double price, String productType, String brand, int warrentyperiod){
        super(productId,productName,numberofavailableitems,price,productType);
        this.brand =brand;
        this.warrantyperiod =warrentyperiod;
    }

    @Override
    public String getBrand() {
        return brand;
    }

    public int getWarrantyPeriod() {
        return warrantyperiod;
    }

    @Override
    public String displayProducts() {
        return (getProductId()+"|"+getProductName()+"|"+ getNumberOfAvailableItems()+"|"+getPrice()+"|"+getBrand()+"|"+getWarrantyPeriod()+"|"+getProductType());
    }

}
