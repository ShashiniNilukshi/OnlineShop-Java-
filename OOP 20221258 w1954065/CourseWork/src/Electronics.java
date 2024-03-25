public class Electronics extends Product {
    private String brand;
    private String WarrentyPeriod;

    public Electronics(String brand, String WarrentyPeriod) {
        // Update the constructor parameters to match the order in the constructor call
        super("", "", 0, 0.0, "", ""); // Provide default values for the superclass constructor
        this.brand = brand;
        this.WarrentyPeriod = WarrentyPeriod;
    }

    public Electronics(String productID, String productName, int noOfAvailableItems, double price, String manufacturer, String brand, String WarrentyPeriod, String productType) {
        super(productID, productName, noOfAvailableItems, price, manufacturer, productType);
        this.WarrentyPeriod = WarrentyPeriod;
        this.brand = brand;


    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getWarrantyPeriod() {
        return WarrentyPeriod;
    }


    public void setWarrantyPeriod(String WarrentyPeriod) {
        this.WarrentyPeriod = WarrentyPeriod;
    }

    public String toString() {
        return "Product ID = " + getProductID() + "," + "Product Name = " + getProductName() + "," + "Number of available Items = "
                + getNoOfAvailableItems() + "," + "Price = " + getPrice() + "," + "Manufacturer = " + getManufacturer() + "," + "Product Type ="
                + getProductType() + "," + "Warrenty Period = " + getWarrantyPeriod() + "," + "Brand = " + getBrand();
    }

    @Override
    public String toFileString() {
        return super.toFileString() +
                String.format(",Warrenty Period = %s,Brand = %s", getWarrantyPeriod(), getBrand());
    }
}