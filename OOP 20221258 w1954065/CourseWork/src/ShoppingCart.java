import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {
    private ArrayList<Product> productList;
   double totalCost;

    public ShoppingCart() {
        this.productList = new ArrayList<>();
        this.totalCost = 0.0;
    }

    public void addToCart(Product product) {
        productList.add(product);
    }

    public void removeProduct(Product product) {
        productList.remove(product);
    }

    public double calculateTotalCost() {
        totalCost = 0.0;
        for (Product product : productList) {
            totalCost += product.getPrice();
        }
        return totalCost;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public List<Product> getShoppingCart() {
        return productList;
    }
}
