package com.template.web_development.Service;

import com.template.web_development.Entity.User;
import com.template.web_development.Repositories.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserRepo userRepo;

    @Transactional(readOnly = true)
    public User getUserByUserName(String userName) {
        LOGGER.info("Get user by userName {}", userName);
        List<User> userByUserName = userRepo.getUserByUserName(userName);
        if (userByUserName.isEmpty()) {
            LOGGER.info("user name is not existed");
            return null;
        }

        if (userByUserName.size() > 1) {
            LOGGER.error("Have {} account with same user name {}", userByUserName.size(), userName);
            return null;
        }

        return userByUserName.get(0);
    }
}
