package pl.sda.blog.repository;

import java.util.List;

public interface Repository<T, K>{
    void save(T entity);
    T merge(T entity);
    T findById(K id);
    void deleteById(K id);
    void delete(T entity);
    List<T> findAll();
}
