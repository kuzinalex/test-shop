package com.kuzin.testTask.controllers;

import com.kuzin.testTask.entities.Item;
import com.kuzin.testTask.services.CartService;
import com.kuzin.testTask.services.ItemService;
import com.kuzin.testTask.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
public class CartController {

    private UserService userService;
    private CartService cartService;
    private ItemService itemService;

    @Autowired
    public CartController(UserService userService, CartService cartService, ItemService itemService) {
        this.userService = userService;
        this.cartService = cartService;
        this.itemService = itemService;
    }


    @GetMapping("/cart")
    public ResponseEntity<List<Item>> getCartItems(Principal principal) {
        return ResponseEntity.ok(cartService.getCartItems(principal));
    }


    @DeleteMapping("/cart/{id}")
    public ResponseEntity<Item> deleteFromCart(@PathVariable Long id, Principal principal) {
        cartService.deleteFromCart(id, principal);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PostMapping("/cart/buy")
    public ResponseEntity<String> buy(Principal principal){
        if ( cartService.buy(principal)){
            return new ResponseEntity<>(HttpStatus.OK);
        }else{
            //System.out.println("Your cart is empty!");
            return ResponseEntity.badRequest().body("Your cart is empty!");
        }


    }

}
