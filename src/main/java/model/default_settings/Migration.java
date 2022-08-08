package model.default_settings;

import org.flywaydb.core.Flyway;

public class Migration {
    public void initDb(DBConnection dbConnection) {
        Flyway flyway = Flyway
                    .configure()
                    .dataSource("jdbc:mysql://127.0.0.1:3306/it_market", "root", "$Elnur&Kasimov1972")
                    .load();
        flyway.migrate();
        }
}
