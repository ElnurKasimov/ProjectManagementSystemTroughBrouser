package model.command.developers;

import model.command.Command;
import model.dao.DeveloperDaoService;
import model.default_settings.DBConnection;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class AddDeveloperCommand  implements Command {
    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp, TemplateEngine engine) throws IOException {
        try {
            DeveloperDaoService developerDaoService = new DeveloperDaoService(DBConnection.getInstance().getConnection());
            resp.setContentType("text/html");
            Map<String, Object> parametrMap = new HashMap<>();
            String lastName = req.getParameter("developerLastName");
            String firstName = req.getParameter("developerFirstName");
            String  age = req.getParameter("developerAge");
            String company = req.getParameter("developerCompany");
            String project = req.getParameter("developerProject");
            String salary = req.getParameter("developerSalary");
            String language = req.getParameter("developerLanguage");
            String languageLevel = req.getParameter("developerLanguageLevel");






            parametrMap.put("question", "Developer " + firstName + " " + lastName);
            parametrMap.put("lines",developerDaoService.addDeveloper(lastName, firstName));
            Context context = new Context(
                    req.getLocale(),
                    parametrMap
            );
            engine.process("result", context, resp.getWriter());
            resp.getWriter().close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
