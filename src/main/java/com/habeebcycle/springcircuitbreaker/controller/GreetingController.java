package com.habeebcycle.springcircuitbreaker.controller;

import com.habeebcycle.springcircuitbreaker.service.GreetingService;
import lombok.extern.log4j.Log4j2;
import org.reactivestreams.Publisher;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Optional;

@RestController
public class GreetingController {
    private final GreetingService greetingService;
    private final ReactiveCircuitBreaker reactiveCircuitBreaker;

    public GreetingController(GreetingService greetingService, ReactiveCircuitBreakerFactory rcbf) {
        this.greetingService = greetingService;
        this.reactiveCircuitBreaker = rcbf.create("greet");
    }

    @GetMapping("/greet")
    public Publisher<String> greet(@RequestParam Optional<String> name){
        Mono<String> publisher = this.greetingService.greet(name);
        return this.reactiveCircuitBreaker.run(publisher, throwable -> Mono.just("Hello World!"));
    }
}
