package view.commands.developers;

import control.commandService.Command;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DevelopersMenuCommand implements Command {
    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp, TemplateEngine engine) throws IOException {
        resp.setContentType("text/html, charset=utf-8");
        Context context = new Context();
        context.setVariable("table", req.getParameter("table"));
        engine.process("developers", context, resp.getWriter());
        resp.getWriter().close();
    }
}