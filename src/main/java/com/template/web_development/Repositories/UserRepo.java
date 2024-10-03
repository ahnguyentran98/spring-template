package com.template.web_development.Repositories;

import com.template.web_development.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepo extends JpaRepository<User, Long> {
    @Query(value = "select * from user where user_name= ?1", nativeQuery = true)
    public List<User> getUserByUserName(String userName);
}
