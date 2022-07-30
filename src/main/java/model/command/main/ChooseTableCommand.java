package model.command.main;

import model.command.Command;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ChooseTableCommand implements Command {
    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp, TemplateEngine engine) throws IOException {
        Context context = new Context();
        switch(req.getParameter("table")) {
            case "developers":
               resp.sendRedirect("/developers");
                break;
            case "projects":
                resp.sendRedirect("/projects");
                break;
            case "companies":
                resp.sendRedirect("/companies");
                break;
            case "customers":
                resp.sendRedirect("/customers");
                break;
            default:
                resp.sendRedirect("/");
                break;
        }
    }
}
