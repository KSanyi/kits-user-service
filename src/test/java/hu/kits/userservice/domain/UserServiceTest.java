package hu.kits.userservice.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;

public class UserServiceTest {

    private UserService userService = new UserService(new FakeUserRepository());
    
    @Test
    public void succesfulLogin() throws AuthenticationException {
        
        User user = userService.authenticateUser("kits", "ksanyi", "xxx");
        
        Assert.assertEquals("Kocso Sanyi", user.name);
    }
    
    @Test(expected = AuthenticationException.class)
    public void wrongUserName() throws AuthenticationException {
        
        userService.authenticateUser("kits", "sanyi", "xxx");
    }
    
    @Test(expected = AuthenticationException.class)
    public void wrongPassword() throws AuthenticationException {
        
        userService.authenticateUser("kits", "ksanyi", "xx");
    }
    
    @Test(expected = AuthenticationException.class)
    public void inactivatedUser() throws AuthenticationException {
        
        userService.authenticateUser("kits", "joe", "jjj");
    }
    
    public void changePassword() throws AuthenticationException {
        
        userService.changePassword("kits", "ksanyi", "xxx", "yyy");
    }
    
    @Test(expected = AuthenticationException.class)
    public void wrongPasswordChange() throws AuthenticationException {
        
        userService.changePassword("kits", "ksanyi", "aaa", "yyy");
    }
    
    @Test(expected = AuthenticationException.class)
    public void inactivatedUserPasswordChange() throws AuthenticationException {
        
        userService.changePassword("kits", "joe", "jjj", "yyy");
    }
    
}

class FakeUserRepository implements UserRepository {

    private List<User> users = new ArrayList<>();
    {
        users.add(new User("ksanyi", "leNMO6yYO0KUHMUoodjRfA==:AIBfoIwvymiGu7BGPwRav1GcuktTOECa6cGYWiYKWdc=", "Kocso Sanyi", "admin", "+3670369999", "ksanyi@gmail.com", true));
        users.add(new User("joe", "wwQaoRoGK+vkS71sSL5ssg==:i3otXubPoJBVL0gdIDTPQ8cD906seUd2/BZdKRdmuWw=", "Joe Dow", "employee", "+3620369999", "joe7893@gmail.com", false));
    }
    
    @Override
    public Optional<User> loadUser(String domain, String userId) {
        return users.stream().filter(u -> u.userId.equals(userId)).findAny();
    }

    @Override
    public void changePassword(String domain, String userId, String newPassword) {
    }
    
}
