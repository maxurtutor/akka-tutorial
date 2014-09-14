package org.maxur.akkacluster;

import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import static akka.actor.ActorRef.noSender;
import static java.lang.String.format;

/**
 * @author Maxim Yunusov
 * @version 1.0 14.09.2014
 */
public class Worker extends UntypedActor {

    private LoggingAdapter logger = Logging.getLogger(context().system(), this);

    private int count = 0;

    private Sender sender;

    private Repository repository;

    public static void main(String[] args) throws Exception {
        ActorSystem system = ActorSystem.create("WorkerSystem");
        system.actorOf(Props.create(Worker.class));
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof String) {
            final String response = format("%d: %s", count++, message);
            repository.save(response);
            sender.send(response);
            sender().tell(response, noSender());
        }
    }

    @Override
    public void preStart() throws Exception {
        logger.info("Start Worker");
        sender = new Sender();
        repository = new Repository();
    }

    @Override
    public void postStop() throws Exception {
        sender.done();
        repository.done();
        logger.info("Stop Worker");
    }

}
