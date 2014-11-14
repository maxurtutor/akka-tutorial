package org.maxur.akkacluster;


import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.perf4j.log4j.Log4JStopWatch;

/**
 * @author Maxim Yunusov
 * @version 1.0 14.09.2014
 */
public class Client extends AbstractClient {

    private ActorSelection worker;
    public static final String PATH = "akka.tcp://ClusterSystem@127.0.0.1:2550/user/worker";

    public static void main(String[] args) throws Exception {
        startSystem();
    }

    private static void startSystem() {
        final Config config = ConfigFactory.load().getConfig("client");
        ActorSystem system = ActorSystem.create("ClientSystem", config);
        system.actorOf(Props.create(Client.class));
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof Start) {
            worker.tell(makeRequest(), self());
        }
        if (message instanceof String) {
            Log4JStopWatch stopWatch = new Log4JStopWatch("Client");
            System.out.println(message);
            stopWatch.stop();
        }
    }

    @Override
    public void init(ActorSystem system) {
        worker = system.actorSelection(PATH);

    }

}
