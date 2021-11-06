package com.javamaster.spring_boot.service;

import com.javamaster.spring_boot.config.dao.UserDao;
import com.javamaster.spring_boot.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.xml.bind.ValidationException;
import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

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
    public void updateUser(Integer id, User user) throws ValidationException {
        userDao.updateUser(id, user);
    }

    @Override
    public void deleteUser(Integer userId) {
        userDao.deleteUser(userId);
    }

    @Override
    public User findByName(String name) {
        return userDao.findByName(name);
    }
}
