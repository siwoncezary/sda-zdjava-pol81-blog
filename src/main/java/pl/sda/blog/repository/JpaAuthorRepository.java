package pl.sda.blog.repository;

import pl.sda.blog.entity.Article;
import pl.sda.blog.entity.Author;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.Optional;

public class JpaAuthorRepository extends JpaRepository<Author, Long> implements AuthorRepository {

    public JpaAuthorRepository(EntityManagerFactory factory, Class<Author> entityClass) {
        super(factory, entityClass);
    }

    @Override
    public Optional<Author> findByNick(String nick) {
        EntityManager em = manager();
        Author author = em.createQuery("SELECT a FROM Author a WHERE a.nick = :nick", Author.class).setParameter("nick", nick).getSingleResult();
        return Optional.ofNullable(author);
    }
}
