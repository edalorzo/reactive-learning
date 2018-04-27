package com.snap.reactive.demo.services.sync;

import com.snap.reactive.demo.models.Order;
import com.snap.reactive.demo.services.GetOrderException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
class DefaultOrderService implements OrderService {

    private final RestTemplate restTemplate;

    @Autowired
    DefaultOrderService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Order getOrderByNumber(long orderNumber) {
        try {
            String endpoint = "http://localhost:4040/orders/{orderNumber}";
            ResponseEntity<Order> response = restTemplate.getForEntity(endpoint, Order.class, orderNumber);
            return response.getBody();
        }
        catch (Exception cause) {
            throw new GetOrderException(orderNumber, cause);
        }
    }

}
