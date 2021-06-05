package com.kuzin.testTask.services;

import com.kuzin.testTask.entities.Cart;
import com.kuzin.testTask.entities.Item;
import com.kuzin.testTask.entities.Tag;
import com.kuzin.testTask.repositories.CartRepository;
import com.kuzin.testTask.repositories.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemService {

    private EmailService emailService;
    private TagService tagService;
    private ItemRepository itemRepository;
    private CartRepository cartRepository;

    @Autowired
    public ItemService(EmailService emailService, ItemRepository itemRepository, TagService tagService, CartRepository cartRepository) {
        this.emailService = emailService;

        this.itemRepository = itemRepository;
        this.tagService = tagService;
        this.cartRepository = cartRepository;
    }


    public List<Item> getAll(String tag, String description) {

        List<Item> items= itemRepository.findAll();

        if (tag == null & description == null) {
            return items;
        } else if (tag != null) {

            items = items.stream()
                    .filter(item -> item.getTags().stream().anyMatch(tag1 -> tag1.getName().equals(tag)))
                    .collect(Collectors.toList());
        }
        if (description != null) {
            items = items.stream()
                    .filter(item -> item.getDescription().equals(description))
                    .collect(Collectors.toList());
        }
        return items;
    }


    public Item getById(Long id) {
        return itemRepository.findById(id).get();
    }


    public void saveItem(Item item) {
        if (item.getTags() != null) {
            List<Tag> list = new ArrayList<>();
            for (Tag tag : item.getTags()
            ) {
                if (tagService.getByName(tag.getName()) == null) {
                    tagService.addTag(tag);
                }
                list.add(tagService.getByName(tag.getName()));
            }
            item.setTags(list);
            itemRepository.save(item);
        }

    }


    public void updateItem(Item item, Long id) {
        Item existingItem = getById(id);

        if (item.getName() != null) {
            existingItem.setName(item.getName());
        }
        if (item.getDescription() != null) {
            existingItem.setDescription(item.getDescription());
        }
        if (item.getTags() != null) {
            List<Tag> list = new ArrayList<>();
            for (Tag tag : item.getTags()
            ) {
                if (tagService.getByName(tag.getName()) == null) {
                    tagService.addTag(tag);
                }
                if (!existingItem.getTags().contains(tagService.getByName(tag.getName()))) {
                    list.add(tagService.getByName(tag.getName()));
                }
            }

            existingItem.getTags().addAll(list);
        }
        saveItem(existingItem);
    }


    public void deleteItem(Long id) {
        List<Cart> carts = cartRepository.findAll();
        Item deletingItem = getById(id);

        for (Cart cart : carts
        ) {
            if (cart.getItems().contains(deletingItem)) {
                emailService.sendUpdatedItemsMessage(cart.getUser().getEmail(), cart.getUser().getUsername(), deletingItem);
                cart.getItems().remove(deletingItem);
            }
        }

        itemRepository.deleteById(id);
    }


    public boolean tryToUpdateItem(Item item, Long id) {
        List<Cart> carts = cartRepository.findAll();
        Item updatingItem = getById(id);

        for (Cart cart : carts
        ) {
            if (cart.getItems().contains(updatingItem)) {
                //System.out.println("Some users have add this item into their carts. Use force update to update it.");
                return false;
            }
        }
        updateItem(item, id);
        return true;
    }


    public void forceUpdate(Item item, Long id) {
        List<Cart> carts = cartRepository.findAll();
        Item updatingItem = getById(id);

        for (Cart cart : carts
        ) {
            if (cart.getItems().contains(updatingItem)) {
                emailService.sendUpdatedItemsMessage(cart.getUser().getEmail(), cart.getUser().getUsername(), updatingItem);
            }
        }
        updateItem(item, id);
    }
}
