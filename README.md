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

### Checking CoordinatedShutdown runs

To confirm `CoordinatedShutdown` is run this sample app enables `DEBUG` level logs for `akka.*`. Follow the steps above 
to run the `hello-impl` service in `PROD` mode. Once running, use `jps` to obtain the PID:

```
$ jps
85345 Launcher
85877 ProdServerStart
81723 
86076 Jps
```

In the example above we used `jps` and it listed our Lagom service under the PID `85877`. Note: `ProdServerStart` is 
Play's `mainClass` in production mode.

Finally, send a `SIGTERM` to your lagom service to force the JVM shutdown which will, in turn, run CoordinatedShutdown.

To confirm CoordinatedShutdown actually ran, review the logs and search for traces like:

```
...
[debug] a.i.TcpListener - Unbound endpoint /0:0:0:0:0:0:0:0:9000, stopping listener
[debug] a.a.CoordinatedShutdown - Performing phase [before-cluster-shutdown] with [0] tasks
[debug] a.a.CoordinatedShutdown - Performing phase [cluster-sharding-shutdown-region] with [0] tasks
[debug] a.a.CoordinatedShutdown - Performing phase [cluster-leave] with [1] tasks: [leave]
[debug] a.a.CoordinatedShutdown - Performing phase [cluster-exiting] with [1] tasks: [wait-exiting]
[debug] a.a.CoordinatedShutdown - Performing phase [cluster-exiting-done] with [1] tasks: [exiting-completed]
[info] a.c.Cluster(akka://application) - Cluster Node [akka.tcp://application@192.168.1.133:2552] - Exiting completed
[info] a.c.Cluster(akka://application) - Cluster Node [akka.tcp://application@192.168.1.133:2552] - Shutting down...
[info] a.c.Cluster(akka://application) - Cluster Node [akka.tcp://application@192.168.1.133:2552] - Successfully shut down
[debug] a.a.CoordinatedShutdown - Performing phase [cluster-shutdown] with [2] tasks: [wait-shutdown, exit-jvm-when-downed]
[debug] a.a.CoordinatedShutdown - Performing phase [before-actor-system-terminate] with [0] tasks
[debug] a.a.CoordinatedShutdown - Performing phase [actor-system-terminate] with [1] tasks: [terminate-system]
[info] a.r.RemoteActorRefProvider$RemotingTerminator - Shutting down remote daemon.
[info] a.r.RemoteActorRefProvider$RemotingTerminator - Remote daemon shut down; proceeding with flushing remote transports.
[info] a.r.Remoting - Remoting shut down
[info] a.r.RemoteActorRefProvider$RemotingTerminator - Remoting shut down.
[debug] a.e.EventStream - shutting down: StandardOutLogger
```

See how `a.a.CoordinatedShutdown - Performing phase` is printed several times (once for each phase run).