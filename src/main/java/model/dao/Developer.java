package model.dao;

import lombok.Data;

@Data
public class Developer {
    private long developer_id;
    private String lastName;
    private String firstName;
    private int age;
    private long company_id;
    private  int salary;
}
