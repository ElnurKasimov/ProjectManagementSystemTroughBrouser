package model.dao;

import lombok.Data;

@Data
public class TemporaryProject {
    private String projectName;
    private String companyName;
    private String customerName;
    private  int projectCost;
    private String startDate;
}
