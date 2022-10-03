package control;

import control.commandService.CommandService;
import model.dao.CompanyDaoService;
import model.dao.CustomerDaoService;
import model.dao.DeveloperDaoService;
import model.dao.ProjectDaoService;
import model.dbConnection.DBConnection;
import model.dbConnection.Migration;
import model.dbConnection.Prefs;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.FileTemplateResolver;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebServlet("/*")
public class FrontController extends HttpServlet {
    private TemplateEngine engine;
    private CommandService commandService;
    private DBConnection dbConnection;

    private ServletConfig config;
    public static String PROJECT_ROOT;
    public static Prefs prefs;
    public static final int LENGTH_SUBDIRECTORIES_NAMES = 16;

    @Override
    public void init(ServletConfig config) throws ServletException  {
        this.config = config;
        /*
        Getting PROJECT_ROOT from servlets getRealPath
        ServletContext sc = config.getServletContext();
        String tomcatDestination = sc.getRealPath("/").replace('\\', '/') ;
        PROJECT_ROOT = tomcatDestination.substring(0, tomcatDestination.length() - LENGTH_SUBDIRECTORIES_NAMES);

         */
       // Getting PROJECT_ROOT from environment variables

       PROJECT_ROOT = System.getenv().get("PROJECT_HOME");

        engine = new TemplateEngine();
        FileTemplateResolver resolver = new FileTemplateResolver();
        resolver.setPrefix(PROJECT_ROOT  + "/src/main//webapp/templates/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode("HTML5");
        resolver.setOrder(engine.getTemplateResolvers().size());
        resolver.setCacheable(false);
        engine.addTemplateResolver(resolver);
        commandService = new CommandService();
        prefs = new Prefs(PROJECT_ROOT+"prefs.json");
        dbConnection = DBConnection.getInstance();
        new Migration().initDb(dbConnection);
        DeveloperDaoService.getInstance(dbConnection.getConnection());
        CompanyDaoService.getInstance(dbConnection.getConnection());
        ProjectDaoService.getInstance(dbConnection.getConnection());
        CustomerDaoService.getInstance(dbConnection.getConnection());
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        commandService.process(req, resp, engine);
    }
}
