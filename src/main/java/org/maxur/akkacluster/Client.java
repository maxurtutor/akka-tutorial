package org.maxur.akkacluster;


import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.PoisonPill;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.perf4j.log4j.Log4JStopWatch;
import scala.concurrent.duration.Duration;

import static akka.actor.ActorRef.noSender;
import static scala.concurrent.duration.Duration.Zero;

/**
 * @author Maxim Yunusov
 * @version 1.0 14.09.2014
 */
public class Client extends UntypedActor {

    private LoggingAdapter logger = Logging.getLogger(context().system(), this);

    private ActorSelection worker;

    public static void main(String[] args) throws Exception {
        startSystem();
    }

    private static void startSystem() {
        final Config config = ConfigFactory.load().getConfig("client");
        ActorSystem system = ActorSystem.create("ClientSystem", config);
        system.actorOf(Props.create(Client.class));
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof Start) {
            worker.tell(makeRequest(), self());
        }
        if (message instanceof String) {
            Log4JStopWatch stopWatch = new Log4JStopWatch("Client");
            System.out.println(message);
            stopWatch.stop();
        }
    }

    @Override
    public void preStart() {
        logger.info("Start Client");
        final ActorSystem system = context().system();
        system.scheduler().schedule(
                Zero(),
                Duration.apply(100, "millisecond"),
                self(),
                new Start(),
                system.dispatcher(),
                noSender()
        );
        final String path = "akka.tcp://ClusterSystem@127.0.0.1:2550/user/worker";
        worker = system.actorSelection(path);
    }

    @Override
    public void postStop() {
        worker.tell(PoisonPill.getInstance(), noSender());
        context().system().shutdown();
        logger.info("Stop Client");
    }


    private String makeRequest() {
        return "Hello";
    }

    private static class Start {
    }

}
