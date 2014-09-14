package org.maxur.akkacluster;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import static java.lang.String.format;

/**
 * @author Maxim Yunusov
 * @version 1.0 14.09.2014
 */
public class Repository extends UntypedActor {

    private LoggingAdapter logger = Logging.getLogger(context().system(), this);

    private final String port;

    private ActorRef sender;

    public static void main(String[] args) throws Exception {
        final String[] ports = args.length == 0 ?
                new String[]{"2561", "2562"} :
                args;
        startup(ports);
    }


    private static void startup(String[] ports) {
        for (String port : ports) {
            final Config config = ConfigFactory
                    .parseString("akka.remote.netty.tcp.port=" + port)
                    .withFallback(ConfigFactory.load().getConfig("repository"));

            ActorSystem system = ActorSystem.create("RepositorySystem", config);
            system.actorOf(Props.create(Repository.class, port), "repository");
        }
    }

    public Repository(String port) {
        this.port = port;
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof String) {
            save(message.toString());
            sender.tell(format("%s (%s)", message, port), sender());
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
