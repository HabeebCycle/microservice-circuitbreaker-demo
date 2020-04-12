package com.habeebcycle.springcircuitbreaker.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Optional;

@Service
@Log4j2
public class GreetingService {
    public Mono<String> greet(Optional<String> name){
        long duration = (long)(Math.random() * 10);
        return name
                .map(str -> {
                    String msg = "Hello " + str + "!  (" + duration + " seconds)";
                    log.info(msg);
                    return Mono.just(msg);
                })
                .orElse(Mono.error(new NullPointerException()))
                .delayElement(Duration.ofSeconds(duration));
    }
}
