package org.maxur.akkacluster;

import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import static akka.actor.ActorRef.noSender;
import static java.lang.String.format;
import static java.lang.Thread.sleep;

/**
 * @author Maxim Yunusov
 * @version 1.0 07.09.2014
 */
public class WorkerActor extends UntypedActor {

    private static ActorSystem system;

    private int count = 0;

    public static void main(String[] args) throws Exception {
        if (args.length == 0)
            startup(new String[]{"2561"});
        else
            startup(args);
    }

    private static void startup(String[] ports) {
        for (String port : ports) {
            final Config config = ConfigFactory
                    .parseString("akka.remote.netty.tcp.port=" + port)
                    .withFallback(ConfigFactory.load().getConfig("worker"));

            ActorSystem system = ActorSystem.create("WorkerSystem", config);
            system.actorOf(Props.create(WorkerActor.class), "worker");
        }
    }

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
        system.shutdown();
    }

    private String doIt(String source) throws InterruptedException {
        sleep(100);
        return format("%s:%d", source, count++);
    }

}
