package model.command.developers;

import model.command.Command;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class GetInformationAboutDeveloperByNameCommand  implements Command {
    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp, TemplateEngine engine) throws IOException {
        Context context = new Context();
        context.setVariable("FirstName", req.getParameter("developerFirstName"));
        context.setVariable("LastName", req.getParameter("developerLastName"));
        engine.process("developers_developer", context, resp.getWriter());
        resp.getWriter().close();
    }
}
