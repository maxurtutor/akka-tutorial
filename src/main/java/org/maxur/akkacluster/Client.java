package org.maxur.akkacluster;

import org.perf4j.log4j.Log4JStopWatch;

/**
 * @author Maxim Yunusov
 * @version 1.0 07.09.2014
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

    private void run() throws InterruptedException {
        for (int i = 0; i < 100; i++) {
            final Log4JStopWatch stopWatch = new Log4JStopWatch("Client");
            // Делаем что-то полезное
            final String source = doIt();
            final String target = worker.run(source);
            System.out.println(target);
            stopWatch.stop();
        }
    }

    private String doIt() throws InterruptedException {
        Thread.sleep(100);
        return "A";
    }

    private void done() {
        worker.done();
    }


}
