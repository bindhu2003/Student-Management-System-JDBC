import java.sql.*;
import java.util.Scanner;

public class MyJdbcProject {

    // Database connection details
    static String url = "jdbc:mysql://localhost:3306/jdbce1";
    static String username = "root";
    static String password = "";//MySQL password

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            System.out.println("Connected to the database!");

            while (true) {
                System.out.println("\nChoose an operation:");
                System.out.println("1. Add Student");
                System.out.println("2. View All Students");
                System.out.println("3. Update Student Details");
                System.out.println("4. Delete Student");
                System.out.println("5. Exit");
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        addStudent(conn, scanner);
                        break;
                    case 2:
                        viewStudents(conn);
                        break;
                    case 3:
                        updateStudent(conn, scanner);
                        break;
                    case 4:
                        deleteStudent(conn, scanner);
                        break;
                    case 5:
                        System.out.println("Exiting the program.");
                        return;
                    default:
                        System.out.println("Invalid choice! Please try again.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Database connection error!");
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }

    // Method to add a student
    private static void addStudent(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter student name: ");
        String name = scanner.next();
        System.out.print("Enter student age: ");
        int age = scanner.nextInt();

        String query = "INSERT INTO student (name, age) VALUES (?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, age);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Student added successfully!");
            }
        }
    }

    // Method to view all students
    private static void viewStudents(Connection conn) throws SQLException {
        String query = "SELECT * FROM student";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            System.out.println("\nStudent List:");
            while (rs.next()) {
                System.out.printf("ID: %d, Name: %s, Age: %d%n",
                        rs.getInt("id"), rs.getString("name"), rs.getInt("age"));
            }
        }
    }

    // Method to update student details
    private static void updateStudent(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter the student ID to update: ");
        int id = scanner.nextInt();
        System.out.print("Enter new name: ");
        String newName = scanner.next();
        System.out.print("Enter new age: ");
        int newAge = scanner.nextInt();

        String query = "UPDATE student SET name = ?, age = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, newName);
            pstmt.setInt(2, newAge);
            pstmt.setInt(3, id);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Student details updated successfully!");
            } else {
                System.out.println("No student found with the given ID.");
            }
        }
    }

    // Method to delete a student
    private static void deleteStudent(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter the student ID to delete: ");
        int id = scanner.nextInt();

        String query = "DELETE FROM student WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Student deleted successfully!");
            } else {
                System.out.println("No student found with the given ID.");
            }
        }
    }
}
