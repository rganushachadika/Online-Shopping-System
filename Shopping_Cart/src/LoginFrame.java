import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class  LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton createAccountButton;

    private HashMap<String, User> userCredentials;

    // HashMap to store user credentials
    public LoginFrame(HashMap<String, User> userCredentials) {
        // Initialize the userCredentials field with the provided HashMap
        this.userCredentials = userCredentials;

        setTitle("Login Menu"); // Set the title of the JFrame
        setSize(300, 200); // Set the size of the JFrame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Set the default close operation
        setLocationRelativeTo(null);

        // Create JLabels for username and password
        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");

        // Create JTextFields for entering username and password
        usernameField = new JTextField(15);
        passwordField = new JPasswordField(15);

        // Create JButtons for login and creating a new account
        loginButton = new JButton("Login");
        createAccountButton = new JButton("Create New Account");

        // ActionListener for login button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginUser(); // Calls the loginUser method when the login button is clicked
            }
        });

        // ActionListener for create account button
        createAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createAccount(); // Calls the createAccount method when the create account button is clicked
            }
        });

        // Create GroupLayout for layout management
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        // Horizontal grouping
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(usernameLabel)
                                .addComponent(passwordLabel))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(usernameField)
                                .addComponent(passwordField)
                                .addComponent(loginButton)
                                .addComponent(createAccountButton)))
        );

        // Vertical grouping
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(usernameLabel)
                        .addComponent(usernameField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(passwordLabel)
                        .addComponent(passwordField))
                .addComponent(loginButton)
                .addComponent(createAccountButton)
        );
    }

    private void createAccount() {  // Method to create a new user account
        String newUsername = JOptionPane.showInputDialog(this, "Enter a new username:");
        if (newUsername != null && !newUsername.trim().isEmpty()) {
            String newPassword = JOptionPane.showInputDialog(this, "Enter a new password:");
            if (newPassword != null) {
                // Hash the new password and create a new user
                String hashedPassword = hashPassword(newPassword);
                User newUser = new User(newUsername, hashedPassword, true);

                // Add the new user to the userCredentials HashMap
                userCredentials.put(newUsername, newUser);

                // Save the user credentials to the file
                User.saveCredentialsToFile(userCredentials);

                JOptionPane.showMessageDialog(this, "A New Account created successfully!");
                dispose(); // Close the login frame
                WestminsterFrame();
            }
        }
    }

    private void loginUser() { // Method to log in a user
        String username = usernameField.getText(); // Get the username and password from the text fields
        String password = new String(passwordField.getPassword());

        if (userCredentials.containsKey(username)) { // Check if the entered username exists in the user credentials HashMap
            String hashedPassword = userCredentials.get(username).getPassword(); // Retrieve the hashed password associated with the username

            if (hashPassword(password).equals(hashedPassword)) { // Compare the hashed entered password with the stored hashed password
                JOptionPane.showMessageDialog(this, "Login Successful!"); // Display a success message in a dialog box
                dispose(); // Close the login frame
                WestminsterFrame();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Password. Please try again."); // Display an error message for an incorrect password
            }
        } else {
            JOptionPane.showMessageDialog(this, "Invalid Username. Please try again."); // Display an error message for an invalid username
        }
    }

    private void WestminsterFrame() { // Method to open the WestminsterFrame
        WestminsterFrame westminsterFrame=new WestminsterFrame();
        westminsterFrame.setVisible(true);
        this.dispose(); // Close the LoginFrame
    }

    private String hashPassword(String password) { // Method to hash a password
        return String.valueOf(password.hashCode()); // Convert the hashed password to a string and return
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Load user credentials from file
            HashMap<String, User> userCredentials = User.loadCredentialsFromFile();

            // Create and display the login frame
            LoginFrame loginFrame = new LoginFrame(userCredentials);
            loginFrame.setVisible(true);

            // Add a window listener to save user credentials before closing the application
            loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            loginFrame.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                    User.saveCredentialsToFile(userCredentials);
                }
            });
        });
    }
}
