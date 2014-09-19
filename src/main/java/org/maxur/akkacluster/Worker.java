package org.maxur.akkacluster;

import static java.lang.String.format;

/**
 * @author Maxim Yunusov
 * @version 1.0 14.09.2014
 */
public class Worker {

    private int count = 0;

    private MailService mailService;

    private Repository repository;

    public Worker() {
        init();
    }

    public void init() {
        mailService = new MailService();
        repository = new Repository();
    }

    public synchronized String run(String request) throws Exception {
        final String message = format("%d: %s", count++, request);
        repository.save(message);
        mailService.send(message);
        return message;
    }

    public void done() {
        mailService.done();
        repository.done();
    }
}
