package frontcontroller.command;

import control.Message;
import frontcontroller.InMemory2MessageStorage;
import org.thymeleaf.TemplateEngine;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

public class AddMessageCommand implements Command {
    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp, TemplateEngine engine) throws IOException {
        String author = req.getParameter("author");
        String content = req.getParameter("content");

        Message message = new Message();
        message.setAuthor(author);
        message.setContent(content);
        message.setId(UUID.randomUUID().toString());

        InMemory2MessageStorage.getInstance().add(message);

        resp.sendRedirect("/");

    }
}