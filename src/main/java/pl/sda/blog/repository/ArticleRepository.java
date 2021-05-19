package pl.sda.blog.repository;

import pl.sda.blog.entity.Article;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ArticleRepository implements Repository<Article, Long>{
    private final EntityManagerFactory factory;

    public ArticleRepository(EntityManagerFactory factory) {
        this.factory = factory;
    }

    private EntityManager manager(){
        return factory.createEntityManager();
    }

    @Override
    public void save(Article entity) {
        EntityManager em = manager();
        em.getTransaction().begin();
        em.persist(entity);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public Article merge(Article entity) {
        EntityManager em = manager();
        em.getTransaction().begin();
        Article managed = em.merge(entity);
        em.getTransaction().commit();
        em.close();
        return managed;
    }

    @Override
    public Article findById(Long id) {
        EntityManager em = manager();
        Article article = em.find(Article.class, id);
        em.close();
        return article;
    }

    @Override
    public void deleteById(Long id) {
        EntityManager em = manager();
        em.getTransaction().begin();
        Article article = em.find(Article.class, id);
        if (article == null){
            return;
        }
        em.remove(article);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public void delete(Article entity) {
        EntityManager em = manager();
        em.getTransaction().begin();
        em.remove(entity);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public List<Article> findAll() {
        EntityManager em = manager();
        List<Article> articles = em.createQuery("select a from Article a", Article.class).getResultList();
        em.close();
        return articles;
    }

    public List<Article> findByAuthor(String author){
        EntityManager em = manager();
//        Query query = em.createQuery("select a from Article a where a.author = :author order by a.rating", Article.class);
//        query.setParameter("author", author);
//        List<Article> resultList = query.getResultList();
        List<Article> articles = em.createQuery("select a from A" +
                "rticle a where a.author = :author order by a.rating", Article.class)
                .setParameter("author", author)
                .getResultList();
        em.close();
        return articles;
    }

    public List<Article> findByRatingGraterThan(int level){
        return null;
    }

    public long sumAllRatingForAuthor(String author){
        EntityManager em = manager();
        Long sum = em.createQuery("select sum(a.rating) from Article a group by a.author having a.author = :author", Long.class)
                .setParameter("author", author)
                .getSingleResult();
        em.close();
        return sum;
    }
}
