package com.dam1rka.SpringApp.service;

import com.dam1rka.SpringApp.dto.*;
import com.dam1rka.SpringApp.entity.OrderEntity;
import com.dam1rka.SpringApp.entity.OrderResponseEntity;
import com.dam1rka.SpringApp.repository.OrderRepo;
import com.dam1rka.SpringApp.repository.OrderResponseRepo;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class OrderService {

    private static final Integer mechantId = 88;
    private final String wooppayApi = "https://pci-demo.wooppay.com/api";

    @Value("${wooppay.web.key}")
    private String webKey;

    @Value("${card.expired.days}")
    private int daysExpiredForCard;

    private final MultiValueMap<String, String> credentials;

    private final OrderRepo orderRepo;
    private final OrderResponseRepo orderResponseRepo;
    private final SecurityService securityService;


    @Autowired
    public OrderService(OrderRepo orderRepo, OrderResponseRepo orderResponseRepo, SecurityService securityService)
    {
        this.orderRepo = orderRepo;
        this.orderResponseRepo = orderResponseRepo;
        this.securityService = securityService;
        this.credentials = new LinkedMultiValueMap<>();

    }

    @PostConstruct
    private void setAuthoriaztionKey() {
        credentials.add("Authorization", webKey);
    }


    public GetCardResponseDto createOrder(OrderDto orderDto) throws Exception {
        // Create order and save to the database
        OrderEntity order = new OrderEntity();

        order.setMsisdn(orderDto.getMsisdn());
        order.setCreated(new Date());
        order.setCurrency(orderDto.getCurrency());
        order.setAmount(orderDto.getAmount());
        order.setExtraData(orderDto.getExtraData().toString());
        order.setDeleted(false);

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

            String response = new RestTemplate().postForObject(wooppayApi + "/prepaid/create_card", httpEntity, String.class);
            Gson gson = new Gson();

            responseDto = gson.fromJson(response, OrderResponseDto.class);
        }

        if(Objects.isNull(responseDto))
            throw new IllegalAccessException("Can't access to web api server");

        // Save response to the database
        OrderResponseEntity responseEntity = new OrderResponseEntity();

        responseEntity.setOrder(order);
        responseEntity.setCreated(order.getCreated());
        responseEntity.setIid(responseDto.getIid());
        responseEntity.setChd(responseDto.getChd());
        responseEntity.setError_code(responseDto.getCode());
        responseEntity.setError_message(responseDto.getMessage());

        order = orderRepo.save(order);
        orderResponseRepo.save(responseEntity);

        if(Objects.equals(responseDto.getCode(), "-1"))
        {
            GetCardResponseDto response = new GetCardResponseDto();
            response.setCode("-1");
            response.setMessage("Failed");
            return response;
        }

        // return card info
        CardInfoDto cardInfo = securityService.decodeCardInfo(responseEntity.getChd());

        GetCardResponseDto response = new GetCardResponseDto();
        response.setEncrypted(Base64.getEncoder().encodeToString( securityService.encodeCard(cardInfo)));
        response.setCode("0");
        response.setMessage("Success");

        return response;
    }

    public GetCardResponseDto getLastCard(GetCardDto getCardDto) {
        List<OrderEntity> orders = orderRepo.findByMsisdn(getCardDto.getTelephone());

        OrderEntity order = orders.get(orders.size() - 1);

        // check days
        LocalDate d1 = Instant.ofEpochMilli(order.getCreated().getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        LocalDate d2 = LocalDate.now(ZoneId.systemDefault());

        Duration diff = Duration.between(d1.atStartOfDay(), d2.atStartOfDay());
        long diffDays = diff.toDays();

        if(diffDays <= daysExpiredForCard)
        {
            OrderResponseEntity orderResponseEntity = orderResponseRepo.findByOrder(order);

            // return card info
            CardInfoDto cardInfo = securityService.decodeCardInfo(orderResponseEntity.getChd());

            GetCardResponseDto response = new GetCardResponseDto();
            response.setEncrypted(Base64.getEncoder().encodeToString( securityService.encodeCard(cardInfo)));
            response.setCode("0");
            response.setMessage("Success");

            return response;
        } else {
            GetCardResponseDto response = new GetCardResponseDto();
            response.setCode("-1");
            response.setMessage("Can't get card");
            return response;
        }
    }

    public GetCardResponseDto[] getAllCards(GetCardDto getCardDto) {
        return null;
    }

}
