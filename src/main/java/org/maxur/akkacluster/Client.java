package org.maxur.akkacluster;


import org.perf4j.log4j.Log4JStopWatch;

/**
 * @author Maxim Yunusov
 * @version 1.0 14.09.2014
 */
public class Client {

    private Worker worker;

    public static void main(String[] args) throws Exception {
        final Client client = new Client();
        client.init();
        client.run();
        client.done();
    }

    private void init() {
        worker = new Worker();
    }

    private void run() throws Exception {
        for (int i = 0; i < 100; i++) {
            Log4JStopWatch stopWatch = new Log4JStopWatch("Client");

            String response = worker.run(makeRequest());
            System.out.println(response);

            stopWatch.stop();
        }
    }

    private String makeRequest() {
        return "Hello";
    }

    private void done() {
        worker.done();
    }


}
