package model.dao;


import model.dbConnection.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CompanyDaoService {
    public static CompanyDaoService INSTANCE;

    public static List<Company> companies = new ArrayList<>();

    private PreparedStatement getAllInfoSt;
    private PreparedStatement getAllNamesSt;
    private PreparedStatement getQuantityEmployeeSt;
    private PreparedStatement getIdCompanyByNameSt;
    private PreparedStatement getCompanyProjectsSt;
    private PreparedStatement selectMaxIdSt;
    private PreparedStatement addCompanySt;
    private PreparedStatement existsByIdSt;
    private PreparedStatement deleteCompanyFromCompaniesByNameSt;

    private CompanyDaoService(Connection connection) throws SQLException {
        getAllInfoSt = connection.prepareStatement("SELECT * FROM companies");
        try (ResultSet rs = getAllInfoSt.executeQuery()) {
            while (rs.next()) {
                Company company = new Company();
                company.setCompany_id(rs.getLong("company_id"));
                if (rs.getString("company_name") != null) {
                    company.setCompany_name(rs.getString("company_name"));
                }
                company.setRating(Company.Rating.valueOf(rs.getString("rating")));
                companies.add(company);
            }
        }

        getAllNamesSt = connection.prepareStatement(
                " SELECT company_id, company_name, rating FROM companies"
        );
        getQuantityEmployeeSt = connection.prepareStatement(
                " SELECT COUNT(developer_id) FROM companies " +
                        "JOIN developers ON companies.company_id = developers.company_id " +
                        " WHERE company_name  LIKE  ?"
        );

        getQuantityEmployeeSt = connection.prepareStatement(
                " SELECT COUNT(developer_id) FROM companies " +
                        "JOIN developers ON companies.company_id = developers.company_id " +
                        " WHERE company_name  LIKE  ?"
        );

        getIdCompanyByNameSt = connection.prepareStatement(
                "SELECT company_id FROM companies " +
                        "WHERE company_name  LIKE  ?"
        );

        getCompanyProjectsSt = connection.prepareStatement(
                "SELECT project_name FROM projects JOIN companies " +
                        "ON projects.company_id = companies.company_id " +
                        "WHERE company_name  LIKE ?"
        );

        selectMaxIdSt = connection.prepareStatement(
                "SELECT max(company_id) AS maxId FROM companies"
        );

        addCompanySt = connection.prepareStatement(
                "INSERT INTO companies  VALUES ( ?, ?, ?)");

        existsByIdSt = connection.prepareStatement(
                "SELECT count(*) > 0 AS companyExists FROM companies WHERE company_id = ?"
        );

        deleteCompanyFromCompaniesByNameSt = connection.prepareStatement(
                "DELETE FROM companies WHERE company_name LIKE  ?"
        );

    }

    public static CompanyDaoService  getInstance(Connection connection) {
        if (INSTANCE == null) {
            try {
                INSTANCE = new CompanyDaoService(DBConnection.getInstance().getConnection());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return INSTANCE;
    }

    public void getAllNames() throws SQLException {
        System.out.println("Список всех IT компаний :");
        try (ResultSet rs = getAllNamesSt.executeQuery()) {
            while (rs.next()) {
                long companyID = rs.getLong("company_id");
                String companyName = rs.getString("company_name");
                String companyRating = rs.getString("rating");
                System.out.print("\t" + companyID + ". " + companyName + ", рейтинг - " + companyRating);
                getQuantityEmployeeSt.setString(1, "%" + companyName + "%");
                int result = 0;
                try (ResultSet rs1 = getQuantityEmployeeSt.executeQuery()) {
                    while (rs1.next()) {
                        result = rs1.getInt("COUNT(developer_id)");
                    }
                }
                System.out.println(",  количество сотрудников - " + result);
            }
        }
    }

    public long getIdCompanyByName(String name) throws SQLException {
        getIdCompanyByNameSt.setString(1, "%" + name + "%");
        int result = 0;
        try (ResultSet rs = getIdCompanyByNameSt.executeQuery()) {
            while (rs.next()) {
                result = rs.getInt("company_id");
            }
        }
        return result;
    }

    public ArrayList<String> getCompanyProjects(String name) throws SQLException {
        ArrayList<String> projectsList = new ArrayList<>();
        getCompanyProjectsSt.setString(1, "%" + name + "%");
        try (ResultSet rs = getCompanyProjectsSt.executeQuery()) {
            while (rs.next()) {
                projectsList.add(rs.getString("project_name"));
            }
        }
        return projectsList;
    }

    public void addCompany() throws SQLException {
        long newCompanyId;
        try (ResultSet rs = selectMaxIdSt.executeQuery()) {
            rs.next();
            newCompanyId = rs.getLong("maxId");
        }
        newCompanyId++;
        System.out.print("Введите название компании : ");
        Scanner sc = new Scanner(System.in);
        String newCompanyName = sc.nextLine();
        System.out.print("Введите рейтинг компании (hight, middle, low) : ");
        String newCompanyRating = sc.nextLine();
        addCompanySt.setLong(1, newCompanyId);
        addCompanySt.setString(2, newCompanyName);
        addCompanySt.setString(3, newCompanyRating);
        Company company = new Company();

        company.setCompany_id(newCompanyId);
        company.setCompany_name(newCompanyName);
        company.setRating(Company.Rating.valueOf(newCompanyRating));

        addCompanySt.executeUpdate();

        if (existsCompany(newCompanyId)) {
            System.out.println("Компания успешно добавлена");
        } else System.out.println("Что-то пошло не так и компания не была  добавлен в базу данных");
    }

    public boolean existsCompany(long id) throws SQLException {
        existsByIdSt.setLong(1, id);
        try (ResultSet rs = existsByIdSt.executeQuery()) {
            rs.next();
            return rs.getBoolean("companyExists");
        }
    }

    public void deleteCompany(String name) throws SQLException {
        long idToDelete = getIdCompanyByName(name);
        deleteCompanyFromCompaniesByNameSt.setString(1, "%" + name + "%");
        deleteCompanyFromCompaniesByNameSt.executeUpdate();
        companies.removeIf(company -> company.getCompany_name().equals(name));
        if (!existsCompany(idToDelete)) {
            System.out.println("Компания успешно удалена из базы данных.");
        } else {
            System.out.println("Что-то пошло не так и компания не была удалена из базы данных");
        }
    }

}


