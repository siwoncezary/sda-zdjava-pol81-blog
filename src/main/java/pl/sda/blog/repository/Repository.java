package pl.sda.blog.repository;

import java.util.List;
import java.util.Optional;

public interface Repository<T, K> {
    void save(T entity);
    T merge(T entity);
    void deleteById(K id);
    void delete(T entity);
    Optional<T> findById(K id);
    List<T> findAll();
}
