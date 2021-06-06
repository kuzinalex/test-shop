package com.kuzin.testTask.services;

import com.kuzin.testTask.entities.Item;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EmailServiceTest {

    @Autowired
    private EmailService emailService;

    @MockBean
    private JavaMailSender javaMailSender;

    @Test
    public void sendShoppingListMessage() {

        Mockito.doNothing()
                .when(javaMailSender)
                .send(new SimpleMailMessage());

        emailService.sendShoppingListMessage("to", "username", new ArrayList<>());

        SimpleMailMessage message = new SimpleMailMessage();
        String text = generateShoppingListMessage("username", new ArrayList<>());
        message.setFrom("dealer-statistics@yandex.by");
        message.setTo("to");
        message.setSubject("Shopping list");
        message.setText(text);

        Mockito.verify(javaMailSender, Mockito.times(1)).send(message);


    }

    @Test
    public void sendUpdatedItemsMessage() {

        Mockito.doNothing()
                .when(javaMailSender)
                .send(new SimpleMailMessage());

        emailService.sendUpdatedItemsMessage("to", "username", new Item());

        SimpleMailMessage message = new SimpleMailMessage();
        String text = generateUpdatedItemsMessage("username", new Item());
        message.setFrom("dealer-statistics@yandex.by");
        message.setTo("to");
        message.setSubject("Your cart has been updated");
        message.setText(text);

        Mockito.verify(javaMailSender, Mockito.times(1)).send(message);
    }


    private String generateUpdatedItemsMessage(String username, Item item) {

        StringBuilder message = new StringBuilder("Dear " + username + ", some items in your cart has been changed.\n");
        message.append(item.getName() + " - " + item.getDescription() + " has been changed.\n");

        return message.toString();
    }

    private String generateShoppingListMessage(String username, List<Item> items) {

        StringBuilder message = new StringBuilder("Dear " + username + ", thank you for your purchase!\n\nYour shopping list:\n");
        for (Item item : items
        ) {
            message.append(String.valueOf(items.indexOf(item) + 1 + ". " + item.getName() + " - " + item.getDescription() + ".\n"));
        }
        return message.toString();
    }
}