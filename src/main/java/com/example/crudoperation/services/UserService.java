package com.example.crudoperation.services;

import com.example.crudoperation.entity.Post;
import com.example.crudoperation.entity.User;

import java.util.List;

public interface UserService {
    void save(User user);

    User findByName(String username);

    List<User> findAll();
    User findUserByName(String username);
}
