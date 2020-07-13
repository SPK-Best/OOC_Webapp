package security;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.ArrayList;

public class DatabaseService {

    enum user_info {
        username, password;
    }

    private final String jdbcDriverStr;
    private final String jdbcURL;

    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    private PreparedStatement preparedStatement;

    public DatabaseService() throws SQLException, ClassNotFoundException {
        this.jdbcDriverStr = "com.mysql.cj.jdbc.Driver";
        this.jdbcURL = "jdbc:mysql://localhost/ooc_homework?"
                + "user=supakrit&password=123";
        Class.forName(jdbcDriverStr);
        connection = DriverManager.getConnection(jdbcURL);
        statement = connection.createStatement();
        createDatabase();
    }

    /**
     * Create new database (This function is used only when we first run the program)
     */
    public void createDatabase() throws SQLException {
        statement.execute("create table if not exists user_info(username varchar(255) not null , password varchar(255) not null)");
        resultSet = statement.executeQuery("select * from ooc_homework.user_info;");
        if (!resultSet.next()){
            String hashed = BCrypt.hashpw("admin", BCrypt.gensalt());
            preparedStatement = connection.prepareStatement("insert into ooc_homework.user_info values ('admin','"+ hashed +"')");
            preparedStatement.execute();
        }
    }

    /**
     * Read the data from the database
     */
    public void readData() throws Exception {
        try {
            resultSet = statement.executeQuery("select * from ooc_homework.user_info;");
            getResultSet(resultSet);
        }
        finally {
            close();
        }
    }

    /**
     * Get username and password
     */
    private void getResultSet(ResultSet resultSet) throws Exception {
        while (resultSet.next()) {
            String username = resultSet.getString(user_info.username.toString());
            String password = resultSet.getString(user_info.password.toString());
        }
    }

    /**
     * Create new User if the User is not already register
     */
    public boolean createNewUser(String username, String password) throws SQLException{
        String hashed = BCrypt.hashpw(password,BCrypt.gensalt());        // Encrypt the password
        if (!containUser(username)){                                    // Case : User is not already register
            statement.execute("insert into user_info(username, password) values ( '" + username + "', '" + hashed + "')");  // Insert new User
            return true;
        }
        return false;
    }

    /**
     * Check whether user is already exists or not
     */
    public boolean containUser(String username) throws SQLException{
        ArrayList<String> nameList = getAllUser();
        if (nameList.contains(username)){
            return true;
        }
        return false;
    }

    /**
     * Delete user
     */
    public boolean deleteUser(String username) throws SQLException{
        if (containUser(username)) {
            statement.execute("delete from user_info where username ='" + username + "';");
            return true;
        }
        return false;
    }

    /**
     * Check for user's authenticate
     */
    public boolean authenticate(String username, String password) throws SQLException {
        if (containUser(username)) {
            resultSet = statement.executeQuery("select * from user_info where(username = '" + username +"')");
            if (resultSet.next()) {
                // Check for the password
                if (BCrypt.checkpw(password, resultSet.getString(user_info.password.toString()))) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Get all username
     */
    public ArrayList<String> getAllUser() throws SQLException {
        resultSet = statement.executeQuery("select  * from user_info");
        ArrayList<String> nameList = new ArrayList<>();
        while (resultSet.next()){
            nameList.add(resultSet.getString(user_info.username.toString()));
        }
        return nameList;
    }

    public User getUser(String username) throws SQLException, ClassNotFoundException {
        User user = new User(username);
        return user;
    }

    private void close() {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        catch (Exception e) {
        }
    }
}