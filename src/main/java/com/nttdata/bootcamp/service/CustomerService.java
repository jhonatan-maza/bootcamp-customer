package com.nttdata.bootcamp.service;

import com.nttdata.bootcamp.entity.Customer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

//Interface Service
public interface CustomerService {

    public Flux<Customer> findAll();
    public Mono<Customer> findByDni(String dni);
    public Mono<Customer> save(Customer customer);
    public Mono<Customer> update(Customer dataCustomer);
    public Mono<Void> delete(String dni);

}
