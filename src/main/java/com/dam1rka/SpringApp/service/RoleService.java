package com.dam1rka.SpringApp.service;

import com.dam1rka.SpringApp.entity.RoleEntity;
import com.dam1rka.SpringApp.entity.RoleEnum;
import com.dam1rka.SpringApp.repository.RoleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    @Autowired
    private RoleRepo roleRepo;

    public void loadAllRoles() {
        for(RoleEnum r : RoleEnum.values())
        {
            RoleEntity roleEntity = new RoleEntity();
            roleEntity.setName(r.name());
            roleRepo.save(roleEntity);
        }
    }

}
