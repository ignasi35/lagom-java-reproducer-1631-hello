package com.example.hello.impl;

import akka.NotUsed;
import akka.actor.ActorSystem;
import com.example.hello.api.HelloService;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.typesafe.config.Config;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * Implementation of the HelloService.
 */
public class HelloServiceImpl implements HelloService {
    private Config config;
    private ActorSystem system;

    @Inject
    public HelloServiceImpl(Config config, ActorSystem system) {
        this.config = config;
        this.system = system;
    }

    @Override
    public ServiceCall<NotUsed, String> hello(String id) {
        return request -> {
            String path = "play.akka.run-cs-from-phase";
            return
                CompletableFuture.completedFuture(
                    "Hello " + id + "\n" +
                        "run-from-phase: " + config.getString(path) + "\n" +
                        config.getValue(path).origin().description() + "\n" +
                        String.join(",", config.getValue(path).origin().comments())

                    )
            ;
        };
    }

}
