package com.nttdata.bootcamp.service;

import ch.qos.logback.core.status.ErrorStatus;
import com.nttdata.bootcamp.entity.Customer;
import com.nttdata.bootcamp.repository.CustomerRepository;
import org.apache.logging.log4j.message.Message;
import org.bouncycastle.asn1.cmp.ErrorMsgContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.text.MessageFormat;
import java.time.Duration;
import java.util.DuplicateFormatFlagsException;
import java.util.function.Supplier;

//Service implementation
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
        Mono<Customer> customerMono = findByDni(dataCustomer.getDni())
                .flatMap(__ -> Mono.<Customer>error(new Error("El cliente xxx con dni " + dataCustomer.getDni() + " YA EXISTE")))
                .switchIfEmpty(customerRepository.save(dataCustomer));
        return customerMono;
    }

    @Override
    public Mono<Customer> updateAddress(Customer dataCustomer) {
        Mono<Customer> customerMono = findByDni(dataCustomer.getDni());
                //.delayElement(Duration.ofMillis(1000));
        try {
            dataCustomer.setId(customerMono.block().getId());
            dataCustomer.setTypeCustomer(customerMono.block().getTypeCustomer());
            dataCustomer.setFlagVip(customerMono.block().getFlagVip());
            dataCustomer.setFlagPyme(customerMono.block().getFlagPyme());
            dataCustomer.setName(customerMono.block().getName());
            dataCustomer.setSurName(customerMono.block().getSurName());
            dataCustomer.setStatus(customerMono.block().getStatus());
            dataCustomer.setCreationDate(customerMono.block().getCreationDate());
            return customerRepository.save(dataCustomer);
        }catch (Exception e){
            return Mono.<Customer>error(new Error("El cliente con dni " + dataCustomer.getDni() + " NO EXISTE"));
        }
    }

    @Override
    public Mono<Customer> updateStatus(Customer dataCustomer) {
        Mono<Customer> customerMono = findByDni(dataCustomer.getDni());
        //.delayElement(Duration.ofMillis(1000));
        try {
            dataCustomer.setId(customerMono.block().getId());
            dataCustomer.setTypeCustomer(customerMono.block().getTypeCustomer());
            dataCustomer.setFlagVip(customerMono.block().getFlagVip());
            dataCustomer.setFlagPyme(customerMono.block().getFlagPyme());
            dataCustomer.setName(customerMono.block().getName());
            dataCustomer.setSurName(customerMono.block().getSurName());
            dataCustomer.setAddress(customerMono.block().getAddress());
            dataCustomer.setCreationDate(customerMono.block().getCreationDate());
            return customerRepository.save(dataCustomer);
        }catch (Exception e){
            return Mono.<Customer>error(new Error("El cliente con dni " + dataCustomer.getDni() + " NO EXISTE"));
        }
    }

    @Override
    public Mono<Void> delete(String dni) {
        Mono<Customer> customerMono = findByDni(dni);
        //customerMono.subscribe();
        try {
            return customerRepository.delete(customerMono.block());
        }catch (Exception e){
            return Mono.<Void>error(new Error("El cliente con dni " + dni + " NO EXISTE"));
        }
    }

}
