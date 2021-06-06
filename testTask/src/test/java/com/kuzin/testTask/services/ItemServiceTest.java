package com.kuzin.testTask.services;

import com.kuzin.testTask.entities.Cart;
import com.kuzin.testTask.entities.Item;
import com.kuzin.testTask.entities.Tag;
import com.kuzin.testTask.repositories.CartRepository;
import com.kuzin.testTask.repositories.ItemRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ItemServiceTest {

    @Autowired
    private ItemService itemService;

    @MockBean
    private ItemRepository itemRepository;

    @MockBean
    private EmailService emailService;

    @MockBean
    private TagService tagService;

    @MockBean
    private CartRepository cartRepository;

    @MockBean
    private JavaMailSender mailSender;

    @Test
    public void getAll() {
        Mockito.doReturn(new ArrayList<Item>())
                .when(itemRepository)
                .findAll();

        itemService.getAll(null, null);

        Mockito.verify(itemRepository, Mockito.times(1)).findAll();
    }

    @Test
    public void getById() {
        Mockito.doReturn(Optional.of(new Item()))
                .when(itemRepository)
                .findById(Mockito.anyLong());

        itemService.getById(1L);

        Mockito.verify(itemRepository, Mockito.times(1)).findById(1L);
    }

    @Test
    public void saveItem() {
        Tag tag = new Tag();
        tag.setName("TAG");
        List<Tag> list = new ArrayList<>();
        list.add(tag);
        Item item = new Item();
        item.setTags(list);

        Mockito.doReturn(item)
                .when(itemRepository)
                .save(item);

        Mockito.doReturn(new Tag())
                .when(tagService)
                .getByName(Mockito.anyString());

        Mockito.doNothing()
                .when(tagService)
                .addTag(new Tag());

        itemService.saveItem(item);

        Mockito.verify(itemRepository, Mockito.times(1)).save(item);
        Mockito.verify(tagService, Mockito.times(2)).getByName("TAG");
        Mockito.verify(tagService, Mockito.times(0)).addTag(tag);
    }

    @Test
    public void updateItem() {
        Tag tag = new Tag();
        tag.setName("TAG");
        List<Tag> list = new ArrayList<>();
        list.add(tag);
        Item item = new Item();
        item.setTags(list);

        Mockito.doReturn(Optional.of(item))
                .when(itemRepository)
                .findById(Mockito.anyLong());

        Mockito.doReturn(new Tag())
                .when(tagService)
                .getByName(Mockito.anyString());

        Mockito.doNothing()
                .when(tagService)
                .addTag(new Tag());

        itemService.updateItem(item, Mockito.anyLong());

        Mockito.verify(itemRepository, Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verify(tagService, Mockito.times(5)).getByName(Mockito.anyString());
        Mockito.verify(tagService, Mockito.times(1)).addTag(new Tag());
    }

    @Test
    public void deleteItem() {
        Mockito.doReturn(new ArrayList<Cart>())
                .when(cartRepository)
                .findAll();

        Mockito.doReturn(Optional.of(new Item()))
                .when(itemRepository)
                .findById(Mockito.anyLong());


        Mockito.doNothing()
                .when(itemRepository)
                .deleteById(Mockito.anyLong());


        itemService.deleteItem(1L);

        Mockito.verify(cartRepository, Mockito.times(1)).findAll();
        Mockito.verify(itemRepository, Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verify(itemRepository, Mockito.times(1)).deleteById(Mockito.anyLong());
    }

    @Test
    public void tryToUpdateItem() {
        Item item = new Item();
        Cart cart = new Cart();
        List<Cart> carts = new ArrayList<>();
        carts.add(cart);
        cart.setItems(new ArrayList<>());
        cart.getItems().add(item);


        Mockito.doReturn(carts)
                .when(cartRepository)
                .findAll();

        Mockito.doReturn(Optional.of(item))
                .when(itemRepository)
                .findById(Mockito.anyLong());

        Assert.assertFalse(itemService.tryToUpdateItem(item, 1L));
    }

    @Test
    public void forceUpdate() {
        Item item = new Item();
        Cart cart = new Cart();
        List<Cart> carts = new ArrayList<>();
        carts.add(cart);
        cart.setItems(new ArrayList<>());
        //cart.getItems().add(item);
        Mockito.doReturn(carts)
                .when(cartRepository)
                .findAll();

        Mockito.doReturn(Optional.of(item))
                .when(itemRepository)
                .findById(Mockito.anyLong());

        Assert.assertTrue(itemService.tryToUpdateItem(item, 1L));
    }
}