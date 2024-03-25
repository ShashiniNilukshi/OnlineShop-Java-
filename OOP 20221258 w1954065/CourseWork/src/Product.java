public abstract class Product {
    protected String productID;
    protected String productName;
    protected int  noOfAvailableItems;
    protected double price;
    protected String manufacturer;
    protected String productType;

    public Product(){
        this.productType="";


    }

    public Product(String productID, String productName, int noOfAvailableItems, double price, String manufacturer, String productType) {
        this.productID = productID;
        this.productName = productName;
        this.noOfAvailableItems = noOfAvailableItems;
        this.price = price;
        this.manufacturer = manufacturer;
        this.productType = productType;
    }

    public String getProductID(){
        return productID;
    }

    public void setProductID(String productID){
        this.productID=productID;
    }

    public String getProductName(){
        return  productName;
    }

    public void setProductName(String productName){
        this.productName=productName;
    }

    public int getNoOfAvailableItems(){
        return  noOfAvailableItems;
    }

    public void setNoOfAvailableItems(int noOfAvailableItems){
        this.noOfAvailableItems=noOfAvailableItems;
    }

    public double getPrice(){
        return price;
    }
    public void setPrice(double price){
        this.price=price;
    }

    public String getManufacturer(){
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }


    public String getProductType(){
        return productType;
    }
    public  void setProductType(String productType){
        this.productType=productType;
    }
    public String toFileString() {
        // Customize the output based on your requirements
        return String.format("Product ID = %s,Product Name = %s,Number of available Items = %d,Price = %.2f,Manufacturer = %s,Product Type = %s",
                getProductID(), getProductName(), getNoOfAvailableItems(), getPrice(), getManufacturer(), getProductType());
    }


}

