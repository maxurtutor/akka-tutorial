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

    private final String port;

    public static void main(String[] args) throws Exception {
        if (args.length == 0)
            startup(new String[]{"2551", "2552"});
        else
            startup(args);
    }

    private static void startup(String[] ports) {
        for (String port : ports) {
            final Config config = ConfigFactory
                    .parseString("akka.remote.netty.tcp.port=" + port)
                    .withFallback(ConfigFactory.load().getConfig("client"));

            ActorSystem system = ActorSystem.create("ClientSystem", config);
            system.actorOf(Props.create(ClientActor.class, port));
        }
    }

    public ClientActor(String port) {
        this.port = port;
    }

    @Override
    public void preStart() throws Exception {
        final String path = "akka.tcp://WorkerSystem@127.0.0.1:2561/user/worker";
        final ActorSelection worker = context().system().actorSelection(path);
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
        context().system().shutdown();
    }

    private String doIt() throws InterruptedException {
        Thread.sleep(100);
        return port;
    }


}
