package hu.kits.userservice.infrastructure;

import java.util.Optional;

import javax.sql.DataSource;

import hu.kits.userservice.domain.User;
import hu.kits.userservice.domain.UserRepository;
import kits.kitsjdbc.DataMap;
import kits.kitsjdbc.DatabaseClient;

public class UserJdbcRepository implements UserRepository {

    private static final String TABLE_USER = "USER";
    private static final String COLUMN_DOMAIN = "DOMAIN";
    private static final String COLUMN_USERID = "USERID";
    private static final String COLUMN_PASSWORD_HASH = "PASSWORD_HASH";
    private static final String COLUMN_ROLE = "ROLE";
    private static final String COLUMN_PHONE = "PHONE";
    private static final String COLUMN_EMAIL = "EMAIL";
    private static final String COLUMN_ACTIVE = "ISACTIVE";
    
    private final DatabaseClient databaseClient;
    
    public UserJdbcRepository(DataSource dataSource) {
        databaseClient = new DatabaseClient(dataSource);
    }
    
    @Override
    public Optional<User> loadUser(String domain, String userId) {
        
        Optional<User> user = databaseClient.loadData(
                "SELECT *  FROM " + TABLE_USER + " WHERE " + COLUMN_DOMAIN + " = :domain AND " + COLUMN_USERID + " = :userId",
                rs -> new User(userId,
                        rs.getString(COLUMN_PASSWORD_HASH),
                        rs.getString("NAME"),
                        rs.getString(COLUMN_ROLE),
                        rs.getString(COLUMN_PHONE),
                        rs.getString(COLUMN_EMAIL),
                        rs.getBoolean(COLUMN_ACTIVE)),
                new DataMap("domain", domain, "userId", userId));
        
        return user;
    }

    @Override
    public void changePassword(String domain, String userId, String newPasswordHash) {
        databaseClient.update(
                "UPDATE " + TABLE_USER + " SET " + COLUMN_PASSWORD_HASH + " = :newPasswordHash WHERE " + COLUMN_DOMAIN + " = :domain AND " + COLUMN_USERID + " = :userId",
                new DataMap("domain", domain, "userId", userId, "newPasswordHash", newPasswordHash));
        
    }

}
