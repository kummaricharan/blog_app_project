package com.example.crudoperation.services;

import com.example.crudoperation.dao.UserRepository;
import com.example.crudoperation.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserServiceImpl implements UserService{
    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public User findByName(String username) {
        return userRepository.findByName(username);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }
    @Override
    public User findUserByName(String username) {
        return userRepository.findUserByName(username);
    }
}
