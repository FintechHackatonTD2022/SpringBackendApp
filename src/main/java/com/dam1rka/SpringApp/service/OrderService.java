package com.dam1rka.SpringApp.service;

import com.dam1rka.SpringApp.dto.OrderDto;
import com.dam1rka.SpringApp.dto.OrderResponseDto;
import com.dam1rka.SpringApp.dto.WpOrderDto;
import com.dam1rka.SpringApp.entity.OrderEntity;
import com.dam1rka.SpringApp.entity.OrderResponseEntity;
import com.dam1rka.SpringApp.repository.OrderRepo;
import com.dam1rka.SpringApp.repository.OrderResponseRepo;
import com.google.gson.Gson;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.Objects;

@Service
public class OrderService {

    private static final Integer mechantId = 88;

    private final MultiValueMap<String, String> credentials;

    private final OrderRepo orderRepo;
    private final OrderResponseRepo orderResponseRepo;

    private final String woopayApi = "https://pci-demo.wooppay.com/api";

    @Autowired
    public OrderService(OrderRepo orderRepo, OrderResponseRepo orderResponseRepo)
    {
        this.orderRepo = orderRepo;
        this.orderResponseRepo = orderResponseRepo;
        this.credentials = new LinkedMultiValueMap<>();
        credentials.add("Authorization", "Basic aGFja2F0aG9uNTpoYWNrYXRob24tc2FicjJjNTE=");
    }


    public void createOrder(OrderDto orderDto) throws Exception {

        // Create order and save to the database
        OrderEntity order = new OrderEntity();

        order.setMsisdn(orderDto.getMsisdn());
        order.setCreated(new Date());
        order.setCurrency(orderDto.getCurrency());
        order.setAmount(orderDto.getAmount());
        order.setExtraData(orderDto.getExtraData().toString());
        order.setDeleted(false);

        order = orderRepo.save(order);

        OrderResponseDto responseDto;
        // Make request to wooppay api
        {
            WpOrderDto request = new WpOrderDto();
            request.merchant_id = mechantId;
            request.currency = order.getCurrency();
            request.amount = order.getAmount();
            request.msisdn = order.getMsisdn();
            request.order_id = String.valueOf(order.getId());
            request.extra_data = orderDto.getExtraData();

            HttpEntity<WpOrderDto> httpEntity = new HttpEntity<>(request, credentials);

            String response = new RestTemplate().postForObject(woopayApi + "/prepaid/create_card", httpEntity, String.class);
            Gson gson = new Gson();

            responseDto = gson.fromJson(response, OrderResponseDto.class);
        }

        if(Objects.isNull(responseDto))
            throw new Exception("Null response");

        if(!Objects.equals(responseDto.getCode(), "0"))
            throw new Exception("Not valid order");

        // Save response to the database
        OrderResponseEntity responseEntity = new OrderResponseEntity();

        responseEntity.setOrder(order);
        responseEntity.setCreated(order.getCreated());
        responseEntity.setIid(responseDto.getIid());
        responseEntity.setError_code(responseDto.getCode());
        responseEntity.setError_message(responseDto.getMessage());

        orderResponseRepo.save(responseEntity);
    }

    public OrderResponseDto getCard() {
        return null;
    }

}
