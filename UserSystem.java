import java.util.Scanner;

public class UserSystem {

    private final int MAX_USERS = 15;

    private String[] usernames = new String[MAX_USERS];
    private String[] passwords = new String[MAX_USERS];

    private String[] forbiddenWords = {
            "admin",
            "pass",
            "password",
            "qwerty",
            "ytrewq"
    };

    private int forbiddenCount = 5;

    private Scanner scanner = new Scanner(System.in);

    public void start() {

        while (true) {

            System.out.println("\nMenu");
            System.out.println("1 - Register user");
            System.out.println("2 - Delete user");
            System.out.println("3 - Authenticate");
            System.out.println("4 - Add forbidden word");
            System.out.println("0 - Exit");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {

                case 1:
                    registerUser();
                    break;

                case 2:
                    deleteUser();
                    break;

                case 3:
                    authenticateUser();
                    break;

                case 4:
                    addForbiddenWord();
                    break;

                case 0:
                    System.out.println("Program finished.");
                    return;

                default:
                    System.out.println("Wrong menu item.");
            }
        }
    }

    private void registerUser() {

        try {

            System.out.print("Enter username: ");
            String username = scanner.nextLine();

            validateUsername(username);

            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            validatePassword(password);

            int freeIndex = findFreePlace();

            if (freeIndex == -1) {
                throw new UserLimitException(
                        "Maximum number of users reached."
                );
            }

            usernames[freeIndex] = username;
            passwords[freeIndex] = password;

            System.out.println("User registered.");

        } catch (InvalidUsernameException e) {
            System.out.println(e.getMessage());

        } catch (InvalidPasswordException e) {
            System.out.println(e.getMessage());

        } catch (UserLimitException e) {
            System.out.println(e.getMessage());
        }
    }

    private void deleteUser() {

        try {

            System.out.print("Enter username: ");
            String username = scanner.nextLine();

            int index = findUser(username);

            if (index == -1) {
                throw new UserNotFoundException(
                        "User not found."
                );
            }

            usernames[index] = null;
            passwords[index] = null;

            System.out.println("User deleted.");

        } catch (UserNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private void authenticateUser() {

        try {

            System.out.print("Username: ");
            String username = scanner.nextLine();

            System.out.print("Password: ");
            String password = scanner.nextLine();

            int index = findUser(username);

            if (index == -1) {
                throw new AuthenticationException(
                        "Wrong username or password."
                );
            }

            if (!passwords[index].equals(password)) {
                throw new AuthenticationException(
                        "Wrong username or password."
                );
            }

            System.out.println("Authentication successful.");

        } catch (AuthenticationException e) {
            System.out.println(e.getMessage());
        }
    }

    private void addForbiddenWord() {

        System.out.print("Enter forbidden word: ");
        String word = scanner.nextLine();

        String[] newArray =
                new String[forbiddenWords.length + 1];

        for (int i = 0; i < forbiddenWords.length; i++) {
            newArray[i] = forbiddenWords[i];
        }

        newArray[forbiddenWords.length] = word;

        forbiddenWords = newArray;
        forbiddenCount++;

        System.out.println("Forbidden word added.");
    }

    private void validateUsername(String username)
            throws InvalidUsernameException {

        if (username.length() < 5) {
            throw new InvalidUsernameException(
                    "Username must contain at least 5 characters."
            );
        }

        if (username.contains(" ")) {
            throw new InvalidUsernameException(
                    "Username cannot contain spaces."
            );
        }
    }

    private void validatePassword(String password)
            throws InvalidPasswordException {

        if (password.length() < 10) {
            throw new InvalidPasswordException(
                    "Password must contain at least 10 characters."
            );
        }

        if (password.contains(" ")) {
            throw new InvalidPasswordException(
                    "Password cannot contain spaces."
            );
        }

        int digitCount = 0;
        int specialCount = 0;

        for (int i = 0; i < password.length(); i++) {

            char ch = password.charAt(i);

            if (Character.isDigit(ch)) {
                digitCount++;
            }
            else if (!Character.isLetter(ch)) {
                specialCount++;
            }
        }

        if (digitCount < 3) {
            throw new InvalidPasswordException(
                    "Password must contain at least 3 digits."
            );
        }

        if (specialCount < 1) {
            throw new InvalidPasswordException(
                    "Password must contain at least 1 special character."
            );
        }

        String lower = password.toLowerCase();

        for (int i = 0; i < forbiddenWords.length; i++) {

            if (forbiddenWords[i] != null &&
                    lower.contains(forbiddenWords[i])) {

                throw new InvalidPasswordException(
                        "Password contains forbidden word: "
                                + forbiddenWords[i]
                );
            }
        }
    }

    private int findFreePlace() {

        for (int i = 0; i < MAX_USERS; i++) {

            if (usernames[i] == null) {
                return i;
            }
        }

        return -1;
    }

    private int findUser(String username) {

        for (int i = 0; i < MAX_USERS; i++) {

            if (usernames[i] != null &&
                    usernames[i].equals(username)) {

                return i;
            }
        }

        return -1;
    }
}
