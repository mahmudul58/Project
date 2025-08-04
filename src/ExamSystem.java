import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class ExamSystem {
    private static final String URL = "jdbc:mysql://localhost:3306/myProject";
    private static final String USER = "root";
    private static final String PASSWORD = "mysql_password";

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }

        Scanner scanner = new Scanner(System.in);

        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            TeachersPortal teachersPortal = new TeachersPortal(connection, scanner);
            StudentsPortal studentsPortal = new StudentsPortal(connection, scanner);
            while (true) {
                System.out.println("Welcome to our Exam System");
                System.out.println("1. Teacher's Portal");
                System.out.println("2. Student's Portal");
                System.out.println("3. Exit");
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();
                switch (choice) {
                    case 1 -> teachersPortal.menue();
                    case 2 -> studentsPortal.menue();
                    case 3 -> {
                        System.out.println("\nBye!\n");
                        return;
                    }
                    default -> System.out.println("Enter a valid choice.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

}
