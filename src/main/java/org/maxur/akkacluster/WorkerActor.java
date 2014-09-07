package org.maxur.akkacluster;

import akka.actor.UntypedActor;

import static akka.actor.ActorRef.noSender;
import static java.lang.String.format;
import static java.lang.Thread.sleep;

/**
 * @author Maxim Yunusov
 * @version 1.0 07.09.2014
 */
public class WorkerActor extends UntypedActor {

    private int count = 0;

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof String) {
            final String source = (String) message;
            sender().tell(doIt(source), noSender());
        }
    }

    @Override
    public void preStart() throws Exception {}

    @Override
    public void postStop() throws Exception {
    }

    private String doIt(String source) throws InterruptedException {
        sleep(100);
        return format("%s:%d", source, count++);
    }

}
