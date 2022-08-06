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
import java.sql.SQLOutput;
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
        commands.put("POST /developers/developer_info", new GetInformationAboutDeveloperByNameCommand());
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


            String scheme = req.getScheme();
            String serverName = req.getServerName();
            int serverPort = req.getServerPort();
            String contextPath = req.getContextPath();
            String servletPath = req.getServletPath();
            String pathInfo = req.getPathInfo();
            String queryString = req.getQueryString();

            System.out.println("scheme- " + scheme);
            System.out.println("server name - " + serverName);
            System.out.println("serverPort - " + serverPort);
            System.out.println("servletPath - " + servletPath);
            System.out.println("contextPath - " + contextPath);
            System.out.println("pathInfo - " + pathInfo);
            System.out.println("queryString - " + queryString);

            StringBuilder url = new StringBuilder();
            url.append(scheme).append("://").append(serverName);

            if (serverPort != 80 && serverPort != 443) {
                url.append(":").append(serverPort);
            }

            url.append(contextPath).append(servletPath);

            if (pathInfo != null) {
                url.append(pathInfo);
            }
            if (queryString != null) {
                url.append("?").append(queryString);
            }
        System.out.println(url.toString());

        commands.get(commandKey).process(req, resp, engine);
    }

}
