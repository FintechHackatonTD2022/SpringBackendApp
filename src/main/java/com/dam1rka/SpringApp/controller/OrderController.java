package com.dam1rka.SpringApp.controller;

import com.dam1rka.SpringApp.dto.EncryptedDto;
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
    public ResponseEntity<?> createCard(@RequestBody EncryptedDto encryptedDto) {
        try {
            GetCardResponseDto res = orderService.createOrder(securityService.decodeOrder(encryptedDto.getEncrypted()));
            return ResponseEntity.accepted().body(res);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("get_card/")
    public ResponseEntity<GetCardResponseDto> getCard(@RequestBody EncryptedDto encryptedDto) {
        return ResponseEntity.ok(orderService.getLastCard(securityService.decodeGetCard(encryptedDto.getEncrypted())));
    }


}
