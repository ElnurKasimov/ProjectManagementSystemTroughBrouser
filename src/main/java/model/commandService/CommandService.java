package model.commandService;

import view.commands.companies.CompaniesMenuCommand;
import view.commands.customers.CustomersMenuCommand;
import view.commands.developers.*;
import view.commands.main.ChooseTableCommand;
import view.commands.main.StartCommand;
import view.commands.projects.*;
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
        commands.put("GET /projects", new ProjectsMenuCommand());
        commands.put("GET /companies", new CompaniesMenuCommand());
        commands.put("GET /customers", new CustomersMenuCommand());
        commands.put("GET /developers/all_developers", new GetInformationAboutAllDevelopersCommand());
        commands.put("POST /developers/developer_info", new GetInformationAboutDeveloperByNameCommand());
        commands.put("GET /developers/java_developers", new QuantityJavaDevelopersCommand());
        commands.put("GET /developers/middle_developers", new ListMiddleDevelopersCommand());
        commands.put("POST /developers/add", new AddDeveloperCommand());
        commands.put("POST /developers/update", new UpdateDeveloperCommand());
        commands.put("POST /developers/delete", new DeleteDeveloperCommand());
        commands.put("GET /projects/all_projects", new GetInformationAboutAllProjectsCommand());
        commands.put("GET /projects/project_info", new GetInformationAboutProjectByNameCommand());
        commands.put("GET /projects/project_developers", new ListProjectDevelopersCommand());
    }

    public void process(HttpServletRequest req, HttpServletResponse resp, TemplateEngine engine) throws IOException {
        String requestUri = req.getRequestURI();
        String commandKey = req.getMethod() + " " + requestUri;
        commands.get(commandKey).process(req, resp, engine);
    }

}
