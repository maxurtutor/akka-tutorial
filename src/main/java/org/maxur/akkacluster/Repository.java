package org.maxur.akkacluster;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

/**
 * @author Maxim Yunusov
 * @version 1.0 14.09.2014
 */
public class Repository extends UntypedActor {

    private LoggingAdapter logger = Logging.getLogger(context().system(), this);

    private ActorRef sender;

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof String) {
            save(message.toString());
            sender.tell(message, sender());
        }
    }

    @Override
    public void preStart() throws Exception {
        logger.info("Start Repository");
        sender = context().actorOf(Props.create(Sender.class));
    }

    @Override
    public void postStop() throws Exception {
        logger.info("Stop Repository");
    }

    private void save(String response) throws InterruptedException {
        // Имитация бурной деятельности
        Thread.sleep(100);
    }
}
