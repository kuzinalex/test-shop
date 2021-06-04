package com.kuzin.testTask.services;

import com.kuzin.testTask.entities.Item;
import com.kuzin.testTask.entities.Tag;
import com.kuzin.testTask.repositories.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemService {

    private TagService tagService;
    private ItemRepository itemRepository;

    @Autowired
    public ItemService(ItemRepository itemRepository, TagService tagService) {

        this.itemRepository = itemRepository;
        this.tagService = tagService;
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
                list.add(tagService.getByName(tag.getName()));
            }

            existingItem.getTags().addAll(list);
        }
        saveItem(existingItem);
    }


    public void deleteItem(Long id) {
        itemRepository.deleteById(id);
    }
}
