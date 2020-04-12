package com.habeebcycle.springcircuitbreaker.config;

import io.github.resilience4j.reactor.retry.
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.util.retry.Retry;

import java.time.Duration;

@Configuration
public class GreetingCircuitBreakerConfig {

    @Bean
    ReactiveCircuitBreakerFactory reactiveCircuitBreakerFactory(){
        ReactiveResilience4JCircuitBreakerFactory circuitBreakerFactory = new ReactiveResilience4JCircuitBreakerFactory();
        /*
        circuitBreakerFactory.configureDefault(
                s -> new Resilience4JConfigBuilder(s)
                        .timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(5)).build())
                        .circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
                        .build()
        );*/
        // Individual configuration

        circuitBreakerFactory.configureDefault(
                s -> new Resilience4JConfigBuilder(s)
                        //.timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(5)).build())
                        .circuitBreakerConfig(CircuitBreakerConfig.custom()
                                .automaticTransitionFromOpenToHalfOpenEnabled(true)
                                .slidingWindowSize(10)
                                .failureRateThreshold(50.0f)
                                .waitDurationInOpenState(Duration.ofSeconds(5))
                                .slowCallDurationThreshold(Duration.ofMillis(2000))
                                .slowCallRateThreshold(50.0F)
                                .permittedNumberOfCallsInHalfOpenState(5)
                                .minimumNumberOfCalls(5)
                                .build()
                        ).build()
        );

        return circuitBreakerFactory;
    }
}
