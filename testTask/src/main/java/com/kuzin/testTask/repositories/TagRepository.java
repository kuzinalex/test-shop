package com.kuzin.testTask.repositories;

import com.kuzin.testTask.entities.Item;
import com.kuzin.testTask.entities.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag,Long> {
    public Tag getByName(String name);
}
