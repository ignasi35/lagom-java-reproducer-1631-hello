package com.example.hello.impl;

import com.example.hello.api.HelloService;
import com.google.inject.AbstractModule;
import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.ServiceLocator;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;
import com.typesafe.config.Config;
import play.Environment;

import java.net.URI;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

/**
 * The module that binds the HelloService so that it can be served.
 */
public class HelloModule extends AbstractModule implements ServiceGuiceSupport {


    private final Environment environment;
    private final Config config;

    public HelloModule(Environment environment, Config config) {
        this.environment = environment;
        this.config = config;
    }


    @Override
    protected void configure() {

        bindService(HelloService.class, HelloServiceImpl.class);
        if (environment.isProd()) {
            bind(ServiceLocator.class).to(NoServiceLocator.class);
        }

    }

    static class NoServiceLocator implements ServiceLocator {

        @Override
        public CompletionStage<Optional<URI>> locate(String name, Descriptor.Call<?, ?> serviceCall) {
            return CompletableFuture.completedFuture(Optional.empty());
        }

        @Override
        public <T> CompletionStage<Optional<T>> doWithService(String name, Descriptor.Call<?, ?> serviceCall, Function<URI, CompletionStage<T>> block) {
            return CompletableFuture.completedFuture(Optional.empty());
        }
    }


}
