import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class User {
    private String userName;
    private String password;
    private Map<String, Integer> purchaseHistory;
    private int purchaseCount;

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
        this.purchaseHistory = new HashMap<>();
        this.purchaseCount = 0;

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Map<String, Integer> getPurchaseHistory() {
        return purchaseHistory;
    }

    public void addPurchase(String productType) {
        // Increment the purchase count for the given product type
        purchaseHistory.put(productType, purchaseHistory.getOrDefault(productType, 0) + 1);
    }

    public int getPurchaseCount() {
        return purchaseCount;
    }



    public void incrementPurchaseCount() {
        purchaseCount++;
    }
    public void savePurchaseToFile() {
        String fileName =   "_purchase_history.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            writer.write( userName);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
   public boolean isUserInFile(String usernameToCheck) {
        String fileName = "_purchase_history.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Assuming each line in the file corresponds to a username
                // You might need to adjust this part based on your file format
                if (line.equals(usernameToCheck)) {
                    return true; // User found in the file
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false; // User not found in the file or an error occurred
    }
}
