package org.maxur.akkacluster;

/**
 * @author Maxim Yunusov
 * @version 1.0 14.09.2014
 */
public class Repository {

    public void save(String response) throws InterruptedException {
        // Имитация бурной деятельности
        Thread.sleep(100);
    }

    public void done() {
        // TODO
    }
}
