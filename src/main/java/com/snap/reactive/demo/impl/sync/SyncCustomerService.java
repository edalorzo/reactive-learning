package com.snap.reactive.demo.impl.sync;

import com.snap.reactive.demo.api.GetCustomerException;
import com.snap.reactive.demo.api.models.Customer;
import com.snap.reactive.demo.api.sync.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
class SyncCustomerService implements CustomerService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final RestTemplate restTemplate;

    @Autowired
    SyncCustomerService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Customer getCustomerByEmail(String email) {
        logger.info("Now getting Customer for {}", email);
        try {
            String endpoint = "http://localhost:4040/customer/{customerEmail}";
            ResponseEntity<Customer> response = restTemplate.getForEntity(endpoint, Customer.class, email);
            return response.getBody();
        }
        catch (Exception cause) {
            throw new GetCustomerException(email, cause);
        }
    }
}
