package com.kuzin.testTask.repositories;

import com.kuzin.testTask.entities.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.stereotype.Repository;

//@Repository
public interface ItemRepository extends JpaRepository<Item,Long> {
}
