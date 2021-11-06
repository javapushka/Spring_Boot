package com.javamaster.spring_boot.repository;

import com.javamaster.spring_boot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByName(String name);
    List<User> findAll();
}
