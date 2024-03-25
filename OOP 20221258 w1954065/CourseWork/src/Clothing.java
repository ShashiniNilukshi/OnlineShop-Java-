public class Clothing extends Product {
    private String size;
    private String color;


    public Clothing(String size, String color) {
        this.color = color;
        this.size = size;
    }

    public Clothing(String productID, String productName, int noOfAvailableItems, double price, String manufacturer, String size, String color, String productType) {
        super(productID, productName, noOfAvailableItems, price, manufacturer, productType);
        this.color = color;
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }


    public String toString() {
        return "Product ID = " + getProductID() + "," + "Product Name = " + getProductName() + "," + "Number of available Items = "
                + getNoOfAvailableItems() + "," + "Price = " + getPrice() + "," + "Manufacturer = " +
                getManufacturer() + "," + "Product Type =" + getProductType()+","+"color = "+getColor()+","+"Size = "+getColor();
    }

    @Override
    public String toFileString() {
        return super.toFileString() +
                String.format(",Color = %s,Size = %s", getColor(), getSize());
    }

}