package com.kuzin.testTask.controllers;

import com.kuzin.testTask.entities.Item;
import com.kuzin.testTask.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
public class CartController {

    private CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }


    @GetMapping("/cart")
    public ResponseEntity<List<Item>> getCartItems(Principal principal) {
        return ResponseEntity.ok(cartService.getCartItems(principal));
    }


    @DeleteMapping("/cart/{id}")
    public ResponseEntity<String> deleteFromCart(@PathVariable Long id, Principal principal) {
        if (cartService.deleteFromCart(id, principal)){
            return new ResponseEntity<>(HttpStatus.OK);
        }else {
            return ResponseEntity.badRequest().body("No such item in cart");
        }

    }


    @PostMapping("/cart/buy")
    public ResponseEntity<String> buy(Principal principal){
        if ( cartService.buy(principal)){
            return new ResponseEntity<>(HttpStatus.OK);
        }else{
            return ResponseEntity.badRequest().body("Your cart is empty!");
        }


    }

}
