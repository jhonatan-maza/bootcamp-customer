package com.nttdata.bootcamp.repository;

import com.nttdata.bootcamp.entity.Customer;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface CustomerRepository extends ReactiveCrudRepository<Customer, String> {
}
