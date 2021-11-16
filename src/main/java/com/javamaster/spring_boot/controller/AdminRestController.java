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

    @GetMapping("/infoAdmin")
    public ResponseEntity<User> infoAdmin() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return new ResponseEntity<>(user,HttpStatus.OK);
    }

    @PostMapping("/new")
    public ResponseEntity<User> addNewUser(@RequestBody User user) throws ValidationException {
        Set<Role> rolesForNewUser = new HashSet<>();
        rolesForNewUser.add(userService.getRoleById(2)); //add role of USER

        if(!(isNull(user.getRoles()) || user.getRoles().isEmpty())){
            if(user.getRoles().iterator().next().getRole().contains("ADMIN")) {
                rolesForNewUser.add(userService.getRoleById(1));
            }
        }

        user.setRoles(rolesForNewUser);
        userService.saveUser(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/edit")
    public ResponseEntity<User> editUser(@RequestBody User user) throws ValidationException {
        Set<Role> rolesForNewUser = new HashSet<>();
        rolesForNewUser.add(userService.getRoleById(2));

        if(!(isNull(user.getRoles()) || user.getRoles().isEmpty())){
            if(user.getRoles().iterator().next().getRole().contains("ADMIN")) {
                rolesForNewUser.add(userService.getRoleById(1));
            }
        }

        user.setRoles(rolesForNewUser);
        userService.updateUser(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<User> deleteUser(@RequestBody User user) {
        userService.deleteUser(user.getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
