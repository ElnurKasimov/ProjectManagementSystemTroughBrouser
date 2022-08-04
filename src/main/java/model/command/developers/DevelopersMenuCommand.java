package model.command.developers;

import model.command.Command;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class DevelopersMenuCommand implements Command {
    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp, TemplateEngine engine) throws IOException {
        resp.setContentType("text/html, charset=utf-8");
        Context context = new Context();
        Map<String,String[]> parameters = req.getParameterMap();
        for (Map.Entry<String, String []> entry : parameters.entrySet()) {
            System.out.println("parametr - " + entry.getKey() + ", value - " + entry.getValue()[0]);
        }
        context.setVariable("table", req.getParameter("table"));
        engine.process("developers", context, resp.getWriter());
        resp.getWriter().close();
    }
}