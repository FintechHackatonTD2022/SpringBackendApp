package com.dam1rka.SpringApp.controller;

import com.dam1rka.SpringApp.dto.GetCardResponseDto;
import com.dam1rka.SpringApp.service.OrderService;
import com.dam1rka.SpringApp.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/order/")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private SecurityService securityService;

    @PostMapping("create_card/")
    public ResponseEntity<?> createCard(@RequestParam String token) {
        try {
            orderService.createOrder(securityService.decodeOrder(token));
            return ResponseEntity.accepted().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("get_card/")
    public ResponseEntity<GetCardResponseDto> getCard(@RequestParam String token) {
        return ResponseEntity.ok(orderService.getCard(securityService.decodeGetCard(token)));
    }


}
