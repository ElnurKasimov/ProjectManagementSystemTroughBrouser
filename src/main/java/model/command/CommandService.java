package model.command;

import model.command.companies.CompaniesMenuCommand;
import model.command.customers.CustomersMenuCommand;
import model.command.developers.*;
import model.command.main.ChooseTableCommand;
import model.command.main.StartCommand;
import model.command.projects.ProjectsMenuCommand;
import org.thymeleaf.TemplateEngine;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CommandService {
    private Map<String, Command> commands;

    public CommandService() {
        commands = new HashMap<>();

        commands.put("GET /", new StartCommand());
        commands.put("POST /", new ChooseTableCommand());
        commands.put("GET /developers", new DevelopersMenuCommand());
        commands.put("GET /developers/all_developers", new GetInformationAboutAllDevelopersCommand());
        commands.put("GET /developers/developer_info", new GetInformationAboutDeveloperByNameCommand());
        commands.put("GET /developers/quantity_java_developers", new QuantityJavaDevelopersCommand());
        commands.put("GET /developers/middle_developers", new ListMiddleDevelopersCommand());
        commands.put("POST /developers/add", new AddDeveloperCommand());
        commands.put("POST /developers/update", new UpdateDeveloperCommand());
        commands.put("POST /developers/delete", new DeleteDeveloperCommand());
        commands.put("GET /projects", new ProjectsMenuCommand());
        commands.put("GET /companies", new CompaniesMenuCommand());
        commands.put("GET /customers", new CustomersMenuCommand());
    }

    public void process(HttpServletRequest req, HttpServletResponse resp, TemplateEngine engine) throws IOException {
        String requestUri = req.getRequestURI();
        String commandKey = req.getMethod() + " " + requestUri;
        System.out.println(commandKey);
        commands.get(commandKey).process(req, resp, engine);
    }

}
