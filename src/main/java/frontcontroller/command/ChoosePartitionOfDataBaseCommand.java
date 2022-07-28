package frontcontroller.command;

import control.Message;
import frontcontroller.InMemory2MessageStorage;
import org.thymeleaf.TemplateEngine;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ChoosePartitionOfDataBaseCommand implements Command {
    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp, TemplateEngine engine) throws IOException {
        System.out.println("POST");
        Map<String, String []> developers = req.getParameterMap();
        for(Map.Entry<String,String[]> param : developers.entrySet()) {
            System.out.println("key = " + param.getKey() + " value " + param.getValue()[0]);
        }
        resp.sendRedirect("/");

    }
}
