package view.commands.developers;

import control.commandService.Command;
import model.dao.DeveloperDaoService;
import model.dao.TemporaryDeveloper;
import model.dbConnection.DBConnection;
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
            resp.setContentType("text/html");
            Map<String, Object> parameterMap = new HashMap<>();
            TemporaryDeveloper temporaryDeveloper = new TemporaryDeveloper();
            temporaryDeveloper.setLastName(req.getParameter("developerLastName"));
            temporaryDeveloper.setFirstName(req.getParameter("developerFirstName"));
            temporaryDeveloper.setAge(Integer.parseInt(req.getParameter("developerAge")));
            temporaryDeveloper.setCompanyName(req.getParameter("developerCompany"));
            temporaryDeveloper.setProjectName(req.getParameter("developerProject"));
            temporaryDeveloper.setSalary(Integer.parseInt(req.getParameter("developerSalary")));
            temporaryDeveloper.setLanguage(req.getParameter("developerLanguage"));
            temporaryDeveloper.setLanguageLevel(req.getParameter("developerLanguageLevel"));
            parameterMap.put("question", "Developer " + temporaryDeveloper.getFirstName() + " " + temporaryDeveloper.getLastName());
            parameterMap.put("lines", DeveloperDaoService.getInstance(DBConnection.getInstance().getConnection()).addDeveloper(temporaryDeveloper));
            Context context = new Context(
                    req.getLocale(),
                    parameterMap
            );
            engine.process("result", context, resp.getWriter());
            resp.getWriter().close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
