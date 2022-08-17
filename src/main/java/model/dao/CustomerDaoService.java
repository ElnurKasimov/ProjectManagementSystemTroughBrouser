package model.dao;


import model.dbConnection.DBConnection;

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
    public List<String> getAllNames() throws SQLException {
        List<String> lines = new ArrayList<>();
        try (ResultSet rs = getAllNamesSt.executeQuery()) {
            while (rs.next()) {
                StringBuilder line = new StringBuilder("");
                long customerID = rs.getLong("customer_id");
                String customerName = rs.getString("customer_name");
                String customerReputation = rs.getString("reputation");
                line.append(customerID + ". " + customerName + ", reputation - " + customerReputation);
                line.append(", is the customer of the following projects: ");
                for (String project : getProjectsNames(customerName)) {
                    line.append(project + ", ");
                }
                lines.add(line.toString());
            }
        }
        return lines;
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

    public List<String> addCustomer(String customerName, String customerReputation) throws SQLException {
        List<String> lines = new ArrayList<>();
        long newCustomerId;
        try(ResultSet rs = selectMaxIdSt.executeQuery()) {
            rs.next();
            newCustomerId = rs.getLong("maxId");
        }
        newCustomerId++;
        addCustomerSt.setLong(1, newCustomerId);
        addCustomerSt.setString(2, customerName);
        addCustomerSt.setString(3, customerReputation);
        Customer  customer = new Customer();
        customer.setCustomer_id(newCustomerId);
        customer.setCustomer_name(customerName);
        customer.setReputation(Customer.Reputation.valueOf(customerReputation));
        addCustomerSt.executeUpdate();
        if (existsCustomer(newCustomerId)) lines.add("was successfully added");
        else lines.add("Something went wrong and the customer was not added to the database");
        return lines;
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

    public List<String> deleteCustomer(String name) throws SQLException {
        List<String> lines = new ArrayList<>();
        long idToDelete = getIdCustomerByName(name);
        deleteCustomerFromCustomersByNameSt.setString(1, "%" + name + "%");
        deleteCustomerFromCustomersByNameSt.executeUpdate();
        customers.removeIf(customer -> customer.getCustomer_name().equals(name));
        if (!existsCustomer(idToDelete)) lines.add("successfully removed from the database.");
        else {
            lines.add("Something went wrong and the customer was not removed to the database.");
        }
        return lines;
    }
}