package servlet;

import security.SecurityService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class LoginServlet extends AbstractRoutableHttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("WEB-INF/login.jsp");
        requestDispatcher.include(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get username and password
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        String error = "";

        // Case : There are username and password
        if (username != null && password != null) {
            try {
                if (securityService.authenticate(username, password, request)) {
                    response.sendRedirect("/");
                }
                else {
                    error = "Wrong username or password.  Please try again";
                    request.setAttribute("error", error);
                    RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/login.jsp");
                    rd.include(request, response);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else {
            error = "Username or password is missing.";
            request.setAttribute("error", error);
            RequestDispatcher rd = request.getRequestDispatcher("WEB-INF/login.jsp");
            rd.include(request, response);
        }
    }

    @Override
    public void setSecurityService(SecurityService securityService) {
        this.securityService = securityService;
    }

    @Override
    public String getPattern() {
        return "/login";
    }

}
