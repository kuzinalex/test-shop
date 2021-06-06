package com.kuzin.testTask.services;

import com.kuzin.testTask.entities.Cart;
import com.kuzin.testTask.entities.User;
import com.kuzin.testTask.repositories.CartRepository;
import com.kuzin.testTask.repositories.RoleRepository;
import com.kuzin.testTask.repositories.UserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;
    @MockBean
    private PasswordEncoder passwordEncoder;
    @MockBean
    private CartRepository cartRepository;
    @MockBean
    private RoleRepository roleRepository;

    @Test
    public void addUser() {
        User user = new User();
        user.setUsername("Alex");
        user.setEmail("test@gmail.com");
        user.setPassword("123");
        Assert.assertTrue(userService.addUser(user));
        Assert.assertNotEquals("123",user.getPassword());

        Mockito.verify(userRepository,Mockito.times(1)).save(user);
        Mockito.verify(passwordEncoder, Mockito.times(1)).encode("123");
        Mockito.verify(cartRepository, Mockito.times(1)).save(new Cart());
    }


    @Test
    public void addUserFail(){
        User user = new User();
        user.setUsername("Alex");

        Mockito.doReturn(new User())
                .when(userRepository)
                .findByUsername("Alex");

        Assert.assertFalse(userService.addUser(user));

        user.setEmail("test@gmail.com");

        Mockito.doReturn(new User())
                .when(userRepository)
                .findByEmail("test@gmail.com");

        Assert.assertFalse(userService.addUser(user));
    }

    @Test
    public void getById() {
        Mockito.doReturn(new User())
                .when(userRepository)
                .getById(1L);

        Assert.assertEquals(new User(), userService.getById(1L));

        Mockito.doReturn(null)
                .when(userRepository)
                .getById(2L);

        Assert.assertNull(userService.getById(2L));
    }

    @Test
    public void getByUsername() {
        Mockito.doReturn(new User())
                .when(userRepository)
                .findByUsername("Alex");

        Assert.assertEquals(new User(), userService.getByUsername("Alex"));

        Mockito.doReturn(null)
                .when(userRepository)
                .findByUsername("Denis");

        Assert.assertNull(userService.getByUsername("Denis"));
    }

    @Test
    public void getByEmail() {
        Mockito.doReturn(new User())
                .when(userRepository)
                .findByEmail("existingEmail@gmail.com");

        Assert.assertEquals(new User(), userService.getByEmail("existingEmail@gmail.com"));

        Mockito.doReturn(null)
                .when(userRepository)
                .findByEmail("notExistingEmail@gmail.com");

        Assert.assertNull(userService.getByEmail("notExistingEmail@gmail.com"));
    }

    @Test
    public void loadUserByUsername() {
        Mockito.doReturn(new User(1L,"Alex","email.com","123",new ArrayList<>()))
                .when(userRepository)
                .findByUsername("Alex");

        Assert.assertEquals(new org.springframework.security.core.userdetails.User(
                userRepository.findByUsername("Alex").getUsername(),
                userRepository.findByUsername("Alex").getPassword(),
                new ArrayList<GrantedAuthority>()),userService.loadUserByUsername("Alex"));
    }
}