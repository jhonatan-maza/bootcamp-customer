package com.nttdata.bootcamp.service;

import com.mongodb.DuplicateKeyException;
import com.nttdata.bootcamp.entity.Customer;
import com.nttdata.bootcamp.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public Flux<Customer> findAll() {
        Flux<Customer> customers = customerRepository.findAll();
        return customers;
    }

    @Override
    public Mono<Customer> findByDni(String dni) {
        Mono<Customer> customer = customerRepository
                .findAll()
                .filter(x -> x.getDni().equals(dni))
                .next();
        return customer;
    }

    @Override
    public Mono<Customer> save(Customer dataCustomer) {
        return customerRepository.save(dataCustomer);
    }

    @Override
    public Mono<Customer> update(String dni, String status) {
        Mono<Customer> customerMono = findByDni(dni);
        Customer customer = customerMono.block();
        customer.setStatus(status);
        return customerRepository.save(customer);
    }

    @Override
    public Mono<Void> delete() {
        return null;
    }

}
