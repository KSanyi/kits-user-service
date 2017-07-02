package hu.kits.userservice.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class User {

    public final String userId;
    
    public final String passwordHash;
    
    public final String name;
    
    public final String role; 
    
    public final String phone;
    
    public final String email;
    
    public final boolean isActive;

    public User(String userId, String passwordHash, String name, String role, String phone, String email, boolean isActive) {
        this.userId = userId;
        this.passwordHash = passwordHash;
        this.name = name;
        this.role = role;
        this.phone = phone;
        this.email = email;
        this.isActive = isActive;
    }
    
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
    
}
