package model.default_settings;

import org.flywaydb.core.Flyway;

public class Migration {
    public void initDb(DBConnection dbConnection) {
        Flyway flyway = Flyway
                    .configure()
                    .dataSource(dbConnection.getConnectionUrl(), dbConnection.getDbUser(), dbConnection.getDbPassword())
                    .load();
        flyway.migrate();
    }
}
