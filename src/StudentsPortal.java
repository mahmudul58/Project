import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class StudentsPortal {
    private final Connection connection; // connection -> interface
    private final Scanner scanner;

    public StudentsPortal(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    public void menue() {
        while (true) {
            System.out.println("\nWelcome to Student's Portal.");
            System.out.println("1. Login \n2. Register\n3. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1 -> StudentsLogin();
                case 2 -> Registration();
                case 3 -> {
                    System.out.println("Bye!\n");
                    return;
                }
                default -> System.out.println("Enter a valid choice.");
            }
        }
    }

    // Registration Section
    public void Registration() {
        System.out.print("\nName : ");
        String name = scanner.next();

        System.out.print("Student ID : ");
        String StudentID = scanner.next();

        System.out.print("Email : ");
        String email = scanner.next();

        System.out.print("Password : ");
        String pass = scanner.next();

        try {
            String query = "INSERT INTO student(name,StudentID,email,password) VALUES(?,?,?,?)";
            PreparedStatement regState = connection.prepareStatement(query);
            regState.setString(1, name);
            regState.setString(2, StudentID);
            regState.setString(3, email);
            regState.setString(4, pass);
            int rowsAffect = regState.executeUpdate();
            if (rowsAffect > 0) {
                System.out.println("\nRegistered Successfully.");
            } else {
                System.out.println("\nRegistration failed!");
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Login Section
    public void StudentsLogin() {
        System.out.print("\nEmail : ");
        String email = scanner.next();

        System.out.print("Password : ");
        String pass = scanner.next();

        try {
            String query = "SELECT name, email, studentID FROM student WHERE email = ? and password = ?";
            PreparedStatement loginState = connection.prepareStatement(query);
            loginState.setString(1, email);
            loginState.setString(2, pass);
            ResultSet result = loginState.executeQuery();
            if (result.next()) {
                String getName = result.getString("name");
                String getmail = result.getString("email");
                String getstudentID = result.getString("studentID");
                System.out.println("\nWelcome " + getName + " !");
                ExamPanel examPanel = new ExamPanel(connection, scanner);
                examPanel.StudentMenu(getmail,getstudentID);
            } else {
                System.out.println("\nInavlid Email or Password.");
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}