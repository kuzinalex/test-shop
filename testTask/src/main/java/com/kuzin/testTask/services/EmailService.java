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


    public void sendSimpleMessage(String to, String username, List<Item> items) {
        SimpleMailMessage message = new SimpleMailMessage();

        String text = generateMessage(username, items);

        message.setFrom("dealer-statistics@yandex.by");
        message.setTo(to);
        message.setSubject("Shopping list");
        message.setText(text);
        javaMailSender.send(message);
    }

    private String generateMessage(String username, List<Item> items) {
        StringBuilder message = new StringBuilder("Dear "+username+", thank you for your purchase!\n\nYour shopping list:\n");
        for (Item item : items
        ) {
            message.append(String.valueOf(items.indexOf(item) + ". " + item.getName()+" - "+item.getDescription() + ".\n"));
        }
        return message.toString();
    }
}
