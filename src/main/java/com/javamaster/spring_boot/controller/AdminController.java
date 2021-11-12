package com.javamaster.spring_boot.controller;

import com.javamaster.spring_boot.entity.Role;
import com.javamaster.spring_boot.entity.User;
import com.javamaster.spring_boot.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminController {

    private final UserService userService;

    @GetMapping()
    public String listOfUsers(Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("oneUser", user);

        Iterable<User> userIterable = userService.findAll();
        model.addAttribute("listUsers", userIterable);
        return "admin/adminPage";
    }

    @PostMapping("/delete")
    public String deleteUser(@ModelAttribute("user") User user) {
        userService.deleteUser(user.getId());
        return "redirect:/admin";
    }

    @PostMapping("/new")
    public String create(@ModelAttribute("user") User user,
                         @RequestParam(name = "role", required = false) String role) throws Exception {
        Set<Role> rolesForNewUser = new HashSet<>();
        rolesForNewUser.add(userService.getRoleById(2)); //add role of USER

        if ((role != null) && (role.equals("admin"))) {
            rolesForNewUser.add(userService.getRoleById(1));
        }

        if (userService.findUserByEmail(user.getEmail()) != null) {
            throw new Exception("======================User exists!======================");
        }

        user.setRoles(rolesForNewUser);
        userService.saveUser(user);
        return "redirect:/admin";
    }

    @PostMapping("/edit/{id}")
    public String update(@ModelAttribute("user") User user,
                         @RequestParam(name = "role", required = false) String role) throws Exception {
        Set<Role> rolesForUser = new HashSet<>();
        rolesForUser.add(userService.getRoleById(2)); //add role of USER

        if ((role != null) && (role.equals("admin"))) {
            rolesForUser.add(userService.getRoleById(1));
        }


        user.setRoles(rolesForUser);
        userService.updateUser(user.getId(), user);
        return "redirect:/admin";
    }
}