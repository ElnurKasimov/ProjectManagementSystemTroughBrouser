package model.default_settings;

import org.flywaydb.core.Flyway;

public class Migration {
    public void initDb(DBConnection dbConnection) {
        System.out.println(" connection URL - " + dbConnection.getConnectionUrl());
        System.out.println(" db User  - " + dbConnection.getDbUser());
        System.out.println("db password - " + dbConnection.getDbPassword());
        Flyway flyway = Flyway
                    .configure()
                    .dataSource(dbConnection.getConnectionUrl(), dbConnection.getDbUser(), dbConnection.getDbPassword())
                    .load();
        flyway.migrate();
        System.out.println("migrations is completed");
        }
}
