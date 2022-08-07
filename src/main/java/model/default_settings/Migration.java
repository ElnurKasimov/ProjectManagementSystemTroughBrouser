package model.default_settings;

import org.flywaydb.core.Flyway;

public class Migration {
    public void initDb(DBConnection dbConnection) {
        Flyway flyway = Flyway
                    .configure()
                    .dataSource(DBConnection.getInstance().connectionUrl, DBConnection.getInstance().dbUser, DBConnection.getInstance().dbPassword)
                    .load();
        flyway.migrate();
        }
}
