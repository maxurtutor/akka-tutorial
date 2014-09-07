package org.maxur.akkacluster;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.routing.FromConfig;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class FrontendActor extends UntypedActor {

    private ActorRef router;

    public static void main(String[] args) throws Exception {
        if (args.length == 0)
            startup(new String[]{"2561", "2562"});
        else
            startup(args);
    }

    private static void startup(String[] ports) {
        for (String port : ports) {
            final Config config = ConfigFactory
                    .parseString("akka.remote.netty.tcp.port=" + port)
                    .withFallback(ConfigFactory.load().getConfig("worker"));

            ActorSystem system = ActorSystem.create("WorkerSystem", config);
            system.actorOf(Props.create(WorkerActor.class, port), "worker");
            system.actorOf(Props.create(FrontendActor.class), "frontend");
        }
    }

    @Override
    public void onReceive(final Object message) throws Exception {
        if (message instanceof String) {
            router.tell(message, sender());
        }
    }

    @Override
    public void preStart() throws Exception {
        router = context().actorOf(FromConfig.getInstance().props(), "router");
    }

    @Override
    public void postStop() throws Exception {
    }
}