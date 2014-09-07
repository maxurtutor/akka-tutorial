package org.maxur.akkacluster;

import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.perf4j.log4j.Log4JStopWatch;

/**
 * @author Maxim Yunusov
 * @version 1.0 07.09.2014
 */
public class ClientActor extends UntypedActor {

    private static ActorSystem system;

    public static void main(String[] args) throws Exception {
        final Config config = ConfigFactory.load().getConfig("client");
        system = ActorSystem.create("ClientSystem", config);
        system.actorOf(Props.create(ClientActor.class));
    }

    @Override
    public void preStart() throws Exception {
        final String path = "akka.tcp://WorkerSystem@127.0.0.1:2552/user/worker";
        final ActorSelection worker = system.actorSelection(path);
        for (int i = 0; i < 100; i++) {
            // Делаем что-то полезное
            final String source = doIt();
            worker.tell(source, self());
        }
    }

    @Override
    public void onReceive(Object message) throws Exception {
        final Log4JStopWatch stopWatch = new Log4JStopWatch("Client");
        if (message instanceof String) {
            System.out.println(message);
        }
        stopWatch.stop();
    }

    @Override
    public void postStop() throws Exception {
        system.shutdown();
    }

    private String doIt() throws InterruptedException {
        Thread.sleep(100);
        return "A";
    }


}
