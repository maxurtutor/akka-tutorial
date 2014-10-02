package org.maxur.spammer;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Maxim Yunusov
 * @version 1.0 14.09.2014
 */
public class MailRepositoryJDBCImpl implements Repository<Mail> {

    private final DataSource dataSource;

    public MailRepositoryJDBCImpl(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Mail findById(Long id) {
        JdbcTemplate select = new JdbcTemplate(dataSource);
        final List<Mail> result = select
                .query("select ID, ADDRESS, SUBJECT, BODY from MAIL where ID = ?",
                        new Object[]{id},
                        new ItemRowMapper());
        if (result.isEmpty()) {
            return null;
        }
        if (result.size() > 1) {
            throw new IllegalStateException("There are more than one item by one id");
        }
        return result.get(0);
    }


    @Override
    public List<Mail> findAll() {
        JdbcTemplate select = new JdbcTemplate(dataSource);
        return select
                .query("select ID, ADDRESS, SUBJECT, BODY from MAIL",
                        new ItemRowMapper());
    }

    @Override
    public void save(Mail value) {
        String sql = "INSERT INTO MAIL " +
                "(ID, ADDRESS, SUBJECT, BODY) VALUES (?, ?, ?, ?)";

        JdbcTemplate insert = new JdbcTemplate(dataSource);

        insert.update(sql,
                value.getId(),
                value.getToAddress(),
                value.getSubject(),
                value.getBody()
        );
    }

    @Override
    public void done() {
        // TODO
    }


    private static class ItemRowMapper implements RowMapper<Mail> {
        @Override
        public Mail mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new ItemExtractor().extractData(rs);
        }
    }


    private static class ItemExtractor implements ResultSetExtractor<Mail> {
        @Override
        public Mail extractData(ResultSet rs) throws SQLException, DataAccessException {
            return Mail.builder()
                    .id(rs.getLong("ID"))
                    .toAddress(rs.getString("ADDRESS"))
                    .subject(rs.getString("SUBJECT"))
                    .body(rs.getString("BODY"))
                    .build();
        }
    }
}
