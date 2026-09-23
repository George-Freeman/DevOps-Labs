package com.napier.sem;

import java.sql.*;

public class App {
    // Connection to MySQL
    private Connection con = null;

    public static void main(String[] args) {
        // Create new Application
        App a = new App();

        // Connect to database
        a.connect();
        Employee emp = a.getEmployee(255530);

        if (emp != null) {
            System.out.println("Employee Number: " + emp.emp_no);
            System.out.println("Name: " + emp.first_name + " " + emp.last_name);
            System.out.println("Title: " + emp.title);
            System.out.println("Salary: " + emp.salary);
            System.out.println("Department: " + emp.dept_name);
            System.out.println("Manager: " + emp.manager);
        }
        else {
            System.out.println("Employee not found");
        }
        // Disconnect from database
        a.disconnect();
    }

    /**
     * Connect to the MySQL database.
     */
    public void connect() {
        try {
            // Load database driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        }
        catch (ClassNotFoundException e) {
            System.out.println("Could not load SQL driver");
            System.exit(-1);
        }

        int retries = 10;

        for (int i = 0; i < retries; ++i) {
            System.out.println("Connecting to database...");

            try {
                // Wait for database to start
                Thread.sleep(30000);

                // Connect to database
                con = DriverManager.getConnection(
                        "jdbc:mysql://db:3306/employees?allowPublicKeyRetrieval=true&useSSL=false",
                        "root",
                        "example"
                );

                System.out.println("Successfully connected");
                break;
            }
            catch (SQLException sqle) {
                System.out.println(
                        "Failed to connect to database attempt "
                                + Integer.toString(i)
                );
                System.out.println(sqle.getMessage());
            }
            catch (InterruptedException ie) {
                System.out.println("Thread interrupted? Should not happen.");
            }
        }
    }

    /**
     * Disconnect from the MySQL database.
     */
    public void disconnect() {
        if (con != null) {
            try {
                con.close();
                System.out.println("Connection closed");
            }
            catch (Exception e) {
                System.out.println("Error closing connection to database");
            }
        }
    }
    /**
     * Gets an employee from the database.
     *
     * @paramID Employee number
     * @return Employee object, or null if not found
     */
    public Employee getEmployee(int ID) {
        Employee emp = null;

        try {
            String sql =
                    "SELECT e.emp_no, e.first_name, e.last_name, " +
                            "t.title, s.salary, d.dept_name, " +
                            "CONCAT(m.first_name, ' ', m.last_name) AS manager " +
                            "FROM employees e " +
                            "JOIN titles t ON e.emp_no = t.emp_no " +
                            "AND t.to_date = '9999-01-01' " +
                            "JOIN salaries s ON e.emp_no = s.emp_no " +
                            "AND s.to_date = '9999-01-01' " +
                            "JOIN dept_emp de ON e.emp_no = de.emp_no " +
                            "AND de.to_date = '9999-01-01' " +
                            "JOIN departments d ON de.dept_no = d.dept_no " +
                            "JOIN dept_manager dm ON d.dept_no = dm.dept_no " +
                            "AND dm.to_date = '9999-01-01' " +
                            "JOIN employees m ON dm.emp_no = m.emp_no " +
                            "WHERE e.emp_no = ?";

            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, ID);
            ResultSet rset = stmt.executeQuery();

            if (rset.next()) {
                emp = new Employee();
                emp.emp_no = rset.getInt("emp_no");
                emp.first_name = rset.getString("first_name");
                emp.last_name = rset.getString("last_name");
                emp.title = rset.getString("title");
                emp.salary = rset.getInt("salary");
                emp.dept_name = rset.getString("dept_name");
                emp.manager = rset.getString("manager");
            }
            rset.close();
            stmt.close();
        }

        catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get employee details");
        }
        return emp;

    }
}