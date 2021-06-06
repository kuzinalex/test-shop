package com.kuzin.testTask.services;

import com.kuzin.testTask.entities.Cart;
import com.kuzin.testTask.entities.Item;
import com.kuzin.testTask.entities.User;
import com.kuzin.testTask.repositories.CartRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.Principal;
import java.util.ArrayList;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CartServiceTest {

    @Autowired
    private CartService cartService;

    @MockBean
    private CartService innerCartService;

    @MockBean
    private UserService userService;

    @MockBean
    private ItemService itemService;

    @MockBean
    private CartRepository cartRepository;

    @MockBean
    private EmailService emailService;

    @Test
    public void getAll() {
        Assert.assertEquals(new ArrayList<Cart>(), cartService.getAll());
    }

    @Test
    public void getCartItems() {
        Mockito.doReturn(new User())
                .when(userService)
                .getByUsername("Alex");

        Mockito.doReturn(new Cart())
                .when(innerCartService)
                .getByUser(new User());

        Assert.assertEquals(new ArrayList<Item>(), cartService.getCartItems(new Principal() {
            @Override
            public String getName() {
                return "Alex";
            }
        }));
    }



    @Test
    public void addToCart() {
        Mockito.doReturn(new User())
                .when(userService)
                .getByUsername("Alex");

        Mockito.doReturn(new Cart())
                .when(innerCartService)
                .getByUser(new User());

        Mockito.doReturn(new Item())
                .when(itemService)
                .getById(1L);

        Assert.assertFalse(cartService.addToCart(new Principal() {
            @Override
            public String getName() {
                return "Alex";
            }
        },1L));

    }

    @Test
    public void deleteFromCart() {
        Mockito.doReturn(new User())
                .when(userService)
                .getByUsername("Alex");

        Mockito.doReturn(new Cart())
                .when(innerCartService)
                .getByUser(new User());

        Mockito.doReturn(new Item())
                .when(itemService)
                .getById(1L);

        Assert.assertFalse(cartService.deleteFromCart(1L,new Principal() {
            @Override
            public String getName() {
                return "Alex";
            }
        }));

    }

    @Test
    public void buy() {
        User user=new User();
        user.setUsername("Alex");
        Cart cart=new Cart();
        cart.setUser(user);
        cart.setItems(new ArrayList<>());

        Mockito.doReturn(user)
                .when(userService)
                .getByUsername("Alex");

        Mockito.doReturn(cart)
                .when(innerCartService)
                .getByUser(user);

        Assert.assertFalse(cartService.buy(new Principal() {
            @Override
            public String getName() {
                return "Alex";
            }
        }));

    }
}