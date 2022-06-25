package com.dam1rka.SpringApp.controller;

import com.dam1rka.SpringApp.dto.OrderDto;
import com.dam1rka.SpringApp.dto.OrderResponseDto;
import com.dam1rka.SpringApp.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/order/")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("create_card/")
    public ResponseEntity<?> createCard(@RequestBody OrderDto orderDto) {
        try {
            orderService.createOrder(orderDto);
            return ResponseEntity.accepted().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("get_card/")
    public ResponseEntity<?> getCard(@RequestBody OrderDto orderDto) {
        try {
            orderService.createOrder(orderDto);
            return ResponseEntity.accepted().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
