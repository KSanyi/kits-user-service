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
        
        logger.debug("Authentication request for {} with user id {}", domain, userId);
        
        Optional<User> oUser = userRepository.loadUser(domain, userId);
        if(oUser.isPresent()) {
            
            User user = oUser.get();
            
            String hashedPassword = PasswordHasher.hash(password);
            if(hashedPassword.equals(user.passwordHash)) {
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
    
}
