import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;

public class WestminsterShoppingManager implements ShoppingManager {
    private final Map<String, List<Product>> productMap;
    private static final int numObject = 50;
    private static final String AllFilePath = "All Products.txt";

    public WestminsterShoppingManager() {
        productMap = new HashMap<>();
        productMap.put("Electronics", new ArrayList<>());
        productMap.put("Clothing", new ArrayList<>());
        productMap.put("All", new ArrayList<>());
    }
    @Override
    public boolean runMenu() {
        boolean exit = false;

        Scanner scanner01 = new Scanner(System.in);

        do {
            System.out.println("To add new products, Press 01: ");
            System.out.println("To remove products, Press 02: ");
            System.out.println("To get the list of Existing products, Press 03: ");
            System.out.println("To save all products to the File, Press 04: ");
            System.out.println("To read all products, Press 05");
            System.out.println("To exit, Press 06");

            while (!scanner01.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number.");
                scanner01.next(); // consume the invalid input
            }
            int choice = scanner01.nextInt();

            switch (choice) {
                case 1:

                    do {
                        System.out.println("To add new Electronic Product, Press 01");
                        System.out.println("To add new Clothing Product, Press 02");

                        while (!scanner01.hasNextInt()) {
                            System.out.println("Invalid Input. Enter 1 or 2:");
                            scanner01.next();
                        }

                        int choice2 = scanner01.nextInt();

                        if (choice2 == 1) {
                            try {
                                System.out.println("Insert the Product Id:");
                                String productID = scanner01.next();
                                System.out.println("Insert the Product Name:");
                                String productName = scanner01.next();
                                System.out.println("Insert the Number of items:");
                                int noOfItems = getValidIntInput(scanner01);
                                System.out.println("Insert the price: ");
                                double price = getValidDoubleInput(scanner01);
                                System.out.println("Insert the Manufacturer: ");
                                String manufacturer = scanner01.next();
                                System.out.println("Insert the Brand: ");
                                String brand = scanner01.next();
                                System.out.println("Insert warranty Period:");
                                String warranty = scanner01.next();
                                String productType = "Electronics";
                                Electronics electronics = new Electronics(productID, productName, noOfItems, price, manufacturer, brand, warranty, productType);
                                electronics.setProductType("Electronics");
                                this.addElectronicProduct(electronics);
                                System.out.println("Electronic product is added.");
                            } catch (java.util.InputMismatchException e) {
                                System.out.println("Invalid input. Please enter the correct data type.");
                                scanner01.nextLine();
                            }
                            break;
                        } else if (choice2 == 2) {
                            try {
                                System.out.println("Insert the Product Id:");
                                String productID = scanner01.next();
                                System.out.println("Insert the Product Name:");
                                String productName = scanner01.next();
                                System.out.println("Insert the Number of items:");
                                int noOfItems = getValidIntInput(scanner01);
                                System.out.println("Insert the price: ");
                                double price = getValidDoubleInput(scanner01);
                                System.out.println("Insert the Manufacturer: ");
                                String manufacturer = scanner01.next();
                                System.out.println("Insert the Size: ");
                                String size = scanner01.next();
                                System.out.println("Insert the color: ");
                                String color = scanner01.next();
                                String productType = "Clothing";
                                Clothing clothing = new Clothing(productID, productName, noOfItems, price, manufacturer, size, color, productType);
                                this.addClothingProduct(clothing);
                                clothing.setProductType("Clothing");
                                System.out.println("Clothing product is added.");
                            } catch (java.util.InputMismatchException e) {
                                System.out.println("Invalid input. Please enter the correct data type.");
                                scanner01.nextLine();
                            }
                        }
                        break;


                    } while (true);
                    break;

                case 2:
                    System.out.println("Enter 1 if you want to remove Electronic item or Enter 2 If you want to remove clothing item");

                    while (!scanner01.hasNextInt()) {
                        System.out.println("Invalid Input. Enter 1 or 2:");
                        scanner01.next();
                    }

                    int choice3 = scanner01.nextInt();

                    if (choice3 == 1) {
                        System.out.println("Enter the Product ID to remove Electronic item:");
                        String electronicProductID = scanner01.next();
                        removeElectronicProduct(electronicProductID);
                        System.out.println("Number of existing products"+" "+productMap.get("All").size());

                    } else if (choice3 == 2) {
                        System.out.println("Enter the Product ID to remove Clothing item:");
                        String clothingProductID = scanner01.next();
                        removeClothingProduct(clothingProductID);
                        System.out.println("Number of existing products:"+" "+productMap.get("All").size());
                    } else if (choice3 == 2) {

                    } else {
                        System.out.println("Invalid choice. Please enter 1 or 2.");
                    }

                case 3:

                    sortingItems(productMap.get("All"));
                    for (Product item : productMap.get("All")) {
                        System.out.println(item);
                    }


                    break;
                case 4:
                    saveToFile(productMap.get("All"));
                    System.out.println("Saved to the Files.");
                    break;
                case 5:

                    loadAllFromFile("All Products.txt");
                    System.out.println("Loaded From Files.");
                    break;
                case 6:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }

        }while (!exit);

        return exit;
    }
    @Override
    public void addElectronicProduct(Electronics product) {
        addProduct(product, "Electronics");
    }

    @Override
    public void addClothingProduct(Clothing product) {
        addProduct(product, "Clothing");
    }

    private void addProduct(Product product, String productType) {
        List<Product> productsOfType = productMap.get(productType);
        if (productsOfType != null && productsOfType.size() < numObject) {
            productsOfType.add(product);
            productMap.get("All").add(product);
        } else {
            System.out.println("No more space for a new product of type: " + productType);
        }
    }

    @Override
    public void removeElectronicProduct(String productID) {
        removeProduct(productID, "Electronics", AllFilePath);

    }

    @Override
    public void removeClothingProduct(String productID) {
        removeProduct(productID, "Clothing", AllFilePath);

    }


    private void removeProduct(String productID, String productType, String filePath) {
        List<Product> productsOfType = productMap.get(productType);

        Iterator<Product> iterator = productsOfType.iterator();
        while (iterator.hasNext()) {
            Product product = iterator.next();
            if (product.getProductID().equals(productID)) {
                iterator.remove();
                productMap.get("All").remove(product);
                System.out.println("The product with ID " + productID + " is removed.");

                // Remove the product from the file
                removeProductFromFile(productID, filePath);

                return;
            }
        }
        System.out.println("The product with ID " + productID + " is not found.");
    }


    private void removeProductFromFile(String productID, String filePath) {
        File originalFile = new File(filePath);
        File tempFile = new File(filePath + ".tmp");

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath));
             BufferedWriter writer = new BufferedWriter(new FileWriter(filePath + ".tmp"))) {

            String line;
            while ((line = reader.readLine()) != null) {
                // Assuming each line has the format "Product ID=value,Product Name=value,..."
                String[] keyValuePairs = line.split(",");
                Map<String, String> productData = new HashMap<>();

                // Extract key-value pairs from the line
                for (String pair : keyValuePairs) {
                    String[] entry = pair.split("=");
                    if (entry.length == 2) {
                        productData.put(entry[0].trim(), entry[1].trim());
                    }
                }

                // Extract the product ID from the data
                String existingProductID = productData.get("Product ID");

                // Omit the line with the matching product ID
                if (!existingProductID.equals(productID.trim())) {
                    writer.write(line + System.lineSeparator());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error reading or writing the file.");
        }

        try {
            // Attempt to move (rename) the temporary file to the original file
            Files.move(tempFile.toPath(), originalFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Product with ID " + productID + " removed from the file.");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to remove the product with ID " + productID + " from the file.");

            // Delete the temporary file if renaming fails
            if (!tempFile.delete()) {
                System.out.println("Failed to delete the temporary file.");
            }
        }
    }





    @Override
    public void saveToFile(List<Product> allItems) {
        saveProductsToFile(productMap.get("All"), AllFilePath);
    }



    private void saveProductsToFile(List<? extends Product> items, String filePath) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath,true))) {
            for (Product product : items) {
                writer.println(product.toFileString()); // Use a method toFileString in Product class
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public List<Product> loadAllFromFile(String filePath) {
        List<Product> productItems = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(new File(filePath)))) {
            String line;

            while ((line = reader.readLine()) != null) {
                // Assuming the format is key-value pairs separated by commas
                String[] keyValuePairs = line.split(",");

                String productId = getValue(keyValuePairs, "Product ID");
                String productName = getValue(keyValuePairs, "Product Name");
                int noOfAvailableItems = parseInteger(getValue(keyValuePairs, "Number of available Items"));
                double price = parseDouble(getValue(keyValuePairs, "Price"));
                String manufacturer = getValue(keyValuePairs, "Manufacturer");
                String productType = getValue(keyValuePairs, "Product Type");

                Product productItem;

                if ("Electronics".equals(productType)) {
                    String warrantyPeriod = getValue(keyValuePairs, "Warrenty Period");
                    String brand = getValue(keyValuePairs, "Brand");
                    productItem = new Electronics(productId, productName, noOfAvailableItems, price, manufacturer, brand, warrantyPeriod, productType);
                    this.addElectronicProduct((Electronics) productItem);
                } else if ("Clothing".equals(productType)) {
                    String color = getValue(keyValuePairs, "Color");
                    String size = getValue(keyValuePairs, "Size");
                    productItem = new Clothing(productId, productName, noOfAvailableItems, price, manufacturer, size, color, productType);
                    this.addClothingProduct((Clothing) productItem);
                } else {
                    throw new IllegalArgumentException("Unsupported product type: " + productType);
                }

                productItems.add(productItem);

            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + filePath);
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Error reading file: " + filePath);
            e.printStackTrace();
        }

        return productItems;
    }

    private String getValue(String[] keyValuePairs, String key) {
        for (String pair : keyValuePairs) {
            String[] entry = pair.split("=");
            if (entry.length == 2 && entry[0].trim().equals(key)) {
                return entry[1].trim();
            }
        }
        return ""; // Return an empty string if the key is not found
    }


    // Use this method for parsing integers with a default value of 0
    private int parseInteger(String value) {
        return value.isEmpty() ? 0 : Integer.parseInt(value);
    }

    // Use this method for parsing doubles with a default value of 0.0
    private double parseDouble(String value) {
        return value.isEmpty() ? 0.0 : Double.parseDouble(value);
    }



    public void sortingItems(List<Product> items) {
        items.sort(Comparator.comparing(Product::getProductID));
    }

    int getValidIntInput(Scanner scanner01) {
        int input; // Initialize with an invalid value

        while (true) {
            try {
                input = scanner01.nextInt();

                // Validate that the input is non-negative
                if (input >= 0) {
                    break; // Break out of the loop if the input is valid
                } else {
                    System.out.println("Please enter a non-negative integer.");
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a valid integer.");
                scanner01.next(); // Consume the invalid input to prevent an infinite loop
            }
        }

        return input;
    }
    private double getValidDoubleInput(Scanner scanner01) {
        double input = -1; // Initialize with an invalid value

        while (true) {
            try {
                input = scanner01.nextDouble();

                // Validate that the input is positive
                if (input > 0) {
                    break; // Break out of the loop if the input is valid
                } else {
                    System.out.println("Please enter a positive double.");
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a valid double.");
                scanner01.next(); // Consume the invalid input to prevent an infinite loop
            }
        }

        return input;
    }
}



