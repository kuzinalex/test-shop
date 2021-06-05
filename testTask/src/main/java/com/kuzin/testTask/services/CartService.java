package com.kuzin.testTask.services;

import com.kuzin.testTask.entities.Cart;
import com.kuzin.testTask.entities.Item;
import com.kuzin.testTask.entities.User;
import com.kuzin.testTask.repositories.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {

    private UserService userService;
    private ItemService itemService;
    private CartRepository cartRepository;
    private EmailService emailService;

    @Autowired
    public CartService(UserService userService, ItemService itemService, CartRepository cartRepository, EmailService emailService) {
        this.userService = userService;
        this.itemService = itemService;
        this.cartRepository = cartRepository;
        this.emailService = emailService;
    }


    public List<Cart> getAll(){
       return cartRepository.findAll();
    }


    public List<Item> getCartItems(Principal principal) {
        User user = userService.getByUsername(principal.getName());
        Cart cart = getByUser(user);
        if (cart.getItems() == null) {
            return new ArrayList<>();
        }
        else {
            return cart.getItems();
        }
    }


    public Cart getByUser(User user) {
        return cartRepository.getByUser(user);
    }


    public void save(Cart cart) {
        cartRepository.save(cart);
    }


    public boolean addToCart(Principal principal, Long id) {

        User user = userService.getByUsername(principal.getName());
        Cart cart = getByUser(user);
        Item item = itemService.getById(id);

        if (cart.getItems() == null) {
            List<Item> items = new ArrayList<>();
            items.add(item);
            cart.setItems(items);
        } else if (cart.getItems().contains(item)) {
            return false;
        } else {
            cart.getItems().add(itemService.getById(id));
        }
        save(cart);
        return true;
    }


    public void deleteFromCart(Long id,Principal principal){
        User user = userService.getByUsername(principal.getName());
        Cart cart = getByUser(user);
        Item deletingItem = itemService.getById(id);

        if (cart.getItems() != null && cart.getItems().contains(deletingItem)) {
            cart.getItems().remove(deletingItem);
            save(cart);
        }
    }


    public boolean buy(Principal principal) {
        User user = userService.getByUsername(principal.getName());
        Cart cart = getByUser(user);

        if (cart.getItems() != null && !cart.getItems().isEmpty()) {
            emailService.sendShoppingListMessage(user.getEmail(), user.getUsername(), cart.getItems());
            cart.getItems().clear();
            save(cart);
            return true;
        } else {
            return false;
        }
    }
}
