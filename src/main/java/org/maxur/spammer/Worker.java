package org.maxur.spammer;

import org.perf4j.log4j.Log4JStopWatch;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import static java.lang.String.format;

/**
 * @author Maxim Yunusov
 * @version 1.0 14.09.2014
 */
public class Worker {

    public static final String FROM_ADDRESS = "sender@here.com";

    public static final String TO_ADDRESS = "receiver@there.com";

    public static final String USERNAME = "";

    public static final String PASSWORD = "";

    public static final String DB_PATH = "c:\\temp\\database\\test01";

    private long count = 0;

    private MailService mailService;

    private Repository<Mail> repository;


    public Worker() {
        init();
    }

    public void init() {

        mailService = new MailServiceJavaxImpl(FROM_ADDRESS);
        final DriverManagerDataSource dataSource = makeDataSource();
        repository = new MailRepositoryJDBCImpl(dataSource);
        MailIDService service = new MailIDServiceJDBCImpl(dataSource);
        count = service.getId();
    }

    public String run(String request) throws Exception {
        final String message = format("%d: %s", count++, request);


        final Mail mail = Mail.builder()
                .id(count)
                .subject(message)
                .body(message)
                .toAddress(TO_ADDRESS)
                .build();

        Log4JStopWatch watch1 = new Log4JStopWatch("save");
        repository.save(mail);
        watch1.stop();
        Log4JStopWatch watch2 = new Log4JStopWatch("send");
        mailService.send(mail);
        watch2.stop();
        return message;
    }

    public void done() {
        mailService.done();
        repository.done();
    }

    private DriverManagerDataSource makeDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.apache.derby.jdbc.EmbeddedDriver");
        dataSource.setUrl(format("jdbc:derby:%s;create=true", DB_PATH));
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
        return dataSource;
    }

}
