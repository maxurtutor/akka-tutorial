package org.maxur.spammer;

import java.util.List;

/**
 * @author Maxim Yunusov
 * @version 1.0
 * @since <pre>10/2/2014</pre>
 */
public interface Repository<T> {

    T findById(Long id);

    List<T> findAll();

    void save(T value);

    void done();
}
