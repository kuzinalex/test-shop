package com.kuzin.testTask.services;

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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

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
        Mockito.verify(passwordEncoder,Mockito.times(1)).encode("123");
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
}