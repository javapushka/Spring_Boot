package com.javamaster.spring_boot.controller;

import com.javamaster.spring_boot.entity.Role;
import com.javamaster.spring_boot.entity.User;
import com.javamaster.spring_boot.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.ValidationException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Objects.isNull;


@RestController
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminRestController {

    private final UserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> userList = userService.findAll();
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @GetMapping("/getUser/{id}")
    public ResponseEntity<User> getUser(@PathVariable("id") int id) throws ValidationException {
        User user = userService.findById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/getCurrentUser")
    public ResponseEntity<User> getCurrentUser() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return new ResponseEntity<>(user, HttpStatus.OK);
    }


    @PostMapping("/new")
    public ResponseEntity<User> addNewUser(@RequestBody User user) throws Exception {
        Set<Role> rolesForNewUser = new HashSet<>();
        rolesForNewUser.add(userService.getRoleById(2)); //add role of USER

        if (userService.findUserByEmail(user.getEmail()) != null) {
            throw new Exception("======================User exists!======================");
        }

        if (!(isNull(user.getRoles()) || user.getRoles().isEmpty())) {
            if (user.getRoles().iterator().next().getRole().contains("ADMIN")) {
                rolesForNewUser.add(userService.getRoleById(1));
            }
        }

        user.setRoles(rolesForNewUser);
        userService.saveUser(user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<User> editUser(@RequestBody User user,
                                         @PathVariable("id") int id) throws ValidationException {
        Set<Role> rolesForUser = new HashSet<>();
        rolesForUser.add(userService.getRoleById(2));

        if (!(isNull(user.getRoles()) || user.getRoles().isEmpty())) {
            if (user.getRoles().iterator().next().getRole().contains("ADMIN")) {
                rolesForUser.add(userService.getRoleById(1));
            }
        }

        user.setRoles(rolesForUser);
        userService.updateUser(id, user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable("id") int id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
