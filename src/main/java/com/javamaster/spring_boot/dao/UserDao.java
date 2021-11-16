package com.javamaster.spring_boot.dao;

import com.javamaster.spring_boot.entity.Role;
import com.javamaster.spring_boot.entity.User;

import javax.xml.bind.ValidationException;
import java.util.List;

public interface UserDao {

    List<User> findAll();

    User findById(Integer id) throws ValidationException;

    void saveUser(User user) throws ValidationException;

    void updateUser(User user) throws ValidationException;

    void deleteUser(Integer userId);

    User findUserByEmail(String email);

    Role getRoleById(Integer id);

}
