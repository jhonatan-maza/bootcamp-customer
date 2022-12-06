package com.nttdata.bootcamp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.nttdata.bootcamp.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.nttdata.bootcamp.entity.Customer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.Date;
import javax.validation.Valid;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/customer")
public class CustomerController {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerController.class);
	@Autowired
	private CustomerService customerService;

	//Customer search
	@GetMapping("/")
	public Flux<Customer> findAllCustomers() {
		Flux<Customer> customers = customerService.findAll();
		LOGGER.info("Registered Customers: " + customers);
		return customers;
	}

	//Search for clients by DNI
	@GetMapping("/findByClient/{dni}")
	public Mono<Customer> findByClientDNI(@PathVariable("dni") String dni) {
		LOGGER.info("Searching client by DNI: " + dni);
		return customerService.findByDni(dni);
	}

	//Save customer
	@PostMapping(value = "/save")
	public Mono<Customer> saveCustomer(@RequestBody Customer dataCustomer){
		Mono.just(dataCustomer).doOnNext(t -> {

					t.setCreationDate(new Date());
					t.setModificationDate(new Date());

				}).onErrorReturn(dataCustomer).onErrorResume(e -> Mono.just(dataCustomer))
				.onErrorMap(f -> new InterruptedException(f.getMessage())).subscribe(x -> LOGGER.info(x.toString()));

		Mono<Customer> newCustomer = customerService.save(dataCustomer);
		return newCustomer;
	}

	//Update customer
	@PutMapping("/update/{dni}")
	public Mono<Customer> updateCustomer(@PathVariable("dni") String dni,
													   @Valid @RequestBody Customer dataCustomer) {

		Mono.just(dataCustomer).doOnNext(t -> {

					t.setDni(dni);
					t.setModificationDate(new Date());

				}).onErrorReturn(dataCustomer).onErrorResume(e -> Mono.just(dataCustomer))
				.onErrorMap(f -> new InterruptedException(f.getMessage())).subscribe(x -> LOGGER.info(x.toString()));

		Mono<Customer> updateCustomer = customerService.update(dataCustomer);
		return updateCustomer;
	}

	//Delete customer
	@DeleteMapping("/delete/{dni}")
	public Mono<Void> deleteCustomer(@PathVariable("dni") String dni) {
		LOGGER.info("Deleting client by DNI: " + dni);
		Mono<Void> deleteCustomer = customerService.delete(dni);
		return deleteCustomer;
	}

}
