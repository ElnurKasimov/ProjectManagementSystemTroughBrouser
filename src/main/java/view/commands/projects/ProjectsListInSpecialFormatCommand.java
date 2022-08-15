package view.commands.projects;

import model.commandService.Command;
import model.dao.ProjectDaoService;
import model.dbConnection.DBConnection;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ProjectsListInSpecialFormatCommand implements Command {
    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp, TemplateEngine engine) throws IOException {
        try {
            resp.setContentType("text/html");
            Map<String, Object> parameterMap = new HashMap<>();
            parameterMap.put("question", "List of all projects in format date-name-quantity developers :");
            parameterMap.put("lines", ProjectDaoService.getInstance(DBConnection.getInstance().getConnection()).
                    getProjectsListInSpecialFormat());
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
