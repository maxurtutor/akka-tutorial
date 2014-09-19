package org.maxur.akkacluster;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.PoisonPill;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.routing.FromConfig;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import static akka.actor.ActorRef.noSender;
import static java.lang.String.format;

/**
 * @author Maxim Yunusov
 * @version 1.0 14.09.2014
 */
public class Worker extends UntypedActor {

    private LoggingAdapter logger = Logging.getLogger(context().system(), this);

    private int count = 0;

    private ActorRef router;

    public static void main(String[] args) throws Exception {
        final Config config = ConfigFactory.load().getConfig("worker");
        ActorSystem system = ActorSystem.create("WorkerSystem", config);
        system.actorOf(Props.create(Worker.class), "worker");
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof String) {
            final String response = format("%d: %s", count++, message);
            router.tell(response, sender());
        }
    }

    @Override
    public void preStart() throws Exception {
        logger.info("Start Worker");
        router = context().actorOf(FromConfig.getInstance().props(), "router");
    }

    @Override
    public void postStop() throws Exception {
        router.tell(PoisonPill.getInstance(), noSender());
        logger.info("Stop Worker");
    }

}
