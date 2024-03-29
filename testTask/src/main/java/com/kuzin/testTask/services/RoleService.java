package com.kuzin.testTask.services;

import com.kuzin.testTask.entities.Role;
import com.kuzin.testTask.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    private RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }


    public Role getByName(String name){
      return   roleRepository.getByName(name);
    }
}
