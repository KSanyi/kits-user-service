package hu.kits.userservice;

import java.net.URI;
import java.net.URISyntaxException;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import hu.kits.userservice.domain.UserService;
import hu.kits.userservice.infrastructure.UserJdbcRepository;
import hu.kits.userservice.infrastructure.rest.RestServer;

public class Main {

    public static void main(String[] args) throws Exception {

        int port = getPort();
        URI dbUri = getDatabaseUri();
        
        DataSource dataSource = createDataSource(dbUri);
        
        new RestServer(new UserService(new UserJdbcRepository(dataSource)), port).start();
    }
    
    private static int getPort() {
        String port = System.getenv("PORT");
        if (port == null) {
            throw new IllegalArgumentException("System environment variable PORT is missing");
        }

        try {
            return Integer.parseInt(port);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Illegal system environment variable PORT: " + port);
        }
    }
    
    private static URI getDatabaseUri() throws URISyntaxException {
        String databaseUrl = System.getenv("CLEARDB_DATABASE_URL");
        if (databaseUrl == null) {
            throw new IllegalArgumentException("System environment variable CLEARDB_DATABASE_URL is missing");
        }
        
        return new URI(databaseUrl);
    }
    
    private static DataSource createDataSource(URI dbUri) throws URISyntaxException {
        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String jdbcUrl = "jdbc:mysql://" + dbUri.getHost() + dbUri.getPath();
        
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(username);
        config.setPassword(password);
        return new HikariDataSource(config);
    }
    
}
