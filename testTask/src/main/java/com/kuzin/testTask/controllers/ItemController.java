package com.kuzin.testTask.controllers;

import com.kuzin.testTask.entities.Item;
import com.kuzin.testTask.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
public class ItemController {

    private CartService cartService;
    private ItemService itemService;
    private RoleService roleService;
    private TagService tagService;
    private UserService userService;

    @Autowired
    public ItemController(CartService cartService, ItemService itemService, RoleService roleService, TagService tagService, UserService userService) {
        this.cartService = cartService;
        this.itemService = itemService;
        this.roleService = roleService;
        this.tagService = tagService;
        this.userService = userService;
    }


    @GetMapping("/items")
    public ResponseEntity<List<Item>> getItems(@RequestParam(required = false) String tag, @RequestParam(required = false) String description) {
        List<Item> items = itemService.getAll(tag, description);
        return ResponseEntity.ok(items);
    }


    @GetMapping("/items/{id}")
    public ResponseEntity<Item> getItem(@PathVariable Long id) {
        return ResponseEntity.ok(itemService.getById(id));
    }


    @PostMapping("/items")
    public ResponseEntity<Item> addItem(@RequestBody Item item) {
        itemService.saveItem(item);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PatchMapping("/items/{id}")
    public ResponseEntity<Item> updateItem(@RequestBody Item item, @PathVariable Long id) {
        itemService.updateItem(item, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @DeleteMapping("/items/{id}")
    public ResponseEntity<Item> deleteItem(@PathVariable Long id) {
        itemService.deleteItem(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PostMapping("/items/{id}")
    public ResponseEntity<Item> addItemToCart(Principal principal, @PathVariable Long id) {
        if (cartService.addToCart(principal, id)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}

