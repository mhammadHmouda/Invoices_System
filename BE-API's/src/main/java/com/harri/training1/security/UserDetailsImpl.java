package com.harri.training1.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.harri.training1.models.entities.User;
import com.harri.training1.models.enums.Role;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.io.Serial;
import java.util.Collection;

/**
 * This class represent th user details in authentication context
 */
@Data
public class UserDetailsImpl implements UserDetails {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserDetailsImpl.class);

    @Serial
    private static final long serialVersionUID = 1L;
    private Long id;
    private String username;
    private String email;
    private String name;
    @JsonIgnore
    private String password;
    private String role;
    private static Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(Long id,  String email, String password,String name, String role,
                           Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = name;
        this.email = email;
        this.password = password;
        this.role = role;
        UserDetailsImpl.authorities = authorities;

        LOGGER.info("Create new UserDetailsImpl with user id = " + id);
    }

    public static UserDetailsImpl build(User user) {

        LOGGER.info("Create new UserDetailsImpl from User object.");

        return new UserDetailsImpl(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                user.getUsername(),
                user.getRole().name(),
                authorities);
    }

    public static User build(UserDetailsImpl user) {
        LOGGER.info("Create new User from UserDetailsImpl object.");

        return new User(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                Role.valueOf(user.getRole()),
                null,
                null);

    }
    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        UserDetailsImpl.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o){
        if (o == null)
            return false;

        return o instanceof UserDetailsImpl;
    }

}
