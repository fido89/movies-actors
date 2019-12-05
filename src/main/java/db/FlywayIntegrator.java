package db;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.FlywayException;
import org.hibernate.boot.Metadata;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.integrator.spi.Integrator;
import org.hibernate.service.spi.SessionFactoryServiceRegistry;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FlywayIntegrator implements Integrator {
    public static final Logger logger = Logger.getLogger("FlywayIntegrator");

    @Override
    public void integrate(Metadata metadata, SessionFactoryImplementor sessionFactoryImplementor, SessionFactoryServiceRegistry sessionFactoryServiceRegistry) {
        logger.log(Level.INFO, "Starting Flyway Migration");
        Flyway flyway = Flyway.configure().dataSource("jdbc:h2:file:./data/demo", "", "").load();
        try {
            flyway.migrate();
        } catch (FlywayException e) {
            logger.log(Level.SEVERE, "Error while migrating:", e);
            System.exit(0);
        }
        logger.log(Level.INFO, "Finished Flyway Migration");
    }

    @Override
    public void disintegrate(SessionFactoryImplementor sessionFactoryImplementor, SessionFactoryServiceRegistry sessionFactoryServiceRegistry) {

    }
}
