package com.snap.reactive.demo.impl.sync;

import com.snap.reactive.demo.api.GetOrderException;
import com.snap.reactive.demo.api.models.Order;
import com.snap.reactive.demo.api.sync.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
class SyncOrderService implements OrderService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final RestTemplate restTemplate;

    @Autowired
    SyncOrderService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Order getOrderByNumber(long orderNumber) {
        logger.info("Now getting order for {}", orderNumber);
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
