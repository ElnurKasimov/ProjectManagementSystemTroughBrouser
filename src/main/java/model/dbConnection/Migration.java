package model.dbConnection;

import org.flywaydb.core.Flyway;

public class Migration {
    public void initDb(DBConnection dbConnection) {
        System.out.println(dbConnection.getConnectionUrl());
        System.out.println(dbConnection.getDbUser());
        System.out.println(dbConnection.getDbPassword());
        Flyway flyway = Flyway
                    .configure()
                    .dataSource(dbConnection.getConnectionUrl(), dbConnection.getDbUser(), dbConnection.getDbPassword())
                    .load();
        flyway.migrate();
    }
}
