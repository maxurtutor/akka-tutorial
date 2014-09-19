package org.maxur.akkacluster;


import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.PoisonPill;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import org.perf4j.log4j.Log4JStopWatch;

import static akka.actor.ActorRef.noSender;

/**
 * @author Maxim Yunusov
 * @version 1.0 14.09.2014
 */
public class Client extends UntypedActor {

    private LoggingAdapter logger = Logging.getLogger(context().system(), this);

    private ActorRef worker;

    public static void main(String[] args) throws Exception {
        startSystem();
    }

    private static void startSystem() {
        ActorSystem system = ActorSystem.create("ClientSystem");
        system.actorOf(Props.create(Client.class));
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof String) {
            Log4JStopWatch stopWatch = new Log4JStopWatch("Client");
            System.out.println(message);
            stopWatch.stop();
        }
    }

    @Override
    public void preStart() {
        logger.info("Start Client");
        worker = context().system().actorOf(Props.create(Worker.class), "worker");
        run();
    }

    @Override
    public void postStop() {
        worker.tell(PoisonPill.getInstance(), noSender());
        context().system().shutdown();
        logger.info("Stop Client");
    }

    private void run() {
        try {
            for (int i = 0; i < 100; i++) {
                worker.tell(makeRequest(), self());
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    private String makeRequest() {
        return "Hello";
    }
}
