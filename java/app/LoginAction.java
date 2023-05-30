package app;

import com.opensymphony.xwork2.ActionSupport;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginAction extends ActionSupport {
    private String username;
    private String password;
    private StudentBean student;
    private StaffBean staff;
    public String getUsername() {
        return username;
    }
    // accessor and mutator methods for each attribute
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public StudentBean getStudent() {
        return student;
    }
    public void setStudent(StudentBean student) {
        this.student = student;
    }
    public StaffBean getStaff() {
        return staff;
    }
    public void setStaff(StaffBean staff) {
        this.staff = staff;
    }

    public String execute() {
        try {
            // establish server driver to avoid
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            // connect to database with set username and password
            try (Connection connection = DriverManager.getConnection("jdbc:sqlserver://localhost\\SQLEXPRESS;databaseName=seng2050_test", "user1", "comp1140isBAE")) {
                // Prepare the SQL query
                String sql = "SELECT * FROM [User] WHERE username = ?";
                PreparedStatement statement = connection.prepareStatement(sql);

                // add username to query
                statement.setString(1, username);
                // run query
                ResultSet resultSet = statement.executeQuery();

                // if the query is not null...
                if (resultSet.next()) {
                    // retrieve the user's passwordHash
                    String storedPasswordHash = resultSet.getString("passwordHash");

                    // validate password against hash
                    if (!verifyPassword(password, storedPasswordHash)) {
                        // return to login page with error message if password is invalid
                        addActionError("Invalid password.");
                        return ERROR;
                    }

                    // determine role (staff or student)
                    String role = resultSet.getString("role");

                    // if the user is a student...
                    if (role.equals("student")) {
                        // query database with a join on User and Student tables
                        sql = "SELECT * FROM [User] u JOIN Student s ON u.username = s.username WHERE u.username = ?";
                        statement = connection.prepareStatement(sql);
                        statement.setString(1, username);
                        ResultSet studentResultSet = statement.executeQuery();

                        // if the query is not null...
                        if (studentResultSet.next()) {
                            // instantiate the student bean
                            student = new StudentBean();

                            // set student attributes
                            student.setUsername(studentResultSet.getString("username"));
                            student.setFirstName(studentResultSet.getString("firstName"));
                            student.setLastName(studentResultSet.getString("lastName"));
                            student.setRole(role);
                            student.setStudentNumber(studentResultSet.getString("studentNumber"));
                            student.setContactNumber(studentResultSet.getString("contactNumber"));
                            student.setEmail(studentResultSet.getString("email"));

                            // redirect to student home screen
                            return "student";
                        }
                    }

                    // if the user is a staff member...
                    else if (role.equals("staff")) {
                        // query database with join on User and Staff tables
                        sql = "SELECT * FROM [User] u JOIN Staff s ON u.username = s.username WHERE u.username = ?";
                        statement = connection.prepareStatement(sql);
                        statement.setString(1, username);
                        ResultSet staffResultsSet = statement.executeQuery();

                        // if the query is not null...
                        if (staffResultsSet.next()) {
                            //instantiate the StaffBean
                            staff = new StaffBean();

                            // set staff attributes
                            staff.setUsername(staffResultsSet.getString("username"));
                            staff.setFirstName(staffResultsSet.getString("firstName"));
                            staff.setLastName(staffResultsSet.getString("lastName"));
                            staff.setRole(role);
                            staff.setContactNumber(staffResultsSet.getString("contactNumber"));
                            staff.setEmail(staffResultsSet.getString("email"));
                            staff.setStaffNumber(staffResultsSet.getString("staffNumber"));
                            staff.setManager(staffResultsSet.getBoolean("managerFlag"));

                            // redirect to staff home screen
                            return "staff";
                        }
                    }
                }
                // return to login page with error message if username is invalid
                addActionError("Invalid username.");
                return ERROR;
            }
        }
        // Catch SQL Server driver not found
        catch (ClassNotFoundException e) {
            e.printStackTrace();
            addActionError("An unexpected error has occurred.");
            return ERROR;
        }
        // Catch invalid SQL
        catch (SQLException e) {
            e.printStackTrace();
            addActionError("An unexpected error has occurred.");
            return ERROR;
        }
    }

    // validates the user entered password against the stored password hash.
    // returns 'true' if the password is correct, else 'false'
    private boolean verifyPassword(String plaintext, String storedHash) {
        return storedHash.equals(hash(plaintext));
    }

    // converts a plain text password into a SHA-256 hash string.
    // returns the hashed string if successful, or 'null' if an exception occurs.
    private String hash(String password) {
        try {
            // obtain the SHA-256 hashing algorithm
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            // the bytes of the password are added to the message digest
            messageDigest.update(password.getBytes());

            // hash value stored in digest byte array
            byte[] digest = messageDigest.digest();

            // a StringBuilder to hold the hashed string
            StringBuilder stringBuilder = new StringBuilder();

            // for each byte in the digest array...
            for (byte b : digest) {
                // convert to a hexadecimal string and append to the StringBuilder
                stringBuilder.append(String.format("%02x", b & 0xff));
            }
            // return the hashed password as a string
            return stringBuilder.toString();
        }
        // catch the SHA-256 algorithm being unavailable
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}