package com.javamaster.spring_boot.service;

import com.javamaster.spring_boot.dao.UserDao;
import com.javamaster.spring_boot.entity.Role;
import com.javamaster.spring_boot.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.xml.bind.ValidationException;
import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImp implements UserService {

    private final UserDao userDao;

    @Override
    public List<User> findAll() {
        return userDao.findAll();
    }

    @Override
    public User findById(Integer userId) throws ValidationException {
        return userDao.findById(userId);
    }

    @Override
    public void saveUser(User user) throws ValidationException {
        userDao.saveUser(user);
    }

    @Override
    public void updateUser(User user) throws ValidationException {
        userDao.updateUser(user);
    }

    @Override
    public void deleteUser(Integer userId) {
        userDao.deleteUser(userId);
    }

    @Override
    public User findUserByEmail(String email) {
        return userDao.findUserByEmail(email);
    }

    @Override
    public Role getRoleById(Integer id) {
        return userDao.getRoleById(id);
    }
}
