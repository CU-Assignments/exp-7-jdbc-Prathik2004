import java.sql.*;
import java.util.Scanner;

public class Java6_2 {
    static final String URL = "jdbc:mysql://localhost:3306/Java6"; // Replace with your DB name
    static final String USER = "root"; // Replace with your MySQL username
    static final String PASSWORD = "Mrithik2008@"; // Replace with your MySQL password

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            conn.setAutoCommit(false); // Enable transaction handling
            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println("\nMenu:");
                System.out.println("1. Create Product");
                System.out.println("2. Read Products");
                System.out.println("3. Update Product");
                System.out.println("4. Delete Product");
                System.out.println("5. Exit");
                System.out.print("Choose an option: ");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1: createProduct(conn, scanner); break;
                    case 2: readProducts(conn); break;
                    case 3: updateProduct(conn, scanner); break;
                    case 4: deleteProduct(conn, scanner); break;
                    case 5: System.out.println("Exiting..."); return;
                    default: System.out.println("Invalid option. Try again.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createProduct(Connection conn, Scanner scanner) {
        String query = "INSERT INTO Product (ProductID, ProductName, Price, Quantity) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            System.out.print("Enter ProductID: ");
            int id = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            System.out.print("Enter Product Name: ");
            String name = scanner.nextLine();
            System.out.print("Enter Price: ");
            double price = scanner.nextDouble();
            System.out.print("Enter Quantity: ");
            int quantity = scanner.nextInt();

            pstmt.setInt(1, id);
            pstmt.setString(2, name);
            pstmt.setDouble(3, price);
            pstmt.setInt(4, quantity);

            pstmt.executeUpdate();
            conn.commit();
            System.out.println("Product added successfully!");
        } catch (SQLException e) {
            rollbackTransaction(conn, e);
        }
    }

    private static void readProducts(Connection conn) {
        String query = "SELECT * FROM Product";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            System.out.println("\nProductID | ProductName | Price | Quantity");
            while (rs.next()) {
                System.out.printf("%d | %s | %.2f | %d\n", rs.getInt("ProductID"), rs.getString("ProductName"), rs.getDouble("Price"), rs.getInt("Quantity"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void updateProduct(Connection conn, Scanner scanner) {
        String query = "UPDATE Product SET Price = ?, Quantity = ? WHERE ProductID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            System.out.print("Enter ProductID to update: ");
            int id = scanner.nextInt();
            System.out.print("Enter new Price: ");
            double price = scanner.nextDouble();
            System.out.print("Enter new Quantity: ");
            int quantity = scanner.nextInt();

            pstmt.setDouble(1, price);
            pstmt.setInt(2, quantity);
            pstmt.setInt(3, id);

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                conn.commit();
                System.out.println("Product updated successfully!");
            } else {
                System.out.println("Product not found!");
            }
        } catch (SQLException e) {
            rollbackTransaction(conn, e);
        }
    }

    private static void deleteProduct(Connection conn, Scanner scanner) {
        String query = "DELETE FROM Product WHERE ProductID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            System.out.print("Enter ProductID to delete: ");
            int id = scanner.nextInt();

            pstmt.setInt(1, id);
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                conn.commit();
                System.out.println("Product deleted successfully!");
            } else {
                System.out.println("Product not found!");
            }
        } catch (SQLException e) {
            rollbackTransaction(conn, e);
        }
    }

    private static void rollbackTransaction(Connection conn, SQLException e) {
        try {
            if (conn != null) {
                conn.rollback();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        e.printStackTrace();
    }
}
