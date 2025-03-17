import java.sql.*;

public class Java6_1 {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/Java6"; // Replace with your database name
        String user = "root"; // Replace with your MySQL username
        String password = "Mrithik2008@"; // Replace with your MySQL password

        String query = "SELECT EmpId, name, salary FROM employees";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            System.out.println("EmpID | Name | Salary");
            System.out.println("----------------------------");

            while (rs.next()) {
                int empID = rs.getInt("EmpID");
                String name = rs.getString("Name");
                double salary = rs.getDouble("Salary");

                System.out.println(empID + " | " + name + " | " + salary);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
