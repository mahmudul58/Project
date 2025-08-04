import java.sql.*;
import java.util.*;

public class ExamPanel {
    private final Connection connection;
    private final Scanner scanner;

    public ExamPanel(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    public void TeacherMenu(String courseTitle, String courseCode) {
        while (true) {
            System.out.println("\n--- Exam Panel ---");
            System.out.println("1. Create Exam");
            System.out.println("2. Show Registered Students");
            System.out.println("3. Show Student's Result");
            System.out.println("4. Logout");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1 -> CreateExam(courseTitle, courseCode);
                case 2 -> RegisteredStudent();
                case 3 -> Result();
                case 4 -> {
                    System.out.println("\nBye!");
                    return;
                }
                default -> System.out.println("Enter a valid choice.");
            }
        }
    }

    public void StudentMenu(String email,String StudentId) {
        while (true) {
            System.out.println("\n--- Exam section ---");
            System.out.println("1. Take an exam");
            System.out.println("2. Show my result");
            System.out.println("3. Show everyone's result");
            System.out.println("4. Logout");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1 -> TakeExam(email,StudentId);
                case 2 -> myResult(StudentId);
                case 3 -> Result();
                case 4 -> {
                    System.out.println("Bye!");
                    return;
                }
                default -> System.out.println("Enter a valid choice.");
            }
        }
    }

    // Exam create section
    public void CreateExam(String courseTitle, String courseCode) {
        System.out.print("\nEnter Exam Title: ");
        String examTitle = scanner.nextLine();

        System.out.print("Enter Exam Code: ");
        String examId = scanner.nextLine();

        System.out.print("Duration (in minutes): ");
        int duration = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Number of Questions: ");
        int no = scanner.nextInt();
        scanner.nextLine();

        try {
            PreparedStatement examStmt = connection.prepareStatement("INSERT INTO exams (exam_id,examTitle,courseCode,duration) VALUES (?,?,?,?)");
            examStmt.setString(1, examId);
            examStmt.setString(2, examTitle);
            examStmt.setString(3, courseCode);
            examStmt.setInt(4, duration);
            examStmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error creating exam: " + e.getMessage());
             return;
        }

        int marks = 0;

        for (int i = 1; i <= no; i++) {
            System.out.print("\nQuestion " + i + ": ");
            String qn = scanner.nextLine();
            System.out.print("Option A: ");
            String opt1 = scanner.nextLine();
            System.out.print("Option B: ");
            String opt2 = scanner.nextLine();
            System.out.print("Option C: ");
            String opt3 = scanner.nextLine();
            System.out.print("Option D: ");
            String opt4 = scanner.nextLine();
            System.out.print("Correct Answer (A/B/C/D): ");
            String ans = scanner.nextLine().toUpperCase();
            System.out.print("Marks: ");
            int score = scanner.nextInt();
            scanner.nextLine();
            marks += score;
            try {
                PreparedStatement ps = connection.prepareStatement("INSERT INTO questions (exam_id,qn,opt1,opt2,opt3,opt4,ans,marks) VALUES (?,?,?,?,?,?,?,?)");
                ps.setString(1, examId);
                ps.setString(2, qn);
                ps.setString(3, opt1);
                ps.setString(4, opt2);
                ps.setString(5, opt3);
                ps.setString(6, opt4);
                ps.setString(7, ans);
                ps.setInt(8, score);
                int rowsAffect = ps.executeUpdate();
                if (rowsAffect > 0) {
                    System.out.println("\nQuestion " + i + " added Successfully.");
                } else {
                    System.out.println("\nAn error occoured.");
                }
            } catch (SQLException e) {
                System.out.println("Error adding question: " + e.getMessage());
            }
        }

        try {
            PreparedStatement examStmt = connection.prepareStatement("UPDATE exams SET marks = ? WHERE exam_id = ?");
            examStmt.setInt(1, marks);
            examStmt.setString(2, examId);
            examStmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error : " + e.getMessage());
        }
    }
// To show Registered students
    public void RegisteredStudent() {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM student");
            System.out.println("\n--- Registered Students ---");
            if (!rs.isBeforeFirst()) {
                System.out.println("\nNo student found.");
            } else {
                System.out.println("+------------+------------+--------------------+");
                System.out.println("|    name    |  studentID |        email       |");
                System.out.println("+------------+------------+--------------------+");
                while (rs.next()) {
                    String name = rs.getString("name");
                    String studentID = rs.getString("studentID");
                    String email = rs.getString("email");
                    System.out.printf("| %-11s| %-11s| %-19s|\n", name, studentID, email);
                    System.out.println("+------------+------------+--------------------+");
                    
                }
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void Result() {
        System.out.print("\nEnter Exam Code: ");
        String examId = scanner.nextLine();
        try {
            PreparedStatement examStmt = connection.prepareStatement("SELECT * FROM results WHERE exam_id = ?");
            examStmt.setString(1, examId);
            ResultSet rs = examStmt.executeQuery();
            System.out.println("\n--- Student Results ---");
            if (!rs.isBeforeFirst()) {
                System.out.println("\nNo result found.");
            } else {
                System.out.println("+------------+------------+--------------------+");
                System.out.println("|    name    |  studentID |        score       |");
                System.out.println("+------------+------------+--------------------+");
                while (rs.next()) {
                    String name = rs.getString("name");
                    String studentID = rs.getString("studentID");
                    String score = rs.getString("score");
                    System.out.printf("| %-11s| %-11s|          %-10s|\n", name,studentID,score);
                    System.out.println("+------------+------------+--------------------+");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void myResult(String StudentId) {
        System.out.print("\nEnter Exam Code: ");
        String examId = scanner.nextLine();
        try {
            PreparedStatement examStmt = connection.prepareStatement("SELECT * FROM results WHERE exam_id = ? AND studentID = ?");
            examStmt.setString(1, examId);
            examStmt.setString(2, StudentId);
            ResultSet rs = examStmt.executeQuery();
            if (rs.next()) {
                System.out.println("\nYour Score: " + rs.getInt("score"));
            } else {
                System.out.println("\nNo result found.");
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Take exam section
    public void TakeExam(String email,String studentId) {
        System.out.print("\nEnter Exam Code: ");
        String examId = scanner.nextLine();
        try {
            PreparedStatement examStmt = connection.prepareStatement("SELECT * FROM exams WHERE exam_id = ?");
            examStmt.setString(1, examId);
            ResultSet examInfo = examStmt.executeQuery();

            if (!examInfo.next()) {
                System.out.println("Invalid Exam ID.");
                return;
            }

            PreparedStatement find = connection.prepareStatement("SELECT name FROM results WHERE exam_id = ? AND studentID = ?");
            find.setString(1, examId);
            find.setString(2, studentId);
            ResultSet findrs = find.executeQuery();

            if (findrs.next()) {
                System.out.println("You attended the exam before.");
                return;
            }

            String course_code = examInfo.getString("courseCode");

            PreparedStatement teacherStmt = connection.prepareStatement("SELECT * FROM teacher WHERE courseCode = ?");
            teacherStmt.setString(1, course_code);
            ResultSet titleInfo = teacherStmt.executeQuery();

            System.out.println("MBSTU");
            System.out.println("Dept. of ICT");
            if (titleInfo.next()) {
                System.out.print("Course Title : " + titleInfo.getString("courseTitle") + "             ");
                System.out.println("Marks: " + examInfo.getInt("marks"));
            }
            int duration = examInfo.getInt("duration");

            System.out.println("Course ID     : " + course_code + "               Duration: " +  duration + "minute");

            long endTime = System.currentTimeMillis() + duration * 60 * 1000;

            PreparedStatement ps = connection.prepareStatement("SELECT * FROM questions WHERE exam_id = ?");
            ps.setString(1, examId);
            ResultSet rs = ps.executeQuery();

            int totalScore = 0;

            int i = 1;
            while (rs.next()) {
                long remaining = (endTime - System.currentTimeMillis()) / 1000;
                if (remaining <= 0) {
                    System.out.println("Time's up!");
                    break;
                }
                System.out.println("\nTime left: " + remaining + " seconds");
                System.out.println("Question " + i + ". " + rs.getString("qn"));
                System.out.println("A. " + rs.getString("opt1"));
                System.out.println("B. " + rs.getString("opt2"));
                System.out.println("C. " + rs.getString("opt3"));
                System.out.println("D. " + rs.getString("opt4"));
                System.out.print("Your answer: ");
                String userAns = scanner.nextLine().toUpperCase();

                remaining = (endTime - System.currentTimeMillis()) / 1000;
                if (remaining <= 0) {
                    System.out.println("Time's up!");
                    break;
                }

                if (userAns.equals(rs.getString("ans"))) {
                    totalScore += rs.getInt("marks");
                }
                i++;
            }

            PreparedStatement studentStmt = connection.prepareStatement("SELECT * FROM student WHERE email = ?");
            studentStmt.setString(1, email);
            ResultSet studentDetails = studentStmt.executeQuery();
            studentDetails.next();
            String name = studentDetails.getString("name");
            String studentID = studentDetails.getString("studentID");

            PreparedStatement insertResult = connection.prepareStatement("INSERT INTO results (name,StudentID,score,exam_id) VALUES (?,?,?,?)");
            insertResult.setString(1, name);
            insertResult.setString(2, studentID);
            insertResult.setInt(3, totalScore);
            insertResult.setString(4, examId);
            insertResult.executeUpdate();

            System.out.println("\nExam submitted. Your score: " + totalScore + " out of" + examInfo.getInt("marks"));

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
