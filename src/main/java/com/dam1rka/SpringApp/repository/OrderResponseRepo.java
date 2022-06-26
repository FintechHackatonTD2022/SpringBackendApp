package com.dam1rka.SpringApp.repository;

import com.dam1rka.SpringApp.entity.OrderEntity;
import com.dam1rka.SpringApp.entity.OrderResponseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderResponseRepo extends JpaRepository<OrderResponseEntity, Long> {

    OrderResponseEntity findByOrder(OrderEntity order);
}
