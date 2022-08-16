package model.dao;

import model.dbConnection.DBConnection;

import java.sql.*;
import java.util.*;

public class DeveloperDaoService {
    public static DeveloperDaoService INSTANCE;
    public static  List<Developer> developers = new ArrayList<>();
    private PreparedStatement getAllLastNamesSt;
    private PreparedStatement getInfoForAllDevelopers;
    private PreparedStatement getInfoByNameSt;
    private PreparedStatement getSkillsByIdSt;
    private PreparedStatement getProjectsByIdSt;
    private PreparedStatement getQuantityJavaDevelopersSt;
    private PreparedStatement getListMiddleDevelopersSt;
    private PreparedStatement selectMaxIdSt;
    private PreparedStatement addDeveloperSt;
    private PreparedStatement addProjectDeveloperSt;
    private PreparedStatement getIdSkillByLanguageAndLevelSt;
    private PreparedStatement addDeveloperSkillSt;
    private PreparedStatement existsByIdSt;
    private PreparedStatement getIdByNameSt;
    private PreparedStatement deleteDeveloperFromDevelopersByIdSt;
    private PreparedStatement deleteDeveloperFromProjectDevelopersByIdSt;
    private PreparedStatement deleteDeveloperFromDevelopersSkillsByIdSt;

    private  DeveloperDaoService(Connection connection) throws SQLException {
        PreparedStatement getAllInfoSt = connection.prepareStatement(
                "SELECT * FROM developers"
        );
        try (ResultSet rs = getAllInfoSt.executeQuery()) {

            while (rs.next()) {
                Developer developer = new Developer();
                developer.setDeveloper_id(rs.getLong("developer_id"));
                developer.setLastName(rs.getString("lastName"));
                if (rs.getString("firstName") != null) {
                    developer.setFirstName(rs.getString("firstName"));
                }
                developer.setAge(rs.getInt("age"));
                developer.setCompany_id(rs.getInt("company_id"));
                developer.setSalary(rs.getInt("salary"));
                developers.add(developer);
            }
        }

        getInfoForAllDevelopers = connection.prepareStatement(
                "SELECT developer_id, firstName, lastName FROM developers"
        );
        getAllLastNamesSt = connection.prepareStatement(
                "SELECT lastName FROM developers"
        );

        getInfoByNameSt = connection.prepareStatement(
                "SELECT lastName, firstName, age, salary, company_name " +
                        "FROM developers JOIN companies " +
                        "ON developers.company_id = companies.company_id" +
                        " WHERE lastName  LIKE ? AND firstName LIKE ?"
        );

        getSkillsByIdSt = connection.prepareStatement(
                "SELECT language, level " +
                        "FROM developers JOIN developers_skills " +
                        "ON developers.developer_id = developers_skills.developer_id " +
                        "JOIN skills ON developers_skills.skill_id = skills.skill_id " +
                        "WHERE developers.developer_id = ?"
        );

        getProjectsByIdSt = connection.prepareStatement(
                "SELECT  project_name " +
                        "FROM developers JOIN projects_developers " +
                        "ON developers.developer_id = projects_developers.developer_id " +
                        "JOIN projects ON projects_developers.project_id = projects.project_id " +
                        "WHERE developers.developer_id = ?"
        );

        getQuantityJavaDevelopersSt = connection.prepareStatement(
                "SELECT COUNT(language) AS  quantityLanguageDevelopers " +
                        "FROM developers JOIN developers_skills " +
                        "ON developers.developer_id = developers_skills.developer_id " +
                        "JOIN skills ON developers_skills.skill_id = skills.skill_id " +
                        "WHERE language = 'Java'"
        );

        getListMiddleDevelopersSt = connection.prepareStatement(
                "SELECT lastName, firstName, language " +
                        "FROM developers JOIN developers_skills " +
                        "ON developers.developer_id = developers_skills.developer_id " +
                        "JOIN skills ON developers_skills.skill_id = skills.skill_id " +
                        "WHERE level = 'middle'"
        );

        selectMaxIdSt = connection.prepareStatement(
                "SELECT max(developer_id) AS maxId FROM developers"
        );

        addDeveloperSt = connection.prepareStatement(
                "INSERT INTO developers  VALUES ( ?, ?, ?, ?, ?, ?)");

        addProjectDeveloperSt = connection.prepareStatement(
                "INSERT INTO projects_developers  VALUES ( ?, ?)");

        getIdSkillByLanguageAndLevelSt  = connection.prepareStatement(
                "SELECT skill_id FROM skills WHERE language LIKE ? AND level LIKE ?");

        addDeveloperSkillSt = connection.prepareStatement(
                "INSERT INTO developers_skills  VALUES ( ?, ?)");

        existsByIdSt = connection.prepareStatement(
                "SELECT count(*) > 0 AS developerExists FROM developers WHERE developer_id = ?"
        );

        getIdByNameSt = connection.prepareStatement(
                "SELECT developer_id FROM developers WHERE lastName LIKE ? AND firstName LIKE ?"
        );

        deleteDeveloperFromDevelopersByIdSt = connection.prepareStatement(
                "DELETE FROM developers WHERE developer_id = ?"
        );

        deleteDeveloperFromProjectDevelopersByIdSt = connection.prepareStatement(
                "DELETE FROM projects_developers WHERE developer_id = ?"
        );

        deleteDeveloperFromDevelopersSkillsByIdSt = connection.prepareStatement(
                "DELETE FROM developers_skills WHERE developer_id = ?"
        );
    }

    public static DeveloperDaoService  getInstance(Connection connection) {
        if (INSTANCE == null) {
            try {
                INSTANCE = new DeveloperDaoService(DBConnection.getInstance().getConnection());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return INSTANCE;
    }

    public List<String> getAllNames() throws SQLException {
        List<String> lines = new ArrayList<>();
        try (ResultSet rs = getInfoForAllDevelopers.executeQuery()) {
            while (rs.next()) {
                lines.add(rs.getLong("developer_id") + ". "
                        + rs.getString("firstName")

                        + " " + rs.getString("lastName"));
            }
        }
        return lines;
    }

    public List<String> getInfoByName(String lastName, String firstName ) throws SQLException {
        getInfoByNameSt.setString(1, "%" + lastName + "%");
        getInfoByNameSt.setString(2, "%" + firstName + "%");
        List<String> lines = new ArrayList<>();
        try (ResultSet rs = getInfoByNameSt.executeQuery()) {
            while (rs.next()) {
                long id = getIdByName(lastName, firstName);
                lines.add(" id " + id + ",");
                lines.add(" Age -  " + rs.getInt("age")+ ", ");
                lines.add(" Works in company -  " + rs.getString("company_name") + ", ");
                lines.add(" Salary -  " + rs.getInt("salary")+ "; ");
                List<String> temporatyList1 = getSkillsById(id);
                lines.addAll(temporatyList1);
                List<String> temporatyList2 = getProjectsById(id);
                lines.addAll(temporatyList2);
            }
        }
        return lines;
    }

    public List<String> getSkillsById(long id) throws SQLException {
        getSkillsByIdSt.setLong(1, id);
        List<String> languageKnowledge = new ArrayList<>();
        languageKnowledge.add("   Programming in such languages: ");
        try (ResultSet rs = getSkillsByIdSt.executeQuery()) {
            while (rs.next()) {
                languageKnowledge.add(" - " + rs.getString("language") + " -  " + rs.getString("level") + ";  ");
            }
        }
        return languageKnowledge;
    }

    public List<String> getProjectsById(long id) throws SQLException {
        getProjectsByIdSt.setLong(1, id);
        List<String> projectsParticipation = new ArrayList<>();
        projectsParticipation.add("   Takes part in the projects: ");
        try (ResultSet rs = getProjectsByIdSt.executeQuery()) {
            while (rs.next()) {
                projectsParticipation.add(" - " + rs.getString("project_name") + ";");
            }
        }
        return projectsParticipation;
    }

    public List<String>  getQuantityJavaDevelopers() throws SQLException {
        int count = 0;
        List<String> lines = new ArrayList<>();
        try (ResultSet rs = getQuantityJavaDevelopersSt.executeQuery()) {
            rs.next();
            count = rs.getInt("quantityLanguageDevelopers");
        }
        lines.add(" In all companies works  " + count  + " Java-developers");
        return lines;
    }

    public List<String> getListMiddleDevelopers() throws SQLException {
        List<String> lines = new ArrayList<>();
        lines.add(" List of all developers with language knowledge level \"middle\": ");
        lines.add("--");
        try (ResultSet rs = getListMiddleDevelopersSt.executeQuery()) {
            while (rs.next()) {
                lines.add(rs.getString("lastName" ) + " " +  rs.getString("firstName") +
                       ",  language - " + rs.getString("language"));
            }
        }
        return lines;
    }

    public long getIdByName(String lastName, String firstName) throws SQLException {
        long id=0;
        getIdByNameSt.setString(1, "%" + lastName + "%");
        getIdByNameSt.setString(2, "%" + firstName + "%");
        try (ResultSet rs = getIdByNameSt.executeQuery()) {
            rs.next();
            id = rs.getInt("developer_id");
        }
        return id;
    }

    public List<String> addDeveloper(TemporaryDeveloper temporaryDeveloper) throws SQLException {
        List<String> lines = new ArrayList<>();
        long newDeveloperId;
        try(ResultSet rs = selectMaxIdSt.executeQuery()) {
            rs.next();
            newDeveloperId = rs.getLong("maxId");
        }
        newDeveloperId++;
        addDeveloperSt.setLong(1, newDeveloperId);
        addDeveloperSt.setString(2, temporaryDeveloper.getLastName());
        addDeveloperSt.setString(3, temporaryDeveloper.getFirstName());
        addDeveloperSt.setInt(4, temporaryDeveloper.getAge());
        boolean isCompanyNameCorrect = false;
        List<Company> companies = CompanyDaoService.companies;
        for (Company companyFromField : CompanyDaoService.companies) {
            if (companyFromField.getCompany_name().equals(temporaryDeveloper.getCompanyName())) {
                isCompanyNameCorrect = true;
            };
        }
        if (!isCompanyNameCorrect) {
            lines.add("There is no company with such name. Please input this into database in the section \"companies\" ");
            return lines;
        }
        long companyId = CompanyDaoService.getInstance(
                DBConnection.getInstance().getConnection()).getIdCompanyByName(temporaryDeveloper.getCompanyName());
        addDeveloperSt.setLong(5, companyId);
        boolean isProjectNameCorrect = false;
        ArrayList<String> companyProjects = CompanyDaoService.getInstance(
                DBConnection.getInstance().getConnection()).getCompanyProjects(temporaryDeveloper.getCompanyName());
        for (String project : companyProjects) {
            if (project.equals(temporaryDeveloper.getProjectName())) {
                isProjectNameCorrect = true;
            };
        }
        if (!isProjectNameCorrect) {
            lines.add("This company doesn't develop project with such name. You can find info about all projects in the section \"projects\" ");
            return lines;
        }
        long projectId = ProjectDaoService.getInstance(
                DBConnection.getInstance().getConnection()).getIdProjectByName(temporaryDeveloper.getProjectName());
        addProjectDeveloperSt.setLong(1, projectId);
        addProjectDeveloperSt.setLong(2, newDeveloperId);
        addDeveloperSt.setInt(6, temporaryDeveloper.getSalary());
        Developer developer = new Developer();
        developer.setDeveloper_id(newDeveloperId);
        developer.setLastName(temporaryDeveloper.getLastName());
        developer.setFirstName(temporaryDeveloper.getFirstName());
        developer.setAge(temporaryDeveloper.getAge());
        developer.setCompany_id(companyId);
        developer.setSalary(temporaryDeveloper.getSalary());
        developers.add(developer);
        getIdSkillByLanguageAndLevelSt.setString( 1, "%" + temporaryDeveloper.getLanguage() + "%");
        getIdSkillByLanguageAndLevelSt.setString( 2, "%" + temporaryDeveloper.getLanguageLevel() + "%");
        long skillId;
        try(ResultSet rs = getIdSkillByLanguageAndLevelSt.executeQuery()) {
            rs.next();
            skillId = rs.getLong("skill_id");
        }
        addDeveloperSkillSt.setLong(1, newDeveloperId);
        addDeveloperSkillSt.setLong(2, skillId);
        addDeveloperSt.executeUpdate();
        addProjectDeveloperSt.executeUpdate();
        addDeveloperSkillSt.executeUpdate();

        if (existsDeveloper(newDeveloperId)) lines.add("was successfully added");
        else lines.add("Something went wrong and the developer was not added to the database");
        return lines;
    }

    public boolean existsDeveloper(long id) throws SQLException {
        existsByIdSt.setLong(1, id);
        try(ResultSet rs = existsByIdSt.executeQuery()) {
            rs.next();
            return rs.getBoolean("developerExists");
        }
    }

    public List<String> updateDeveloper(TemporaryDeveloper temporaryDeveloper) {
        List<String> lines = new ArrayList<>();
        long idToDelete;
        try {
            idToDelete = DeveloperDaoService.getInstance(DBConnection.getInstance().getConnection()).
                                     getIdByName(temporaryDeveloper.getLastName(), temporaryDeveloper.getFirstName());
            DeveloperDaoService.getInstance(DBConnection.getInstance().getConnection()).deleteDeveloper(idToDelete);

            addDeveloper(temporaryDeveloper);
           lines.add("was successfully updated");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lines;
    }

    public List<String> deleteDeveloper(String lastName, String firstName) throws SQLException {
        List<String> lines = new ArrayList<>();
        long idToDelete = getIdByName( lastName, firstName);
        deleteDeveloperFromProjectDevelopersByIdSt.setLong(1, idToDelete);
        deleteDeveloperFromProjectDevelopersByIdSt.executeUpdate();
        deleteDeveloperFromDevelopersSkillsByIdSt.setLong(1, idToDelete);
        deleteDeveloperFromDevelopersSkillsByIdSt.executeUpdate();
        deleteDeveloperFromDevelopersByIdSt.setLong(1, idToDelete);
        deleteDeveloperFromDevelopersByIdSt.executeUpdate();
        developers.removeIf(nextDeveloper -> nextDeveloper.getDeveloper_id() == idToDelete);
        if (!existsDeveloper(idToDelete)) {lines.add("Successfully removed from the database.");}
        else {
            lines.add("Something went wrong and the developer was not removed to the database.");
        }
        return lines;
    }

    public void deleteDeveloper(long id) throws SQLException {
        deleteDeveloperFromProjectDevelopersByIdSt.setLong(1, id);
        deleteDeveloperFromProjectDevelopersByIdSt.executeUpdate();
        deleteDeveloperFromDevelopersSkillsByIdSt.setLong(1, id);
        deleteDeveloperFromDevelopersSkillsByIdSt.executeUpdate();
        deleteDeveloperFromDevelopersByIdSt.setLong(1, id);
        deleteDeveloperFromDevelopersByIdSt.executeUpdate();
        developers.removeIf(nextDeveloper -> nextDeveloper.getDeveloper_id() == id);
    }

}
