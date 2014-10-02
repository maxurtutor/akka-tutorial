package org.maxur.spammer;

import org.apache.derby.tools.ij;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.Connection;
import java.sql.SQLException;
/**
 * @author Maxim Yunusov
 * @version 1.0
 * @since <pre>2/28/14</pre>
 */
public class AbstractDAOJDBCTest {

    protected static DriverManagerDataSource dataSource;

    @BeforeClass
    public static void initTestFixture() throws Exception {
// Initialize the datasource, could /should be done of Spring
// configuration
        dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.apache.derby.jdbc.EmbeddedDriver");
        dataSource.setUrl("jdbc:derby:c:\\temp\\database\\test01;create=true");
        dataSource.setUsername("");
        dataSource.setPassword("");


        ij.runScript(getConnection(),
                AbstractDAOJDBCTest.class.getResourceAsStream("/sql/schema.ddl"),
                "UTF-8",
                System.out,
                "UTF-8"
        );
    }

    protected static Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Cleans up the session.
     */
    @AfterClass
    public static void closeTestFixture() {
    }


}