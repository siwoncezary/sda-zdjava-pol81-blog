package pl.sda.blog.repository;

import pl.sda.blog.entity.Author;

import java.util.Optional;

public interface AuthorRepository extends Repository<Author, Long>{
    Optional<Author> findByNick(String nick);
}
