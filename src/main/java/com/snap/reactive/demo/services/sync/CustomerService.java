package com.snap.reactive.demo.services.sync;

import com.snap.reactive.demo.models.Customer;

public interface CustomerService {
    Customer getCustomerByEmail(String email);
}
