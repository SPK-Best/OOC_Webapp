package servlet;

import security.DatabaseService;
import security.SecurityService;
import security.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class HomeServlet extends AbstractRoutableHttpServlet {

    private SecurityService securityService;

    private DatabaseService databaseService;

    public HomeServlet() throws SQLException, ClassNotFoundException {
        this.databaseService = new DatabaseService();
    }

    /**
     * Refresh the current table on the page (When user click remove button)
     */
    public void refreshTable(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServletException {
        ArrayList<String> userList = databaseService.getAllUser();      // Get all users in the database
        request.setAttribute("userList", userList);                 // Set attribute to userList
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("WEB-INF/home.jsp");
        requestDispatcher.include(request, response);
    }

    @Override
    public void setSecurityService(SecurityService securityService) {
        this.securityService = securityService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean authorized = false;

        try {
            authorized = securityService.isAuthorized(request);      // Check for user authorization
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        // This username is authorized
        if (authorized) {
            String username = (String) request.getSession().getAttribute("username");
            try {
                User user = databaseService.getUser(username);            // Get the user's data
                request.getSession().setAttribute("user", user);       // Set attribute to user
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            // Refresh the user table in the home page
            try {
                refreshTable(request,response);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        // Otherwise, redirect to login page (username or password are incorrect)
        else {
            response.sendRedirect("/login");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Case : user press submit button
        if(request.getParameter("addUserInfo") != null){
            String newUsername = request.getParameter("newUsername");    // Get new username from home.jsp
            try {
                if (databaseService.containUser(newUsername)){              // Case : Username is already exists
                    String error = "This username already exists. Please use another name";       // Creating error message
                    request.setAttribute("nameError", error);            // Send error message
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
                String error = e.getMessage();
                request.setAttribute("nameError", error);
            }

            String newPassword = request.getParameter("newPassword");           // Get password
            String confirmPassword = request.getParameter("confirmPassword");
            if (newPassword.equals(confirmPassword)) {        // Compare both passwords are the same or not
                try {
                    databaseService.createNewUser(newUsername, newPassword);     // Create new User with username and password
                    refreshTable(request, response);                             // Refresh the table
                }
                catch (SQLException e) {
                    e.printStackTrace();
                    String error = e.getMessage();
                    request.setAttribute("nameError", error);
                }
            }
            // Both Passwords and confirmPassword are not the same
            else{
                String error = "Password doesn't match";            // Create error message
                request.setAttribute("nameError", error);
            }
        }
        // Case : user press remove button
        else if (request.getParameter("removeUser") != null){
            String user = request.getParameter("user_to_use");
            try {
                databaseService.deleteUser(user);       // Delete user in the database
                refreshTable(request, response);        // Refresh the table
            }
            catch (SQLException e) {
                e.printStackTrace();
                String error = e.getMessage();
                request.setAttribute("removing_error", error);
            }
        }
        // Case : user press logout button
        else if(request.getParameter("logout")!= null){
            request.getSession().invalidate();
            response.sendRedirect("/login");                // Redirect to login page
        }

    }


    @Override
    public String getPattern() {
        return "/index.jsp";
    }
}
