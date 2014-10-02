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
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

/**
 * @author Maxim Yunusov
 * @version 1.0
 * @since <pre>2/28/14</pre>
 */
public class MailRepositoryJDBCImplTest extends AbstractDAOJDBCTest {

    private Repository<Mail> repository;

    @Before
    public void initTest() throws Exception {
        repository = new MailRepositoryJDBCImpl(dataSource);

        IDataSet dataSet = new FlatXmlDataSetBuilder().build(
                MailRepositoryJDBCImplTest.class.getResourceAsStream("/sql/dataset.xml"));

        try(final Connection connection = getConnection()) {
            IDatabaseConnection databaseConnection = new DatabaseConnection(connection);
            DatabaseOperation.CLEAN_INSERT.execute(databaseConnection, dataSet);
        }
    }

    @Test
    public void testFindAll() {
        List<Mail> items = repository.findAll();
        assertNotNull(items);
        assertFalse(items.isEmpty());
    }

    @Test
    public void testFindById() {
        Mail item = repository.findById(1l);
        assertNotNull(item);
        assertEquals("a", item.getToAddress());
    }

    @Test
    public void testSave() {
        repository.save(
                Mail.builder()
                        .id(2l)
                        .toAddress("a2")
                        .subject("b2")
                        .body("c2")
                        .build()
        );

        Mail item = repository.findById(2l);
        assertNotNull(item);
        assertEquals("a2", item.getToAddress());
    }


    @After
    public void after() {
    }

}