package model.command.developers;

import model.command.Command;
import model.dao.DeveloperDaoService;
import model.default_settings.DBConnection;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

public class GetInformationAboutAllDevelopersCommand  implements Command {
    @Override
    public void process(HttpServletRequest req, HttpServletResponse resp, TemplateEngine engine) throws IOException {
        //todo
        // 1. создать темплейт  developers_list_all_developers  для вывода всех разрабов
        // 2. подкорректировать getAllNames() в DaoDeveloperService.

        try {
            DeveloperDaoService developerDaoService = new DeveloperDaoService(DBConnection.getInstance().getConnection());

            resp.setContentType("text/html");

            Context context = new Context(
                    req.getLocale(),
                    Map.of("developers", developerDaoService.getAllNames())
            );

            engine.process("developers_list_all_developers", context, resp.getWriter());
            resp.getWriter().close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
