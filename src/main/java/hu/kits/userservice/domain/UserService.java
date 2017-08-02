package hu.kits.userservice.domain;

import java.lang.invoke.MethodHandles;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserService {

    private final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    
    private final UserRepository userRepository;
    
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User authenticateUser(String domain, String userId, String password) throws AuthenticationException {
        
        logger.debug("Authentication request in domain '{}' for user '{}'", domain, userId);
        
        Optional<User> oUser = userRepository.loadUser(domain, userId);
        if(oUser.isPresent()) {
            
            User user = oUser.get();
            
            if(PasswordHasher.checkPassword(user.passwordHash, password)) {
                if(user.isActive) {
                    logger.info("Authentication success for user '{}'", userId);
                    return user;
                } else {
                    logger.info("Authentication failure. User '{}' is inactive", userId);
                    throw new AuthenticationException("User is inactive");
                }
            } else {
                logger.info("Authentication failure. Wrong password for user '{}'", userId);
                throw new AuthenticationException();
            }
        } else {
            logger.info("Authentication failure. User with user id '{}' not found", userId);
            throw new AuthenticationException();
        }
    }
    
    public void changePassword(String domain, String userId, String oldPassword, String newPassword) throws AuthenticationException {
        
        logger.info("Password change request in domain '{}' for user '{}'", domain, userId);
        
        authenticateUser(domain, userId, oldPassword);
        
        userRepository.changePassword(domain, userId, PasswordHasher.createNewPasswordHash(newPassword));
        
        logger.info("Successful password change for '{}'", userId);
    }
    
}
