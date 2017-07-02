package hu.kits.userservice.domain;

public class PasswordHasher {

    public static String hash(String password) {
        
        return password + "hashed";
    }

}
