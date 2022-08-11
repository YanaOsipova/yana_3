package configuration;

import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PostgreDatasource {
    private static DataSource dataSource;

    public static DataSource getPostgreDataSource() {
        if (dataSource == null) {
            Properties props = new Properties();
            try(InputStream input = PostgreDatasource.class.getClassLoader().getResourceAsStream("database.properties")) {
                props.load(input);
                PGSimpleDataSource ds = new PGSimpleDataSource();
                ds.setDatabaseName("postgres");
                ds.setUrl(props.getProperty("db.url"));
                ds.setUser(props.getProperty("db.login"));
                ds.setPassword(props.getProperty("db.password"));
                dataSource = ds;
            } catch (IOException exception) {
                throw new RuntimeException(exception.getMessage());
            }
        }
        return dataSource;
    }
}
