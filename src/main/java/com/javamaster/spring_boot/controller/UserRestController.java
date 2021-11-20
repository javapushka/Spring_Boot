package com.javamaster.spring_boot.controller;

import com.javamaster.spring_boot.entity.User;
import com.javamaster.spring_boot.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
public class UserRestController {


    @GetMapping("/userInfo")
    public ResponseEntity<User> userInfo() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

}
