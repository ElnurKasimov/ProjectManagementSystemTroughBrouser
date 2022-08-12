package model.dao;

import java.sql.*;
import java.util.*;

public class DeveloperDaoService {
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


    public DeveloperDaoService(Connection connection) throws SQLException {
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





    public List<String> getAllNames() throws SQLException {
        List<String> lines = new ArrayList<>();
        try (ResultSet rs = getInfoForAllDevelopers.executeQuery()) {
            while (rs.next()) {
                lines.add(rs.getLong("developer_id") + ". " + rs.getString("firstName") + " " + rs.getString("lastName"));
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
                lines.add("id " + id);
                lines.add(", Возраст -  " + rs.getInt("age")+ ", ");
                lines.add("Работает в компании -  " + rs.getString("company_name") + ", ");
                lines.add("Зарплата -  " + rs.getInt("salary")+ "; ");
                List<String> temporatyList1 = getSkillsById(id);
                for (String line: temporatyList1) {
                    lines.add(line);
                }
                List<String> temporatyList2 = getProjectsById(id);
                for (String line: temporatyList2) {
                    lines.add(line);
                }
            }
        }
        return lines;
    }

    public List<String> getSkillsById(long id) throws SQLException {
        getSkillsByIdSt.setLong(1, id);
        List<String> languageKnowledge = new ArrayList<>();
        languageKnowledge.add("\tВладеет языками: ");
        try (ResultSet rs = getSkillsByIdSt.executeQuery()) {
            while (rs.next()) {
                languageKnowledge.add("\t\t" + rs.getString("language") + " -  " + rs.getString("level") + ";  ");
            }
        }
        return languageKnowledge;
    }

    public List<String> getProjectsById(long id) throws SQLException {
        getProjectsByIdSt.setLong(1, id);
        List<String> projectsParticipation = new ArrayList<>();
        projectsParticipation.add("\tУчаствует в проектах: ");
        try (ResultSet rs = getProjectsByIdSt.executeQuery()) {
            while (rs.next()) {
                projectsParticipation.add("\t\t" + rs.getString("project_name") + ".");
            }
        }
        return projectsParticipation;
    }

    public void getQuantityJavaDevelopers() throws SQLException {
        int count = 0;
        try (ResultSet rs = getQuantityJavaDevelopersSt.executeQuery()) {
            rs.next();
            count = rs.getInt("quantityLanguageDevelopers");
        }
        System.out.println("\tВо всех компаниях работат  " + count  + " Java-разработчиков");
    }

    public void getListMiddleDevelopers() throws SQLException {
        System.out.println("\tCписок всех разработчиков с уровнем знания языка middle: ");
        try (ResultSet rs = getListMiddleDevelopersSt.executeQuery()) {
            while (rs.next()) {
                System.out.print(rs.getString("lastName" ) + " " +  rs.getString("firstName"));
                System.out.println(",  язык - " + rs.getString("language"));
            }
        }
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

    public int addDeveloper(String lastName, String firstName) throws SQLException {
/*
        long newDeveloperId;
        try(ResultSet rs = selectMaxIdSt.executeQuery()) {
            rs.next();
            newDeveloperId = rs.getLong("maxId");
        }
        newDeveloperId++;
        addDeveloperSt.setLong(1, newDeveloperId);
        addDeveloperSt.setString(2, lastName);
        addDeveloperSt.setString(3, firstName);
        Scanner sc5 = new Scanner(System.in);
        System.out.print("\tВозраст разработчика : ");
        int age = sc5.nextInt();
        addDeveloperSt.setInt(4, age);
        System.out.print("\tНазвание компании, в которой он работает : ");
        String company = sc5.nextLine();
        if(company.equals("")) company= sc5.nextLine();
        boolean isCompanyNameCorrect = false;
        for (Company companyFromField : CompanyDaoService.companies) {
            if (companyFromField.getCompany_name().equals(company)) {
                isCompanyNameCorrect = true;
            };
        }
        if (!isCompanyNameCorrect) {
            System.out.println("Компании с таким именем не существует. Введите корректные данные или внесите эту компанию в базу данных в разделе \"companies\" ");
            return -1;
        }
        long companyId = new CompanyDaoService(Storage.getInstance().getConnection()).getIdCompanyByName(company);
        addDeveloperSt.setLong(5, companyId);
        System.out.println("\tЭта компания разрабатывает следующие проекты:");
        ArrayList<String> companyProjects = new CompanyDaoService(Storage.getInstance().getConnection()).getCompanyProjects(company);
        for (String project : companyProjects) {
            System.out.println("\t\t" + project);
        }
        System.out.print("Укажите тот, в котором участвует этот разработчик : ");
        String project = sc5.nextLine();
        long projectId = new ProjectDaoService(Storage.getInstance().getConnection()).getIdProjectByName(project);
        addProjectDeveloperSt.setLong(1, projectId);
        addProjectDeveloperSt.setLong(2, newDeveloperId);
        System.out.print("\tЗарплата разработчика : ");
        int salary = sc5.nextInt();
        addDeveloperSt.setInt(6, salary);
        Developer developer = new Developer();
        developer.setDeveloper_id(newDeveloperId);
        developer.setLastName(lastName);
        developer.setFirstName(firstName);
        developer.setAge(age);
        developer.setCompany_id(companyId);
        developer.setSalary(salary);
        developers.add(developer);
        System.out.print("\tКаким языком владеет (Java, JS, C++, PHP) : ");
        String language = sc5.nextLine();
        if(language.equals("")) language= sc5.nextLine();
        System.out.print("\tУровень знания языка (junior, middle, senior) : ");
        String level = sc5.nextLine();
        getIdSkillByLanguageAndLevelSt.setString( 1, "%" + language + "%");
        getIdSkillByLanguageAndLevelSt.setString( 2, "%" + level + "%");
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
        if (existsDeveloper(newDeveloperId)) {System.out.println("Разработчик успешно добавлен");}
        else System.out.println("Что-то пошло не так и разработчик не был добавлен в базу данных");
 */
        return +1;
    }

    public boolean existsDeveloper(long id) throws SQLException {
        existsByIdSt.setLong(1, id);
        try(ResultSet rs = existsByIdSt.executeQuery()) {
            rs.next();
            return rs.getBoolean("developerExists");
        }
    }

    public void deleteDeveloper(String lastName, String firstName) throws SQLException {
        long idToDelete = getIdByName( lastName, firstName);
        deleteDeveloperFromProjectDevelopersByIdSt.setLong(1, idToDelete);
        deleteDeveloperFromProjectDevelopersByIdSt.executeUpdate();
        deleteDeveloperFromDevelopersSkillsByIdSt.setLong(1, idToDelete);
        deleteDeveloperFromDevelopersSkillsByIdSt.executeUpdate();
        deleteDeveloperFromDevelopersByIdSt.setLong(1, idToDelete);
        deleteDeveloperFromDevelopersByIdSt.executeUpdate();
        developers.removeIf(nextDeveloper -> nextDeveloper.getDeveloper_id() == idToDelete);
        if (!existsDeveloper(idToDelete)) { System.out.println("Разработчик успешно удален из базы данных.");}
        else {
            System.out.println("Что-то пошло не так и разработчик не был удален из базы данных");
        }
    }

    public void deleteDeveloper(long id) throws SQLException {
        deleteDeveloperFromProjectDevelopersByIdSt.setLong(1, id);
        deleteDeveloperFromProjectDevelopersByIdSt.executeUpdate();
        deleteDeveloperFromDevelopersSkillsByIdSt.setLong(1, id);
        deleteDeveloperFromDevelopersSkillsByIdSt.executeUpdate();
        deleteDeveloperFromDevelopersByIdSt.setLong(1, id);
        deleteDeveloperFromDevelopersByIdSt.executeUpdate();
        developers.removeIf(nextDeveloper -> nextDeveloper.getDeveloper_id() == id);
        if (!existsDeveloper(id)) { System.out.println("Разработчик успешно удален из базы данных.");}
        else {
            System.out.println("Что-то пошло не так и разработчик не был удален из базы данных");
        }
    }
}
