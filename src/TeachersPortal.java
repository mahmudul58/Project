import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class TeachersPortal {
    private final Connection connection; // connection -> interface
    private final Scanner scanner;

    public TeachersPortal(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    public void menue() {
        while (true) {
            System.out.println("\nWelcome to Teacher's Portal.");
            System.out.println("1. Login \n2. Register\n3. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1 -> TeachersLogin();
                case 2 -> Registration();
                case 3 -> {
                    System.out.println("\nBye!\n");
                    return;
                }
                default -> System.out.println("Enter a valid choice.");
            }
        }
    }

    // Registration Section
    public void Registration() {
        System.out.print("\nName : ");
        String name = scanner.nextLine();

        System.out.print("Email : ");
        String email = scanner.nextLine();

        System.out.print("Password : ");
        String pass = scanner.nextLine();

        System.out.print("Course Title : ");
        String courseTitle = scanner.nextLine();

        System.out.print("Coursse Code : ");
        String courseCode = scanner.nextLine();

        try {
            String query = "INSERT INTO teacher(name,email,password,courseTitle,courseCode) VALUES(?,?,?,?,?)";
            PreparedStatement regState = connection.prepareStatement(query);
            regState.setString(1, name);
            regState.setString(2, email);
            regState.setString(3, pass);
            regState.setString(4, courseTitle);
            regState.setString(5, courseCode);
            int rowsAffect = regState.executeUpdate();
            if (rowsAffect > 0) {
                System.out.println("\nRegistered Successfully.");
            } else {
                System.out.println("\nRegistration failed.");
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /// Login Section
    public void TeachersLogin() {
        System.out.print("\nEmail : ");
        String email = scanner.nextLine();

        System.out.print("Password : ");
        String pass = scanner.nextLine();

        try {
            ExamPanel examPanel = new ExamPanel(connection, scanner);
            String query = "SELECT * FROM teacher where email = ? and password = ?";
            PreparedStatement loginState = connection.prepareStatement(query);
            loginState.setString(1, email);
            loginState.setString(2, pass);
            ResultSet result = loginState.executeQuery();
            if (result.next()) {
                String getName = result.getString("name");
                String courseTitle = result.getString("courseTitle");
                String courseCode = result.getString("courseCode");
                System.out.println("\nWelcome " + getName + " !");
                examPanel.TeacherMenu(courseTitle, courseCode);
            } else {
                System.out.println("\nInavlid Email or Password.");
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}