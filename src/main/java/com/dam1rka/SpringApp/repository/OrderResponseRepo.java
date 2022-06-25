package com.dam1rka.SpringApp.repository;

import com.dam1rka.SpringApp.entity.OrderResponseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderResponseRepo extends JpaRepository<OrderResponseEntity, Long> {

}
