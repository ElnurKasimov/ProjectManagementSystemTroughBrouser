package control;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.FileTemplateResolver;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@WebServlet("/forum/*")
public class DataBaseController extends HttpServlet {
    private TemplateEngine engine;
    private  MessageStorage messageStorage;

    @Override
    public void init() throws ServletException {
        engine = new TemplateEngine();

        FileTemplateResolver resolver = new FileTemplateResolver();
        resolver.setPrefix("C:/Users/user/Downloads/tmp/ProjectManagementSystem/templates/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode("HTML5");
        resolver.setOrder(engine.getTemplateResolvers().size());
        resolver.setCacheable(false);
        engine.addTemplateResolver(resolver);

        messageStorage = new InMemoryMessageStorage();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html, charset=utf-8");

        Context simpleContext = new Context(
                req.getLocale(),
                Map.of("messages", messageStorage.getAllMessages())
        );

        engine.process("forum", simpleContext, resp.getWriter());


        resp.getWriter().close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(req.getRequestURI().contains("delete")) {
            String id = req.getParameter("id");
            messageStorage.deleteById(id);
        } else {
            String author = req.getParameter("author");
            String content = req.getParameter("content");
            resp.setContentType("text/html");

            Message message = new Message();
            message.setAuthor(author);
            message.setContent(content);
            message.setId(UUID.randomUUID().toString());

            messageStorage.add(message);
        }

        resp.sendRedirect("/forum");
    }


/*
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        commandService.process(req, resp, engine);
    }

 */
}
