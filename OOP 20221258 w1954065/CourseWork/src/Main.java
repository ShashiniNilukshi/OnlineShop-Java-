import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int userType;

        do {
            System.out.println("Select user type:");
            System.out.println("1. Shopping Manager");
            System.out.println("2. Customer");

            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next(); // consume the invalid input
            }

            userType = scanner.nextInt();

            switch (userType) {
                case 1:
                    // User is a shopping manager
                    ShoppingManager shoppingManager = new WestminsterShoppingManager();

                    boolean exit = false;

                    while (!exit) {
                        exit = shoppingManager.runMenu();
                    }
                    break;
                case 2:
                    // User is a regular customer
                    UserSignIn userSignIn = new UserSignIn();

                    break;
                default:
                    System.out.println("Invalid user type. Please enter 1 for Shopping Manager or 2 for Customer.");
            }
        } while (userType != 1 && userType != 2);

        // Close scanner if needed
        scanner.close();
    }
}
