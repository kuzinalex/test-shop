package com.kuzin.testTask.repositories;

import com.kuzin.testTask.entities.Cart;
import com.kuzin.testTask.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart,Long> {
    public Cart getByUser(User user);
}
