package security;

import java.sql.SQLException;

public class User {

    private String username;
    private String password;

    private DatabaseService databaseService;

    public User(String username) throws SQLException, ClassNotFoundException {
        this.username = username;
        this.databaseService = new DatabaseService();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
