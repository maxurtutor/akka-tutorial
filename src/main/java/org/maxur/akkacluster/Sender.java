package org.maxur.akkacluster;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import static akka.actor.ActorRef.noSender;

/**
 * @author Maxim Yunusov
 * @version 1.0 14.09.2014
 */
public class Sender extends UntypedActor {

    private LoggingAdapter logger = Logging.getLogger(context().system(), this);

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof String) {
            send(message.toString());
            sender().tell(message, noSender());
        }
    }

    @Override
    public void preStart() throws Exception {
        logger.info("Start Sender");
    }

    @Override
    public void postStop() throws Exception {
        logger.info("Stop Sender");
    }

    private  void send(String response) throws InterruptedException {
        // Имитация бурной деятельности
        Thread.sleep(100);
    }
}
