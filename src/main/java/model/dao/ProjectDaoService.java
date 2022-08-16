package model.dao;

import model.dbConnection.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ProjectDaoService {
    public static ProjectDaoService INSTANCE;
    public static List<Project> projects = new ArrayList<>();

    private PreparedStatement getAllInfoSt;
    private PreparedStatement getAllNamesSt;
    private PreparedStatement getCompanyNameByProjectNameSt;
    private PreparedStatement getCustomerNameByProjectNameSt;
    private PreparedStatement getCostDateSt;
    private PreparedStatement getListDevelopersSt;
    private PreparedStatement getQuantityDevelopersByProjectNameSt;
    private PreparedStatement getBudgetByProjectNameSt;
    private PreparedStatement getIdProjectByNameSt;
    private PreparedStatement selectMaxIdSt;
    private  PreparedStatement addProjectSt;
    private PreparedStatement existsByIdSt;
    private PreparedStatement getIdByNameSt;
    private PreparedStatement deleteProjectFromProjectsByIdSt;
    private  PreparedStatement deleteProjectFromProjectDevelopersByIdSt;


    private ProjectDaoService(Connection connection) throws SQLException {
        PreparedStatement getAllInfoSt = connection.prepareStatement(
                "SELECT * FROM projects"
        );
        try (ResultSet rs = getAllInfoSt.executeQuery()) {
            while (rs.next()) {
                Project project = new Project();
                project.setProject_id(rs.getLong("project_id"));
                project.setProject_name(rs.getString("project_name"));
                project.setCompany_id(rs.getLong("company_id"));
                project.setCustomer_id(rs.getLong("customer_id"));
                project.setCost(rs.getInt("cost"));
                String startDate = rs.getString("start_date");
                if (startDate != null) {
                    project.setStart_date(LocalDate.parse(startDate));
                }
                projects.add(project);
            }
        }

        getAllNamesSt = connection.prepareStatement(
                " SELECT project_id, project_name, start_date FROM projects"
        );

        getCompanyNameByProjectNameSt = connection.prepareStatement(
                " SELECT company_name FROM projects JOIN companies " +
                        "ON projects.company_id = companies.company_id " +
                        " WHERE  project_name LIKE   ?"
        );

        getCustomerNameByProjectNameSt = connection.prepareStatement(
                " SELECT customer_name FROM projects JOIN customers " +
                        "ON projects.customer_id = customers.customer_id " +
                        " WHERE  project_name LIKE   ?"
        );

        getCostDateSt = connection.prepareStatement(
                " SELECT cost, start_date FROM projects " +
                        " WHERE  project_name LIKE   ?"
        );

        getListDevelopersSt = connection.prepareStatement(
                "SELECT lastName, firstName FROM projects JOIN projects_developers " +
                        "ON projects.project_id = projects_developers.project_id " +
                        "JOIN developers ON projects_developers.developer_id = developers.developer_id " +
                        "WHERE project_name  LIKE ?"
        );

        getQuantityDevelopersByProjectNameSt = connection.prepareStatement(
                "SELECT COUNT(developer_id) FROM projects JOIN projects_developers " +
                        "ON projects.project_id = projects_developers.project_id " +
                        " WHERE project_name  LIKE  ?"
        );

        getBudgetByProjectNameSt = connection.prepareStatement(
                "SELECT SUM(salary) FROM projects JOIN projects_developers " +
                        "ON projects.project_id = projects_developers.project_id " +
                        "JOIN developers ON projects_developers.developer_id = developers.developer_id " +
                        " WHERE project_name  LIKE  ?"
        );

        getIdProjectByNameSt = connection.prepareStatement(
                "SELECT project_id FROM projects " +
                        "WHERE project_name  LIKE  ?"
        );

        selectMaxIdSt = connection.prepareStatement(
                "SELECT max(project_id) AS maxId FROM projects"
        );

        addProjectSt = connection.prepareStatement(
                "INSERT INTO projects  VALUES ( ?, ?, ?, ?, ?, ?)");

        existsByIdSt = connection.prepareStatement(
                "SELECT count(*) > 0 AS projectExists FROM projects WHERE project_id = ?"
        );

        getIdByNameSt = connection.prepareStatement(
                "SELECT project_id FROM projects WHERE project_name LIKE ? "
        );

        deleteProjectFromProjectsByIdSt = connection.prepareStatement(
                "DELETE FROM projects WHERE project_id = ?"
        );

        deleteProjectFromProjectDevelopersByIdSt = connection.prepareStatement(
                "DELETE FROM projects_developers WHERE project_id = ?"
        );

    }

    public static ProjectDaoService  getInstance(Connection connection) {
        if (INSTANCE == null) {
            try {
                INSTANCE = new ProjectDaoService(DBConnection.getInstance().getConnection());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return INSTANCE;
    }

    public List<String> getAllNames() throws SQLException {
       List<String> lines = new ArrayList<>();
        try (ResultSet rs = getAllNamesSt.executeQuery()) {
            while (rs.next()) {
                long projectID = rs.getLong("project_id");
                String projectName = rs.getString("project_name");
                lines.add( projectID + ". " + projectName);
            }
            return lines;
        }
    }


    public List<String> getInfoByName(String name ) throws SQLException {
        List<String> lines = new ArrayList<>();
        getCustomerNameByProjectNameSt.setString(1,  name );
        try (ResultSet rs1 = getCustomerNameByProjectNameSt.executeQuery()) {
            while (rs1.next()) {
                lines.add("ordered by the customer   " + rs1.getString("customer_name") + ",");
            }
        }
        getCompanyNameByProjectNameSt.setString(1, name);
        try (ResultSet rs2 = getCompanyNameByProjectNameSt.executeQuery()) {
            while (rs2.next()) {
                lines.add("developed by the IT company " + rs2.getString("company_name") + ",");
            }
        }
        getCostDateSt.setString(1, name);
        try (ResultSet rs = getCostDateSt.executeQuery()) {
            while (rs.next()) {
                lines.add("has budget " + rs.getInt("cost")+ ",");
                lines.add("launched date " + LocalDate.parse(rs.getString("start_date")) + ".");
            }
        }
        return lines;
    }

    public List<String> getListDevelopers (String name) throws SQLException {
        getListDevelopersSt.setString(1, name);
        List<String> lines = new ArrayList<>();
        try (ResultSet rs1 = getListDevelopersSt.executeQuery()) {
            while (rs1.next()) {
                lines.add(rs1.getString("lastName") + " " + rs1.getString("firstName") );
            }
        }
        return lines;
    }

    public void getQuantityDevelopers (String name) throws SQLException {
        getQuantityDevelopersByProjectNameSt.setString(1, name);
        try (ResultSet rs1 = getQuantityDevelopersByProjectNameSt.executeQuery()) {
            while (rs1.next()) {
                System.out.println("\t\tВ данном проекте задействовано " +  rs1.getInt("COUNT(developer_id)") + " разработчика(ов)");
            }
        }
    }

    public List<String> getBudgetByProjectName (String name) throws SQLException {
        List<String> lines = new ArrayList<>();
        getBudgetByProjectNameSt.setString(1, name);
        try (ResultSet rs1 = getBudgetByProjectNameSt.executeQuery()) {
            while (rs1.next()) {
                lines.add(rs1.getInt("SUM(salary)") + " USD.");
            }
        }
        return lines;
    }

    public List<String> getProjectsListInSpecialFormat () throws SQLException {
        List<String> lines = new ArrayList<>();

        try (ResultSet rs = getAllNamesSt.executeQuery()) {
            while (rs.next()) {
                StringBuilder line = new StringBuilder("");
                line.append(LocalDate.parse(rs.getString("start_date")));
                String projectName = rs.getString("project_name");
                line.append(" - " + projectName);
                getQuantityDevelopersByProjectNameSt.setString(1, projectName);
                try (ResultSet rs1 = getQuantityDevelopersByProjectNameSt.executeQuery()) {
                    while (rs1.next()) {
                        line.append(" -  " + rs1.getInt("COUNT(developer_id)") + " developers");
                    }
                }
                lines.add(line.toString());
            }
        }
        return lines;
    }

    public long getIdProjectByName(String name) throws SQLException {
        getIdProjectByNameSt.setString(1, "%" + name + "%");
        int result = 0;
        try (ResultSet rs = getIdProjectByNameSt.executeQuery()) {
            rs.next();
            result = rs.getInt("project_id");
        }
        return result;
    }

    public List<String> addProject(TemporaryProject temporaryProject) throws SQLException {
        List<String> lines = new ArrayList<>();
        long newProjectId;
        try(ResultSet rs = selectMaxIdSt.executeQuery()) {
            rs.next();
            newProjectId = rs.getLong("maxId");
        }
        newProjectId++;
        addProjectSt.setLong(1, newProjectId);
        addProjectSt.setString(2, temporaryProject.getProjectName());

        boolean isCompanyNameCorrect = false;
        List<Company> companies = CompanyDaoService.companies;
        for (Company companyFromField : companies) {
            if (companyFromField.getCompany_name().equals(temporaryProject.getCompanyName())) {
                isCompanyNameCorrect = true;
            };
        }
        if (!isCompanyNameCorrect) {
            lines.add("There is no company with such name. Please input this into database in the section \"companies\" ");
            return lines;
        }
        long companyId = CompanyDaoService.getInstance(
                DBConnection.getInstance().getConnection()).getIdCompanyByName(temporaryProject.getCompanyName());
        addProjectSt.setLong(3, companyId);

        boolean isCustomerNameCorrect = false;
        List<Customer> customers = CustomerDaoService.customers;
        for (Customer customerFromField : customers) {
            if (customerFromField.getCustomer_name().equals(temporaryProject.getCustomerName())) {
                isCustomerNameCorrect = true;
            };
        }
        if (!isCustomerNameCorrect) {
            lines.add("There is no customer with such name. Please input this into database in the section \"customers\" ");
            return lines;
        }
        long customerId = CustomerDaoService.getInstance(
                DBConnection.getInstance().getConnection()).getIdCustomerByName(temporaryProject.getCompanyName());
        addProjectSt.setLong(4, customerId);
        addProjectSt.setInt(5, temporaryProject.getProjectCost());
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
        LocalDate startLocalDate = LocalDate.parse(temporaryProject.getStartDate(), dtf);
        java.sql.Date startSqlDate = java.sql.Date.valueOf(startLocalDate);
        addProjectSt.setDate(6, startSqlDate);
        addProjectSt.executeUpdate();

        Project project = new Project();
        project.setProject_id(newProjectId);
        project.setProject_name(temporaryProject.getProjectName());
        project.setCompany_id(companyId);
        project.setCustomer_id(customerId);
        project.setCost(temporaryProject.getProjectCost());
        project.setStart_date(startLocalDate);
        projects.add(project);
        if (existsProject(newProjectId)) lines.add("was successfully added");
        else lines.add("Something went wrong and the developer was not added to the database");
        return lines;
    }

    public boolean existsProject(long id) throws SQLException {
        existsByIdSt.setLong(1, id);
        try(ResultSet rs = existsByIdSt.executeQuery()) {
            rs.next();
            return rs.getBoolean("projectExists");
        }
    }

    public long getIdByName(String name) throws SQLException {
        long id=0;
        getIdByNameSt.setString(1, "%" + name + "%");
        try (ResultSet rs = getIdByNameSt.executeQuery()) {
            rs.next();
            id = rs.getInt("project_id");
        }
        return id;
    }

    public void deleteProject(String name) throws SQLException {
        long idToDelete = getIdByName(name);
        deleteProjectFromProjectDevelopersByIdSt.setLong(1, idToDelete);
        deleteProjectFromProjectDevelopersByIdSt.executeUpdate();
        deleteProjectFromProjectsByIdSt.setLong(1, idToDelete);
        deleteProjectFromProjectsByIdSt.executeUpdate();
        projects.removeIf(nextProject -> nextProject.getProject_id() == idToDelete);
        if (!existsProject(idToDelete)) { System.out.println("Проект успешно удален из базы данных.");}
        else {
            System.out.println("Что-то пошло не так и проект не был удален из базы данных");
        }
    }

    public void deleteProject(long id) throws SQLException {
        deleteProjectFromProjectDevelopersByIdSt.setLong(1, id);
        deleteProjectFromProjectDevelopersByIdSt.executeUpdate();
        deleteProjectFromProjectsByIdSt.setLong(1, id);
        deleteProjectFromProjectsByIdSt.executeUpdate();
        projects.removeIf(nextProject -> nextProject.getProject_id() == id);
        if (!existsProject(id)) { System.out.println("Проект успешно удален из базы данных.");}
        else {
            System.out.println("Что-то пошло не так и проект не был удален из базы данных");
        }
    }
}
