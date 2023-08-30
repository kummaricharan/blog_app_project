package com.example.crudoperation.dao;

import com.example.crudoperation.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface UserRepository extends JpaRepository<User,Integer> {
    List<User> findAll();
    @Query("SELECT u FROM User u WHERE u.username = :username")
    User findUserByName(String username);
    @Query("SELECT user FROM User user WHERE user.username = :username")
    User findByName(@Param("username") String username);

}
