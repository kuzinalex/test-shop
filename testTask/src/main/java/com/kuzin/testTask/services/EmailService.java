package com.kuzin.testTask.services;

import com.kuzin.testTask.entities.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailService {

    private JavaMailSender javaMailSender;

    @Autowired
    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }


    public void sendShoppingListMessage(String to, String username, List<Item> items) {
        SimpleMailMessage message = new SimpleMailMessage();

        String text = generateShoppingListMessage(username, items);


        message.setFrom("dealer-statistics@yandex.by");
        message.setTo(to);
        message.setSubject("Shopping list");
        message.setText(text);
        javaMailSender.send(message);
    }


    public void sendUpdatedItemsMessage(String to, String username, Item item) {

        SimpleMailMessage message = new SimpleMailMessage();

        String text = generateUpdatedItemsMessage(username, item);


        message.setFrom("dealer-statistics@yandex.by");
        message.setTo(to);
        message.setSubject("Your cart has been updated");
        message.setText(text);
        javaMailSender.send(message);
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
