package org.maxur.akkacluster;

import static java.lang.String.format;

/**
 * @author Maxim Yunusov
 * @version 1.0 14.09.2014
 */
public class Worker {

    private int count = 0;

    private Sender sender;

    private Repository repository;

    public Worker() {
        init();
    }

    public void init() {
        sender = new Sender();
        repository = new Repository();
    }

    public String run(String request) throws Exception {
        final String message = format("%d: %s", count++, request);
        repository.save(message);
        sender.send(message);
        return message;
    }

    public void done() {
        sender.done();
        repository.done();
    }
}
