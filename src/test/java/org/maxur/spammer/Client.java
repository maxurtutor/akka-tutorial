package org.maxur.spammer;

import com.dumbster.smtp.SimpleSmtpServer;
import org.perf4j.log4j.Log4JStopWatch;

/**
 * @author Maxim Yunusov
 * @version 1.0 14.09.2014
 */
public class Client {

    private Worker worker;
    private SimpleSmtpServer server;

    public static void main(String[] args) throws Exception {
        final Client client = new Client();
        client.init();
        client.run();
        client.done();
    }

    private void init() {

        server = SimpleSmtpServer.start();
        worker = new Worker();
    }

    private void run() throws Exception {
        for (int i = 0; i < 20000; i++) {
            Log4JStopWatch stopWatch = new Log4JStopWatch("end-to-end");

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
        server.stop();
    }


}
