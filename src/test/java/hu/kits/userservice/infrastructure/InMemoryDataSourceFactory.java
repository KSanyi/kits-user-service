package hu.kits.userservice.infrastructure;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;

import javax.sql.DataSource;

import org.h2.jdbcx.JdbcDataSource;

public class InMemoryDataSourceFactory {

    public static DataSource createDataSource() throws Exception {
        Class.forName("org.h2.Driver");
        JdbcDataSource dataSource = new JdbcDataSource();
        Path tempDirPath = Files.createTempDirectory("kits-user-service");
        dataSource.setURL("jdbc:h2:" + tempDirPath + "/test");
        try (Connection connection = dataSource.getConnection()) {
            dropExistingDb(connection);
            createDb(connection);
        }
        
        return dataSource;
    }
    
    private static void dropExistingDb(Connection connection) throws IOException {
        try {
            for(String line : Files.readAllLines(Paths.get("database/drop-database.sql"))) {
                connection.createStatement().executeUpdate(line);
            }
        } catch (Exception ex) {} // TABLE does not exist, no problem
    }
    
    private static void createDb(Connection connection) throws Exception {
        for (String line : Files.readAllLines(Paths.get("database/create-database.sql"))) {
            connection.createStatement().executeUpdate(line);
        }
    }
    
}
