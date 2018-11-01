package com.example.hello.impl;

import akka.NotUsed;
import com.example.hello.api.HelloService;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.typesafe.config.Config;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;

/**
 * Implementation of the HelloService.
 */
public class HelloServiceImpl implements HelloService {
    private Config config;

    @Inject
    public HelloServiceImpl(Config config) {
        this.config = config;
    }

    @Override
    public ServiceCall<NotUsed, String> hello(String id) {
        return request -> {
            return CompletableFuture.completedFuture("Hello " + id+ " - "+ config.getString("play.akka.run-cs-from-phase")) ;
        };
    }

}
