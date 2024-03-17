import java.io.*;
import java.util.HashMap;

public class  User implements Serializable { // The class implements the Serializable interface, allowing objects of this class to be serialized.
    public static Boolean isNewUser;
    private String username;
    private String password;


    public User(String username, String password, boolean isNewUser) {
        this.username = username;
        this.password = password;
        this.isNewUser = isNewUser;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean isNewUser() {
        return isNewUser;
    }

    public void setNewUser(boolean isNewUser) {
        this.isNewUser = isNewUser;
    }

    public static HashMap<String, User> loadCredentialsFromFile() { // Static method to load user credentials from a file and return them as a HashMap.
        HashMap<String, User> userCredentials = new HashMap<>();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("user_credentials.ser"))) {
            // Read the serialized HashMap from the file.
            userCredentials = (HashMap<String, User>) ois.readObject();

            // When loading existing users, set isNewUser to false for each user.
            for (User user : userCredentials.values()) {
                user.setNewUser(false);
            }
        } catch (IOException | ClassNotFoundException e) {
            // Handle exceptions related to file not found or deserialization errors. If an empty HashMap will be returned.
        }

        return userCredentials;
    }

    public static void saveCredentialsToFile(HashMap<String, User> userCredentials) { // Static method to save user credentials to a file.
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("user_credentials.ser"))) {
            // Write the HashMap of user credentials to the file.
            oos.writeObject(userCredentials);
        } catch (IOException e) {
            // Handle exceptions related to file writing. Print the stack trace for debugging purposes.
            e.printStackTrace();
        }
    }
}
