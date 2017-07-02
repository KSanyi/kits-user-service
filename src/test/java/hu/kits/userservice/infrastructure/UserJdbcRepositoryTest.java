package hu.kits.userservice.infrastructure;

import java.util.Optional;

import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import hu.kits.userservice.domain.User;
import kits.kitsjdbc.SimpleDatabaseClient;

public class UserJdbcRepositoryTest {

    private UserJdbcRepository userRepository;
    
    @Before
    public void init() throws Exception {
        DataSource dataSource = InMemoryDataSourceFactory.createDataSource();
        userRepository = new UserJdbcRepository(dataSource);
        
        new SimpleDatabaseClient(dataSource).executeSql(
                "INSERT INTO USER VALUES('kits', 'ksanyi', 'xxxhashed', 'Kócsó Sándor', 'admin', '+36703699208', 'kocso@kits.hu', true)");
    }
    
    @Test
    public void loadUser() {
        Optional<User> user = userRepository.loadUser("kits", "ksanyi");
        
        Assert.assertTrue(user.isPresent());
        
        User u = user.get();
        Assert.assertEquals("xxxhashed", u.passwordHash);
        Assert.assertEquals("Kócsó Sándor", u.name);
        Assert.assertEquals("admin", u.role);
        Assert.assertEquals("+36703699208", u.phone);
        Assert.assertEquals("kocso@kits.hu", u.email);
        Assert.assertTrue(u.isActive);
    }
    
    @Test
    public void updatePassword() {
        
        userRepository.changePassword("kits", "ksanyi", "yyyhashed");
        
        Optional<User> user = userRepository.loadUser("kits", "ksanyi");
        Assert.assertTrue(user.isPresent());
        Assert.assertEquals("yyyhashed", user.get().passwordHash);
    }
    
}
