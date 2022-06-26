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
//    @ApiParam(value = "Create new card;" +
//            "Input - encrypted json" +
//            "{" +
//            "  \"currency\": 398," +
//            " \"amount\": \"120000.00\"," +
//            " \"msisdn\": \"77773546064\"," +
//            " \"extra_data\":" +
//            "  {" +
//            "   \"INN\": \"133713371337\"" +
//            "  }," +
//            " \"iat\": 1656171726," +
//            " \"exp\": 1656175326" +
//            "}"
//    )
    public ResponseEntity<?> createCard(@RequestBody EncryptedDto encryptedDto) {
        try {
            GetCardResponseDto res = orderService.createOrder(securityService.decodeOrder(encryptedDto.getEncrypted()));
            return ResponseEntity.accepted().body(res);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("get_card/")
//    @ApiParam(value = "Return only last card from card holder;" +
//            "Input - encrypted json" +
//            "{\n" +
//            "\"telephone\": \"7777017156\",\n" +
//            "\"iin\": \"126733279987\"\n" +
//            "}")
    public ResponseEntity<?> getCard(@RequestBody EncryptedDto encryptedDto) {
        try {
            return ResponseEntity.accepted().body(orderService.getLastCard(securityService.decodeGetCard(encryptedDto.getEncrypted())));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("get_cards/")
//    @ApiParam(value = "Return all card from card holder;" +
//            "Input - encrypted json" +
//            "{\n" +
//            "\"telephone\": \"7777017156\",\n" +
//            "\"iin\": \"126733279987\"\n" +
//            "}")
    public ResponseEntity<?> getCards(@RequestBody EncryptedDto encryptedDto) {
        try {
            return ResponseEntity.accepted().body(orderService.getAllCards(securityService.decodeGetCard(encryptedDto.getEncrypted())));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
