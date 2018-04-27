package com.snap.reactive.demo.services.sync;

import com.snap.reactive.demo.models.Customer;
import com.snap.reactive.demo.services.GetCustomerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
class DefaultCustomerService implements CustomerService {

    private final RestTemplate restTemplate;

    @Autowired
    DefaultCustomerService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Customer getCustomerByEmail(String email) {
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
