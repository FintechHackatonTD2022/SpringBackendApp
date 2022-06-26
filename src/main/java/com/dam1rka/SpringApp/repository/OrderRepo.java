package com.dam1rka.SpringApp.repository;

import com.dam1rka.SpringApp.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepo extends JpaRepository<OrderEntity, Long> {
    OrderEntity findByMsisdn(String msisdn);
}