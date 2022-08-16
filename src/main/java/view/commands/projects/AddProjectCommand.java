package view.commands.projects;

import model.commandService.Command;
import model.dao.DeveloperDaoService;
import model.dao.ProjectDaoService;
import model.dao.TemporaryDeveloper;
import model.dao.TemporaryProject;
import model.dbConnection.DBConnection;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class AddProjectCommand implements Command {
    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp, TemplateEngine engine) throws IOException {
        try {
            resp.setContentType("text/html");
            Map<String, Object> parameterMap = new HashMap<>();
            TemporaryProject temporaryProject = new TemporaryProject();
            temporaryProject.setProjectName(req.getParameter("projectName"));
            temporaryProject.setCompanyName(req.getParameter("companyName"));
            temporaryProject.setCustomerName(req.getParameter("customerName"));
            temporaryProject.setProjectCost(Integer.parseInt(req.getParameter("projectCost")));
            temporaryProject.setStartDate(req.getParameter("startDate"));
            System.out.println(temporaryProject);
            parameterMap.put("question", "Project " + temporaryProject.getProjectName() + ":");
            parameterMap.put("lines", ProjectDaoService.getInstance(DBConnection.getInstance().getConnection()).
                    addProject(temporaryProject));
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