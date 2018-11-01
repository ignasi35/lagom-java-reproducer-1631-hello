# CoordinatedShutdown override sandbox

This is pet project to try to reproduce and (eventually) fix https://github.com/lagom/lagom/issues/1631.

```
sbt stage
```

then 

```
hello-impl/target/universal/stage/bin/hello-impl
```

and finally

```
curl http://127.0.0.1:9000/api/hello/alice
```

The response to the `curl` command is the value of `play.akka.run-cs-from-phase` in `application.conf