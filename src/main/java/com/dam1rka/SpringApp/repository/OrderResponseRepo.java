package com.dam1rka.SpringApp.repository;

import com.dam1rka.SpringApp.entity.OrderEntity;
import com.dam1rka.SpringApp.entity.OrderResponseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderResponseRepo extends JpaRepository<OrderResponseEntity, Long> {

    OrderResponseEntity findByOrder(OrderEntity order);

    @Query("SELECT ore FROM OrderResponseEntity ore LEFT JOIN OrderEntity oe ON oe = ore.order WHERE ore.error_code = '0' AND oe.msisdn = :telephone")
    List<OrderResponseEntity> findAllByOrders(String telephone);


}
