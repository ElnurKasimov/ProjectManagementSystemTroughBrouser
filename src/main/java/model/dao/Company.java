package model.dao;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Company {
    private long company_id;
    private String company_name;
    private Rating rating;

    public enum Rating {
        high,
        middle,
        low
    }
}
