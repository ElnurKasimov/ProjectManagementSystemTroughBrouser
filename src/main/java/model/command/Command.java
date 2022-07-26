package model.command;

import org.thymeleaf.TemplateEngine;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface Command {
    void process(HttpServletRequest req, HttpServletResponse resp, TemplateEngine engine) throws IOException;
}
