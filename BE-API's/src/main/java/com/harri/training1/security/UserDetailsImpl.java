package com.harri.training1.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.harri.training1.models.entities.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.io.Serial;
import java.util.Collection;

@Data
public class UserDetailsImpl implements UserDetails {
    @Serial
    private static final long serialVersionUID = 1L;
    private final Long id;
    private final String username;
    private final String email;
    private String name;
    @JsonIgnore
    private final String password;
    private final String role;

    private static Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(Long id,  String email, String password,String name, String role,
                           Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = name;
        this.email = email;
        this.password = password;
        this.role = role;
        UserDetailsImpl.authorities = authorities;
    }

    public static UserDetailsImpl build(User user) {

        return new UserDetailsImpl(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                user.getUsername(),
                user.getRole().getName().name(),
                authorities);
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