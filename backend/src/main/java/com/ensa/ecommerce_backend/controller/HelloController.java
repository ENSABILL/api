package com.ensa.ecommerce_backend.controller;

import com.ensa.ecommerce_backend.entity.UserEntity;
import com.ensa.ecommerce_backend.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class HelloController {
    private UserService userService;
    @GetMapping("/users")
    public ResponseEntity<List<UserEntity>> hello(){
        System.out.println(userService.findAll());
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }
}
