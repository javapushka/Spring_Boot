package com.javamaster.spring_boot.controller;

import com.javamaster.spring_boot.entity.Role;
import com.javamaster.spring_boot.entity.User;
import com.javamaster.spring_boot.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/admin")
@AllArgsConstructor
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {

    private final UserService userService;


    @GetMapping()
    public String index(Model model) {
        model.addAttribute("all_users", userService.findAll());
        return "admin/index";
    }

    @GetMapping("/new")
    public String newUser(@ModelAttribute("new_user") User user) {
        return "admin/new";
    }

    @PostMapping()
    public String create(@RequestParam(name = "isAdmin", required = false) boolean isAdmin,
                         @RequestParam(name = "isUser", required = false) boolean isUser,
                         @ModelAttribute User user) throws Exception {
        Set<Role> rolesToAdd = new HashSet<>();

        if (userService.findByName(user.getName()) != null) {
            throw new Exception("======================User exists!======================");
        }
        if (isUser) {
            rolesToAdd.add(new Role(2, "ROLE_USER"));
        }
        if (isAdmin) {
            rolesToAdd.add(new Role(1, "ROLE_ADMIN"));
        }

        if (rolesToAdd.isEmpty()) {
            throw new Exception("=================The role of the new user is not assigned!=================");
        }

        user.setRoles(rolesToAdd);
        userService.saveUser(user);
        return "redirect:/admin";
    }
}