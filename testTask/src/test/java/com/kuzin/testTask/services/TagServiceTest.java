package com.kuzin.testTask.services;

import com.kuzin.testTask.entities.Tag;
import com.kuzin.testTask.repositories.TagRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TagServiceTest {

    @Autowired
    private TagService tagService;

    @MockBean
    private TagRepository tagRepository;

    @Test
    public void addTag() {
        Mockito.doReturn(new Tag())
                .when(tagRepository)
                .save(new Tag());

        tagService.addTag(new Tag());

        Mockito.verify(tagRepository,Mockito.times(1)).save(new Tag());
    }

    @Test
    public void getByName() {
        Mockito.doReturn(new Tag())
                .when(tagRepository)
                .getByName("Alex");

        tagService.getByName("Alex");

        Mockito.verify(tagRepository,Mockito.times(1)).getByName("Alex");
    }

    @Test
    public void getAll() {
        Mockito.doReturn(new ArrayList<Tag>())
                .when(tagRepository)
                .findAll();

        tagService.getAll();

        Mockito.verify(tagRepository,Mockito.times(1)).findAll();
    }

    @Test
    public void getById() {
        Mockito.doReturn(new Tag())
                .when(tagRepository)
                .getById(Mockito.anyLong());

        tagService.getById(1L);

        Mockito.verify(tagRepository,Mockito.times(1)).findById(1L);
    }
}