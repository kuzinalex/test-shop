package com.kuzin.testTask.services;

import com.kuzin.testTask.entities.Tag;
import com.kuzin.testTask.repositories.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TagService {

    private TagRepository tagRepository;

    @Autowired
    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public Optional<Tag> getById(Long id){
        return tagRepository.findById(id);
    }

    public Tag getByName(String name){
        return tagRepository.getByName(name);
    }

    public List<Tag> getAll(){
        return tagRepository.findAll();
    }

    public void addTag(Tag tag){
        tagRepository.save(tag);
    }
}
