package com.snap.reactive.demo.api.sync;

import com.snap.reactive.demo.api.models.Customer;

public interface CustomerService {
    Customer getCustomerByEmail(String email);
}
