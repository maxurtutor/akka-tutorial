package org.maxur.akkacluster;


import org.perf4j.log4j.Log4JStopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Maxim Yunusov
 * @version 1.0 14.09.2014
 */
public class Client implements Runnable {

    Logger logger = LoggerFactory.getLogger(Client.class);


    private Worker worker;

    public static void main(String[] args) throws Exception {
        final Client client = new Client();
        client.init();
        new Thread(client).start();
        new Thread(client).start();
        client.done();
    }

    private void init() {
        worker = new Worker();
    }

    public void run() {
        try {
            for (int i = 0; i < 100; i++) {
                Log4JStopWatch stopWatch = new Log4JStopWatch("Client");

                String response = worker.run(makeRequest());
                System.out.println(response);

                stopWatch.stop();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

    }

    private String makeRequest() {
        return "Hello";
    }

    private void done() {
        worker.done();
    }


}
