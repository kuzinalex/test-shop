package com.kuzin.testTask.services;

import com.kuzin.testTask.entities.Role;
import com.kuzin.testTask.repositories.RoleRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RoleServiceTest {

    @Autowired
    private RoleService roleService;

    @MockBean
    private RoleRepository roleRepository;

    @Test
    public void getByName() {
        Mockito.doReturn(new Role())
                .when(roleRepository)
                .getByName(Mockito.anyString());

        roleService.getByName("ROLE_USER");

        Mockito.verify(roleRepository,Mockito.times(1)).getByName("ROLE_USER");

    }
}