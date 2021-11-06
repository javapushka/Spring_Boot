package com.javamaster.spring_boot.controller;

import com.javamaster.spring_boot.entity.Role;
import com.javamaster.spring_boot.entity.User;
import com.javamaster.spring_boot.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.ValidationException;
import java.util.HashSet;
import java.util.Set;


@Controller
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public String show(@PathVariable("id") Integer id, Model model) throws ValidationException {
        //add check for null
        model.addAttribute("one_user", userService.findById(id));
        return "user/show";
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String delete(@PathVariable("id") Integer id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }

    //этот метод для того чтобы у юзера был доступ только к своей странице к своему id
    public boolean hasUserId(Authentication authentication, int userId) {
        User user = (User) authentication.getPrincipal();
        return user.getId() == userId;
    }

    @GetMapping(value = "/{id}/edit")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String edit(@PathVariable("id") int id, Model model) throws ValidationException {
        model.addAttribute("edit_user", userService.findById(id));
        return "admin/edit";
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String update(@RequestParam(name = "isAdmin", required = false) boolean isAdmin,
                         @RequestParam(name = "isUser", required = false) boolean isUser,
                         @ModelAttribute("update_user") User user, @PathVariable("id") int id) throws Exception {
        Set<Role> rolesToAdd = new HashSet<>();

        if (isUser) {
            rolesToAdd.add(new Role(2, "ROLE_USER"));
        }
        if (isAdmin) {
            rolesToAdd.add(new Role(1, "ROLE_ADMIN"));
        }

        if (rolesToAdd.isEmpty()) {
            throw new Exception("=================The user's role is not assigned!=================");
        }

        user.setRoles(rolesToAdd);
        userService.updateUser(id, user);
        return "redirect:/admin";
    }
}
