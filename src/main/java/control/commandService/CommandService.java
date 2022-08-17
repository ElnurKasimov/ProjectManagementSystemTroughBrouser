package control.commandService;

import view.commands.companies.AddCompanyCommand;
import view.commands.companies.CompaniesMenuCommand;
import view.commands.companies.DeleteCompanyCommand;
import view.commands.companies.ListAllCompaniesCommand;
import view.commands.customers.AddCustomerCommand;
import view.commands.customers.CustomersMenuCommand;
import view.commands.customers.DeleteCustomerCommand;
import view.commands.customers.ListAllCustomersCommand;
import view.commands.developers.*;
import view.commands.projects.*;

import view.commands.main.ChooseTableCommand;
import view.commands.main.StartCommand;
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
        commands.put("GET /developers/all_developers", new ListAllDevelopersCommand());
        commands.put("POST /developers/developer_info", new InformationAboutDeveloperByNameCommand());
        commands.put("GET /developers/java_developers", new QuantityJavaDevelopersCommand());
        commands.put("GET /developers/middle_developers", new ListMiddleDevelopersCommand());
        commands.put("POST /developers/add", new AddDeveloperCommand());
        commands.put("POST /developers/update", new UpdateDeveloperCommand());
        commands.put("POST /developers/delete", new DeleteDeveloperCommand());
        commands.put("GET /projects/all_projects", new ListAllProjectsCommand());
        commands.put("GET /projects/project_info", new InformationAboutProjectByNameCommand());
        commands.put("GET /projects/project_developers", new ListProjectDevelopersCommand());
        commands.put("GET /projects/project_budget", new ProjectBudgetCommand());
        commands.put("GET /projects/all_projects_in_special_format", new ProjectsListInSpecialFormatCommand());
        commands.put("POST /projects/add", new AddProjectCommand());
        commands.put("POST /projects/update", new UpdateProjectCommand());
        commands.put("POST /projects/delete", new DeleteProjectCommand());
        commands.put("GET /companies/all_companies", new ListAllCompaniesCommand());
        commands.put("POST /companies/add", new AddCompanyCommand());
        commands.put("POST /companies/delete", new DeleteCompanyCommand());
        commands.put("GET /customers/all_customers", new ListAllCustomersCommand());
        commands.put("POST /customers/add", new AddCustomerCommand());
        commands.put("POST /customers/delete", new DeleteCustomerCommand());
    }

    public void process(HttpServletRequest req, HttpServletResponse resp, TemplateEngine engine) throws IOException {
        String requestUri = req.getRequestURI();
        String commandKey = req.getMethod() + " " + requestUri;
        commands.get(commandKey).process(req, resp, engine);
    }

}
