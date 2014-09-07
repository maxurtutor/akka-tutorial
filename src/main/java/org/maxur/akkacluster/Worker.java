package org.maxur.akkacluster;

import static java.lang.String.format;
import static java.lang.Thread.sleep;

/**
 * @author Maxim Yunusov
 * @version 1.0 07.09.2014
 */
public class Worker {

    private int count = 0;

    public synchronized String run(String source) throws InterruptedException {
        return doIt(source);
    }

    private String doIt(String source) throws InterruptedException {
        sleep(100);
        return format("%s:%d", source, count++);
    }

    public void done() {
    }


}
