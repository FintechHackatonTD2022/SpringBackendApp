package com.dam1rka.SpringApp.repository;

import com.dam1rka.SpringApp.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepo extends JpaRepository<OrderEntity, Long> {
    List<OrderEntity> findByMsisdn(String msisdn);
}