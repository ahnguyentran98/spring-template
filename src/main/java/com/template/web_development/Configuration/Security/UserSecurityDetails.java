package com.template.web_development.Configuration.Security;

import com.template.web_development.Entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
public class UserSecurityDetails implements UserDetails {
    private String name;
    private String password;
    private List<GrantedAuthority> authorities = new ArrayList<>();

    public UserSecurityDetails(User userInfo) {
        name = userInfo.getUserName();
        password = userInfo.getPassword();
        // add "ROLE_" prefix for spring authority handle
        authorities.add(new SimpleGrantedAuthority("ROLE_" + userInfo.getRole()));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return name;
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
}
