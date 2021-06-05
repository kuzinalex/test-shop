package com.kuzin.testTask.controllers;

import com.kuzin.testTask.entities.Cart;
import com.kuzin.testTask.entities.User;
import com.kuzin.testTask.services.CartService;
import com.kuzin.testTask.services.RoleService;
import com.kuzin.testTask.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class RegistrationController {

    private RoleService roleService;
    private UserService userService;
    private CartService cartService;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationController(RoleService roleService, UserService userService, CartService cartService, PasswordEncoder passwordEncoder) {
        this.roleService = roleService;
        this.userService = userService;
        this.cartService = cartService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/user/signUp")
    public ResponseEntity<String> signUp(@RequestBody User user) {
        if (userService.getByUsername(user.getUsername()) != null | userService.getByEmail(user.getEmail()) != null | user.getUsername().contains(" ")) {
            return  ResponseEntity.badRequest().body("User already exists.");
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRoles(new ArrayList<>());
            user.getRoles().add(roleService.getByName("ROLE_USER"));
            userService.addUser(user);
            Cart cart=new Cart();
            cart.setUser(userService.getByUsername(user.getUsername()));
            cartService.save(cart);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
    }

}
