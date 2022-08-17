package view.commands.developers;

import control.commandService.Command;
import model.dao.DeveloperDaoService;
import model.dbConnection.DBConnection;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DeleteDeveloperCommand  implements Command {
    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp, TemplateEngine engine) throws IOException {
        try {
            resp.setContentType("text/html");
            Map<String, Object> parameterMap = new HashMap<>();
            String lastName = req.getParameter("developerLastName");
            String firstName = req.getParameter("developerFirstName");
            parameterMap.put("question", "Developer " + firstName + " " + lastName);
            parameterMap.put("lines",
                    DeveloperDaoService.getInstance(DBConnection.getInstance().getConnection()).deleteDeveloper(lastName, firstName));
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
