package com.javamaster.spring_boot.service;

import com.javamaster.spring_boot.entity.User;
import com.javamaster.spring_boot.repository.RoleRepository;
import com.javamaster.spring_boot.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.xml.bind.ValidationException;
import java.util.List;


import static java.util.Objects.isNull;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(Integer userId) throws ValidationException {
        User user = userRepository.getById(userId);
        validateUser(user);
        return user;
    }

    @Override
    public User saveUser(User user) throws ValidationException {
        validateUser(user);
        User saveUser = new User();
        saveUser.setId(user.getId());
        saveUser.setAge(user.getAge());
        saveUser.setName(user.getName());
        saveUser.setPassword(user.getPassword());
        saveUser.setRoles(user.getRoles());
        validateUser(saveUser);
        return userRepository.save(saveUser);
    }

    @Override
    public void updateUser(Integer id, User user) throws ValidationException {
        User userFromDb = userRepository.getById(id);
        userFromDb.setAge(user.getAge());
        userFromDb.setName(user.getName());
        userFromDb.setPassword(user.getPassword());
        userFromDb.setRoles(user.getRoles());
        validateUser(userFromDb);
        userRepository.save(userFromDb);

    }

    @Override
    public void deleteUser(Integer userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public User findByName(String name) {
        User user = userRepository.findByName(name);
        if (user != null) {
            return user;
        }
        return null;
    }

    private void validateUser(User user) throws ValidationException {
        if (isNull(user)) {
            throw new ValidationException("Object user is null!");
        }
        if (isNull(user.getName()) || user.getName().isEmpty()) {
            throw new ValidationException("Name is empty!");
        }
        if (isNull(user.getPassword()) || user.getPassword().isEmpty()) {
            throw new ValidationException("Password is empty!");
        }
        if (isNull(user.getAge()) || user.getAge().isEmpty()) {
            throw new ValidationException("Age is empty!");
        }
        if (isNull(user.getRoles()) || user.getRoles().isEmpty()) {
            throw new ValidationException("Roles is empty!");
        }
    }
}
