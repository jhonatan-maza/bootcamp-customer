package com.nttdata.bootcamp.controller;

import com.nttdata.bootcamp.entity.enums.Status;
import com.nttdata.bootcamp.entity.serverResponse.Error;
import com.nttdata.bootcamp.entity.serverResponse.ServerResponse;
import com.nttdata.bootcamp.service.CustomerService;
import com.nttdata.bootcamp.util.Constant;
import io.netty.handler.codec.http.HttpResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.nttdata.bootcamp.entity.Customer;

import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

//@Slf4j
@RestController
@RequestMapping(value = "/customer")
public class CustomerController {
	
	//private static Logger LOGGER = Logger.getLogger(CustomerController.class);
	@Autowired
	private CustomerService customerService;

	@GetMapping("/abc")
	public Flux<Customer> findAllClienta() {
		//cambio app
		return customerService.findAll();
	}

	@GetMapping("/")
	public Flux<ResponseEntity<ServerResponse>> findAllClient() {
		return customerService.findAll()
				.map(
						customer -> {
							return new ResponseEntity<>(
									new ServerResponse(Status.OK, null, customer), HttpStatus.OK);
						}
				)
				.defaultIfEmpty(
						ResponseEntity.status(HttpStatus.OK)
								.body(new ServerResponse(Status.ERROR, new Error(null, Constant.NO_RECORDS), null))
				)
				.onErrorResume(
						error -> {
							return Mono.just(new ResponseEntity<>(
									new ServerResponse(Status.ERROR, new Error(null, Constant.QUERY_FAILED), error.getMessage()),
									HttpStatus.BAD_REQUEST));
						}
				);
	}

	@GetMapping("/findByClient/{dni}")
	public Mono<ResponseEntity<ServerResponse>> findByClient(@PathVariable("dni") String dni) {
		return customerService.findByDni(dni)
				.map(
						customer -> {
							return new ResponseEntity<>(
									new ServerResponse(Status.OK, null, customer), HttpStatus.OK);
						}
				)
				.defaultIfEmpty(
						ResponseEntity.status(HttpStatus.OK)
								.body(new ServerResponse(Status.ERROR, new Error(null, Constant.CLIENT_NOT_EXIST), null))
				)
				.onErrorResume(
						error -> {
							return Mono.just(new ResponseEntity<>(
									new ServerResponse(Status.ERROR, new Error(null, Constant.QUERY_FAILED), error.getMessage()),
									HttpStatus.BAD_REQUEST));
						}
				);
	}

	@PostMapping(value = "/save")
	public Mono<ResponseEntity<ServerResponse>> saveCustomer(@RequestBody Customer dataCustomer){
		return customerService.save(dataCustomer)
				.map(
						customer -> {
							return new ResponseEntity<>(
									new ServerResponse(Status.ERROR, new Error(null, Constant.CLIENT_EXIST), null), HttpStatus.OK);
						}
				)
				.defaultIfEmpty(
						ResponseEntity.status(HttpStatus.OK)
								.body(new ServerResponse(Status.ERROR, new Error(null, Constant.CLIENT_NOT_EXIST), null))
				)
				.onErrorResume(
						error -> {
							return Mono.just(new ResponseEntity<>(
									new ServerResponse(Status.ERROR, new Error(null, Constant.QUERY_FAILED), error.getMessage()),
									HttpStatus.BAD_REQUEST));
						}
				);
	}

	@PutMapping(value = "/update/{dni}/{status}")
	public Mono<ResponseEntity<ServerResponse>> updateCustomer(@PathVariable("dni") String dni, @PathVariable("status") String status){
		return customerService.update(dni, status)
				.map(
						customer -> {
							return new ResponseEntity<>(
									new ServerResponse(Status.OK, null, customer), HttpStatus.OK);
						}
				)
				.defaultIfEmpty(
						ResponseEntity.status(HttpStatus.OK)
								.body(new ServerResponse(Status.ERROR, new Error(null, Constant.CLIENT_NOT_EXIST), null))
				)
				.onErrorResume(
						error -> {
							return Mono.just(new ResponseEntity<>(
									new ServerResponse(Status.ERROR, new Error(null, Constant.QUERY_FAILED), error.getMessage()),
									HttpStatus.BAD_REQUEST));
						}
				);
	}

}
