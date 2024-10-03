package com.template.web_development.Configuration.Security;

import com.template.web_development.Entity.User;
import com.template.web_development.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserSecurityInfoService implements UserDetailsService {
    @Autowired
    private UserService userService;


    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User userInfo = userService.getUserByUserName(userName);
        if (userInfo == null){
            throw new UsernameNotFoundException("User not found with user name: " + userName);
        }
        return new UserSecurityDetails(userInfo);
    }
}