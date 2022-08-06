package model.command.main;

import model.command.Command;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class StartCommand implements Command {
    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp, TemplateEngine engine) throws IOException {
        Properties p = System.getProperties();
        p.list(System.out);
        System.out.println("catalina.home = " + System.getProperty("catalina.home"));
        System.out.println("catalina.base = " + System.getProperty("catalina.base"));
        System.out.println("user.dir = " + System.getProperty("user.dir"));



        resp.setContentType("text/html, charset=utf-8");
        Context context = new Context();
        context.setVariable("table", req.getParameter("table"));
        engine.process("main", context, resp.getWriter());
        resp.getWriter().close();
    }
}