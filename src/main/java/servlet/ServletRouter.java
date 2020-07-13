package servlet;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import security.SecurityService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServletRouter {

    private final List<Class<? extends AbstractRoutableHttpServlet>> servletClasses = new ArrayList<>();
    {
        servletClasses.add(HomeServlet.class);
        servletClasses.add(LoginServlet.class);
    }

    public void init(Context ctx) throws SQLException, ClassNotFoundException {
        SecurityService securityService = new SecurityService();

        for (Class<? extends AbstractRoutableHttpServlet> sc : servletClasses){
            try {
                AbstractRoutableHttpServlet httpServlet = sc.newInstance();
                httpServlet.setSecurityService(securityService);
                Tomcat.addServlet(ctx, sc.getSimpleName(),httpServlet);
                ctx.addServletMapping(httpServlet.getPattern(), sc.getSimpleName());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
    }
}