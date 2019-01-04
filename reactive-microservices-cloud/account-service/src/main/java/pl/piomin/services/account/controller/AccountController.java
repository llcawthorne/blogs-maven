package pl.piomin.services.account.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import pl.piomin.services.account.model.Account;
import pl.piomin.services.account.repository.AccountRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class AccountController {

	private static final Logger logger = LoggerFactory.getLogger(AccountController.class);
	
	@Autowired
	private AccountRepository repository;

	@GetMapping("/customer/{customerId}")
	public Flux<Account> findByCustomer(@PathVariable("customerId") String customerId) {
		logger.info("findByCustomer: customerId={}", customerId);
		return repository.findByCustomerId(customerId);
	}

	@GetMapping
	public Flux<Account> findAll() {
		logger.info("findAll");
		return repository.findAll();
	}

	@GetMapping("/{id}")
	public Mono<Account> findById(@PathVariable("id") String id) {
		logger.info("findById: id={}", id);
		return repository.findById(id);
	}

	@PostMapping
	public Mono<Account> create(@RequestBody Account account) {
		logger.info("create: {}", account);
		return repository.save(account);
	}
	
}