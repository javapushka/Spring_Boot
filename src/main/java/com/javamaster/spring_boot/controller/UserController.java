//package com.javamaster.spring_boot.controller;
//
//import com.javamaster.spring_boot.entity.User;
//import com.javamaster.spring_boot.service.UserService;
//import lombok.AllArgsConstructor;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//
//
//@Controller
//@RequestMapping("/user")
//@AllArgsConstructor
//public class UserController {
//
//    @GetMapping()
//    public String showUserPage(Model model) {
//        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        model.addAttribute("user", user);
//        model.addAttribute("role", user.printRoles());
//        return "user/userPage";
//    }
//}
