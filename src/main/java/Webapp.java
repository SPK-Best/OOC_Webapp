import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import security.DatabaseService;
import servlet.ServletRouter;

import java.io.File;
import java.sql.SQLException;

public class Webapp {

    public static void main(String[] args) throws Exception {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(80);

        DatabaseService databaseService = new DatabaseService();
        databaseService.readData();


        File docBase = new File("src/main/webapp/");
        docBase.mkdir();

        try {
            Context ctx = tomcat.addWebapp("",docBase.getAbsolutePath());

            ServletRouter servletRouter = new ServletRouter();
            servletRouter.init(ctx);

            tomcat.start();
            tomcat.getServer().await();
        } catch (LifecycleException e) {
            e.printStackTrace();
        }
    }
}