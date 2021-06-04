package com.kuzin.testTask.controllers;

import com.kuzin.testTask.entities.User;
import com.kuzin.testTask.services.RoleService;
import com.kuzin.testTask.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegistrationController {

    private RoleService roleService;
    private UserService userService;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationController(RoleService roleService, UserService userService, PasswordEncoder passwordEncoder) {
        this.roleService = roleService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/user/signUp")
    public ResponseEntity<User> signUp(@RequestBody User user) {
        if (userService.getByUsername(user.getUsername()) != null | userService.getByEmail(user.getEmail()) != null | user.getUsername().contains(" ")) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userService.addUser(user);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

}
