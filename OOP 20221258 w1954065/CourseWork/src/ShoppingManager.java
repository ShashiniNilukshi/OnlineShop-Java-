import java.util.List;

public interface ShoppingManager {

public abstract void addElectronicProduct(Electronics product);

public abstract void  removeElectronicProduct( String productID);

public abstract  void sortingItems(List<Product> items);

public abstract boolean runMenu();

public abstract void saveToFile(List<Product> allItems);

public abstract List<Product> loadAllFromFile(String AllFilePath);

public abstract void addClothingProduct(Clothing product);

public abstract void removeClothingProduct(String productID);
}
