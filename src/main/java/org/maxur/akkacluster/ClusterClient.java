package org.maxur.akkacluster;


import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.contrib.pattern.ClusterClient.Send;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.perf4j.log4j.Log4JStopWatch;
import scala.concurrent.duration.FiniteDuration;

import java.util.HashSet;
import java.util.Set;

import static akka.contrib.pattern.ClusterClient.props;

/**
 * @author Maxim Yunusov
 * @version 1.0 14.09.2014
 */
public class ClusterClient extends AbstractClient {

    public static final String PATH = "akka.tcp://ClusterSystem@127.0.0.1:2550/user/receptionist";

    private ActorRef receptionist;


    public static void main(String[] args) throws Exception {
        startSystem();
    }

    private static void startSystem() {
        final Config config = ConfigFactory.load().getConfig("clusterClient");
        ActorSystem system = ActorSystem.create("ClientSystem", config);
        system.actorOf(Props.create(ClusterClient.class));
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof Start) {
            final Send send = new Send("/user/worker", makeRequest(), true);
            receptionist.tell(send, self());
        }
        if (message instanceof String) {
            Log4JStopWatch stopWatch = new Log4JStopWatch("Client");
            System.out.println(message);
            stopWatch.stop();
        }
    }

    @Override
    public void init(ActorSystem system) {
        receptionist = makeReceptionist(PATH);
    }

    private ActorRef makeReceptionist(final String path) {
        final Set<ActorSelection> initialContacts = new HashSet<>();
        initialContacts.add(context().system().actorSelection(path));
        FiniteDuration establishingGetContactsInterval = FiniteDuration.apply(10, "second");
        FiniteDuration refreshContactsInterval = FiniteDuration.apply(10, "second");
        return context().system().actorOf(
            props(initialContacts, establishingGetContactsInterval, refreshContactsInterval)
        );
    }


}
