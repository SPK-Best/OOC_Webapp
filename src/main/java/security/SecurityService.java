package security;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;

public class SecurityService {

    private DatabaseService databaseService;

    public SecurityService() throws SQLException, ClassNotFoundException {
        databaseService = new DatabaseService();
    }

    /**
     * Check whether you're already sign in or not (User is in the database or not)
     */
    public boolean isAuthorized(HttpServletRequest request) throws SQLException {
        String username = (String) request.getSession().getAttribute("username");
        return (username != null && databaseService.containUser(username));
    }

    /**
     * Check for user's authenticate
     */
    public boolean authenticate(String username, String password, HttpServletRequest request) throws SQLException {
        // True : username and password are matched, False : Otherwise
        if (databaseService.authenticate(username,password)) {
            request.getSession().setAttribute("username", username);
            return true;
        }
        else {
            return false;
        }
    }
}
