package model.dao;


import model.default_settings.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CustomerDaoService {
    public static CustomerDaoService INSTANCE;
    public static  List<Customer> customers = new ArrayList<>();

    private PreparedStatement getAllInfoSt;
    private PreparedStatement getAllNamesSt;
    private PreparedStatement getProjectsNamesSt;
    private PreparedStatement selectMaxIdSt;
    private PreparedStatement  addCustomerSt;
    private PreparedStatement existsByIdSt;
    private PreparedStatement getIdCustomerByNameSt;
    private PreparedStatement deleteCustomerFromCustomersByNameSt;

    private CustomerDaoService(Connection connection) throws SQLException {
        PreparedStatement getAllInfoSt = connection.prepareStatement(
                "SELECT * FROM customers"
        );
        try (ResultSet rs = getAllInfoSt.executeQuery()) {
            while (rs.next()) {
                Customer customer = new Customer();
                customer.setCustomer_id(rs.getLong("customer_id"));
                if (rs.getString("customer_name") != null) {
                    customer.setCustomer_name(rs.getString("customer_name"));
                }
                customer.setReputation(Customer.Reputation.valueOf(rs.getString("reputation")));
                customers.add(customer);
            }
        }

        getAllNamesSt = connection.prepareStatement(
                " SELECT customer_id, customer_name, reputation FROM customers"
        );
        getProjectsNamesSt = connection.prepareStatement(
                " SELECT project_name FROM customers JOIN projects " +
                        "ON customers.customer_id = projects.customer_id " +
                        " WHERE  customer_name  LIKE  ?"
        );

        selectMaxIdSt = connection.prepareStatement(
                "SELECT max(customer_id) AS maxId FROM customers"
        );

        addCustomerSt = connection.prepareStatement(
                "INSERT INTO customers  VALUES ( ?, ?, ?)");

        existsByIdSt = connection.prepareStatement(
                "SELECT count(*) > 0 AS customerExists FROM customers WHERE customer_id = ?"
        );

        getIdCustomerByNameSt = connection.prepareStatement(
                "SELECT customer_id FROM customers " +
                        "WHERE customer_name  LIKE  ?"
        );

        deleteCustomerFromCustomersByNameSt = connection.prepareStatement(
                "DELETE FROM customers WHERE customer_name LIKE  ?"
        );
    }


    public static CustomerDaoService  getInstance(Connection connection) {
        if (INSTANCE == null) {
            try {
                INSTANCE = new CustomerDaoService(DBConnection.getInstance().getConnection());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return INSTANCE;
    }
    public void getAllNames() throws SQLException {
        System.out.println("Список всех  заказчиков :");
        try (ResultSet rs = getAllNamesSt.executeQuery()) {
            while (rs.next()) {
                long customerID = rs.getLong("customer_id");
                String customerName = rs.getString("customer_name");
                String customerReputation = rs.getString("reputation");
                System.out.print("\t" + customerID + ". " + customerName + ", репутация - " + customerReputation);
                System.out.println(", является заказчиком следующих проектов: ");
                for (String project : getProjectsNames(customerName)) {
                    System.out.println("\t\t" + project);
                }
            }
        }
    }

    public ArrayList<String>  getProjectsNames(String customerName) throws SQLException {
        ArrayList<String> projectNames = new ArrayList<>();
        getProjectsNamesSt.setString(1, "%" + customerName + "%");
        try (ResultSet rs = getProjectsNamesSt.executeQuery()) {
            while (rs.next()) {
                projectNames.add(rs.getString("project_name"));
            }
        }
        return projectNames;
    }

    public void addCustomer() throws SQLException {
        long newCustomerId;
        try(ResultSet rs = selectMaxIdSt.executeQuery()) {
            rs.next();
            newCustomerId = rs.getLong("maxId");
        }
        newCustomerId++;
        System.out.print("\tВведите название заказчика : ");
        Scanner sc = new Scanner(System.in);
        String newCustomerName = sc.nextLine();
        System.out.print("\tВведите репутацию заказчика (trustworthy, respectable, insolvent) : ");
        String newCustomerReputation = sc.nextLine();
        addCustomerSt.setLong(1, newCustomerId);
        addCustomerSt.setString(2, newCustomerName);
        addCustomerSt.setString(3, newCustomerReputation);
        Customer  customer = new Customer();

        customer.setCustomer_id(newCustomerId);
        customer.setCustomer_name(newCustomerName);
        customer.setReputation(Customer.Reputation.valueOf(newCustomerReputation));

        addCustomerSt.executeUpdate();

        if (existsCustomer(newCustomerId)) {System.out.println("Заказчик успешно добавлен");}
        else System.out.println("Что-то пошло не так и заказчик не был  добавлен в базу данных");
    }

    public boolean existsCustomer(long id) throws SQLException {
        existsByIdSt.setLong(1, id);
        try (ResultSet rs = existsByIdSt.executeQuery()) {
            rs.next();
            return rs.getBoolean("customerExists");
        }
    }

    public long getIdCustomerByName(String name) throws SQLException {
        getIdCustomerByNameSt.setString(1, "%" + name + "%");
        int result = 0;
        try (ResultSet rs = getIdCustomerByNameSt.executeQuery()) {
            while (rs.next()) {
                result = rs.getInt("customer_id");
            }
        }
        return result;
    }

    public void deleteCustomer(String name) throws SQLException {
        long idToDelete = getIdCustomerByName(name);
        deleteCustomerFromCustomersByNameSt.setString(1, "%" + name + "%");
        deleteCustomerFromCustomersByNameSt.executeUpdate();
        customers.removeIf(customer -> customer.getCustomer_name().equals(name));
        if (!existsCustomer(idToDelete)) { System.out.println("Заказчик успешно удален из базы данных.");}
        else {
            System.out.println("Что-то пошло не так и заказчик не был удален из базы данных");
        }
    }
}