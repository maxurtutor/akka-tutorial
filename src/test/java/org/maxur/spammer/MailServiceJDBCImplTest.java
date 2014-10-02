package org.maxur.spammer;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;

import static org.junit.Assert.assertEquals;

/**
 * @author Maxim Yunusov
 * @version 1.0
 * @since <pre>2/28/14</pre>
 */
public class MailServiceJDBCImplTest extends AbstractDAOJDBCTest {

    private MailIDService service;

    @Before
    public void initTest() throws Exception {
        service = new MailIDServiceJDBCImpl(dataSource);

        IDataSet dataSet = new FlatXmlDataSetBuilder().build(
                MailServiceJDBCImplTest.class.getResourceAsStream("/sql/dataset.xml"));

        try(final Connection connection = getConnection()) {
            IDatabaseConnection databaseConnection = new DatabaseConnection(connection);
            DatabaseOperation.CLEAN_INSERT.execute(databaseConnection, dataSet);
        }
    }

    @Test
    public void testGetId() {
        final Long id = service.getId();
        assertEquals(2L, id.longValue());
    }


    @After
    public void after() {
    }

}