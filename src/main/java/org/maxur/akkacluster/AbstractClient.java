package org.maxur.akkacluster;

import akka.actor.ActorSystem;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import scala.concurrent.duration.Duration;

import static akka.actor.ActorRef.noSender;

/**
 * @author Maxim Yunusov
 * @version 1.0
 * @since <pre>11/14/2014</pre>
 */
public abstract class AbstractClient extends UntypedActor {

    protected LoggingAdapter logger = Logging.getLogger(context().system(), this);

    private Integer count = 0;

    protected String makeRequest() {
        return "[" + count++ + "]";
    }

    @Override
    public void preStart() {
        final ActorSystem system = context().system();
        system.scheduler().schedule(
                Duration.apply(10000, "millisecond"),
                Duration.apply(1000, "millisecond"),
                self(),
                new Start(),
                system.dispatcher(),
                noSender()
        );
        init(system);
        logger.info("Start Client");
    }

    protected abstract void init(ActorSystem system);

    @Override
    public void postStop() {
        context().system().shutdown();
        logger.info("Stop Client");
    }

    protected static class Start {
    }

}
