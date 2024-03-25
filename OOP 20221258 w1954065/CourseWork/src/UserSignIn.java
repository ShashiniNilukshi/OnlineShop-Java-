import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class UserSignIn extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton signInButton;
    private JButton logInButton;

    private Map<String, User> userCredentials;
    private LoginService loginService;
    private User loggedInUser;

    public UserSignIn() {
        setTitle("Sign In / Log In Page");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 250);
        setLayout(new GridLayout(4, 2, 10, 10)); // Added some space between components

        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");

        usernameField = new JTextField();
        passwordField = new JPasswordField();

        signInButton = new JButton("Register");
        logInButton = new JButton("Log In");

        userCredentials = loadUserCredentials();
        loginService = new LoginService(userCredentials);

        // Add components to the layout with additional space
        add(usernameLabel);
        add(usernameField);
        add(passwordLabel);
        add(passwordField);
        add(signInButton);
        add(logInButton);




        signInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showLoginPanel();
            }
        });


        logInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = String.valueOf(passwordField.getPassword());

                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(UserSignIn.this, "Please enter a valid username and password.");
                    return;
                }

                loggedInUser = loginService.checkCredentials(username, password);

                if (loggedInUser != null) {
                    loggedInUser.addPurchase("SomeProductType");
                    JOptionPane.showMessageDialog(UserSignIn.this, "Log in successful!");
                    openOnlineShop();
                } else {
                    JOptionPane.showMessageDialog(UserSignIn.this, "Invalid credentials. Please try again.");
                }
            }
        });

        add(usernameLabel);
        add(usernameField);
        add(passwordLabel);
        add(passwordField);
        add(signInButton);
        add(logInButton);


        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void showLoginPanel() {
        // Create a new panel for entering username and password
        JPanel loginPanel = new JPanel(new GridLayout(3, 2));

        JLabel newUsernameLabel = new JLabel("New Username:");
        JLabel newPasswordLabel = new JLabel("New Password:");

        JTextField newUsernameField = new JTextField();
        JPasswordField newPasswordField = new JPasswordField();

        JButton registerButton = new JButton("Register");

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newUsername = newUsernameField.getText();
                String newPassword = String.valueOf(newPasswordField.getPassword());

                if (newUsername.isEmpty() || newPassword.isEmpty()) {
                    JOptionPane.showMessageDialog(UserSignIn.this, "Please enter a valid new username and password.");
                    return;
                }

                saveUserCredentials(new User(newUsername, newPassword));

                // Close the registration dialog after saving credentials
                loginPanel.disable();

                // Log in the user automatically after registration
                loggedInUser = new User(newUsername, newPassword);
                loggedInUser.addPurchase("SomeProductType");
                JOptionPane.showMessageDialog(UserSignIn.this, "Registration successful! Log in automatically.");
                openOnlineShop();
            }
        });

        loginPanel.add(newUsernameLabel);
        loginPanel.add(newUsernameField);
        loginPanel.add(newPasswordLabel);
        loginPanel.add(newPasswordField);
        loginPanel.add(new JLabel());
        loginPanel.add(registerButton);

        // Display the login panel in a new dialog
        JDialog loginDialog = new JDialog(UserSignIn.this, "Enter Username and Password", true);
        loginDialog.getContentPane().add(loginPanel);
        loginDialog.setSize(300, 150);
        loginDialog.setLocationRelativeTo(UserSignIn.this);
        loginDialog.setVisible(true);
    }


    private Map<String, User> loadUserCredentials() {
        Map<String, User> credentials = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("user_credentials.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");

                if (parts.length == 2) { // Ensure the line has the expected format
                    String username = parts[0];
                    String password = parts[1];
                    credentials.put(username, new User(username, password));
                }
            }
        } catch (FileNotFoundException e) {
            createEmptyCredentialsFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return credentials;
    }

    private void createEmptyCredentialsFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("user_credentials.txt"))) {
            // Create an empty file
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveUserCredentials(User user) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("user_credentials.txt", true))) {
            writer.write(user.getUserName() + ":" + user.getPassword());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openOnlineShop() {
        dispose();
        SwingUtilities.invokeLater(() -> new OnlineShop(loggedInUser));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UserSignIn());
    }

    class LoginService {
        private Map<String, User> userCredentials;

        public LoginService(Map<String, User> userCredentials) {
            this.userCredentials = userCredentials;
        }

        public User checkCredentials(String enteredUsername, String enteredPassword) {
            User user = userCredentials.get(enteredUsername.trim());

            if (user != null && user.getPassword().equals(enteredPassword.trim())) {
                return user;
            }

            return null;
        }

    }

}
