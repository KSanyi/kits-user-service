package hu.kits.userservice.domain;

import java.util.Optional;

public interface UserRepository {

    Optional<User> loadUser(String domain, String userId);

    void changePassword(String domain, String userId, String newPasswordHash);

}
