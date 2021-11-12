package com.javamaster.spring_boot.dao;

import com.javamaster.spring_boot.entity.Role;
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
public class UserDaoImp implements UserDao {

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
    public void saveUser(User user) throws ValidationException {
        validateUser(user);
        User saveUser = new User();
        saveUser.setId(user.getId());
        saveUser.setEmail(user.getEmail());
        saveUser.setFirstName(user.getFirstName());
        saveUser.setLastName(user.getLastName());
        saveUser.setAge(user.getAge());
        saveUser.setPassword(user.getPassword());
        saveUser.setRoles(user.getRoles());
        validateUser(saveUser);
        userRepository.save(saveUser);
    }

    @Override
    public void updateUser(Integer id, User user) {
        User userFromDb = userRepository.getById(id);
        userFromDb.setEmail(user.getEmail());
        userFromDb.setFirstName(user.getFirstName());
        userFromDb.setLastName(user.getLastName());
        userFromDb.setAge(user.getAge());
        userFromDb.setPassword(user.getPassword());
        userFromDb.setRoles(user.getRoles());
        userRepository.save(userFromDb);

    }

    @Override
    public void deleteUser(Integer userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public User findUserByEmail(String email) {
        User user = userRepository.findUserByEmail(email);
        if (user != null) {
            return user;
        }
        return null;
    }

    @Override
    public Role getRoleById(Integer id) {
        Role role = roleRepository.findRoleById(id);
        if (role != null) {
            return role;
        }
        return null;
    }

    private void validateUser(User user) throws ValidationException {
        if (isNull(user)) {
            throw new ValidationException("Object user is null!");
        }
        if (isNull(user.getEmail()) || user.getEmail().isEmpty()) {
            throw new ValidationException("Email is empty!");
        }
        if (isNull(user.getFirstName()) || user.getFirstName().isEmpty()) {
            throw new ValidationException("Firstname is empty!");
        }
        if (isNull(user.getLastName()) || user.getLastName().isEmpty()) {
            throw new ValidationException("Lastname is empty!");
        }
        if (isNull(user.getPassword()) || user.getPassword().isEmpty()) {
            throw new ValidationException("Password is empty!");
        }
        if (isNull(user.getAge()) || (user.getAge() == null) ) {
            throw new ValidationException("Age is empty!");
        }
        if (isNull(user.getRoles()) || user.getRoles().isEmpty()) {
            throw new ValidationException("Roles is empty!");
        }
    }
}
