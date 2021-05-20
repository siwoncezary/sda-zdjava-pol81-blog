package pl.sda.blog;

import pl.sda.blog.entity.Article;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.*;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Zdefiniuj encję opisująca autora w pakiecie `entity`
 * Author:
 *  id
 *  firstName
 *  lastName
 *  nick
 *  registered - timestamp
 * Napisz krótki program, który dodaje jednego autora do bazy
 */
public class EntityManagerDemo {
    public static void main(String[] args) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("blog");
        EntityManager entityManager = factory.createEntityManager();
        Article article = new Article(0, "Tomek", "BBB","BBB", Timestamp.valueOf(LocalDateTime.now()), 14);
        //zapis encji do bazy
        entityManager.getTransaction().begin();
        entityManager.persist(article);
        entityManager.getTransaction().commit();

        //odczyt encji z bazy - encja jest w stanie zarządzania przez entity managera
        Article articleFromBase = entityManager.find(Article.class, 1L);
        System.out.println(articleFromBase);

        //update artykułu tylko, gdy obiekt jest w stanie managed
        entityManager.getTransaction().begin();
        articleFromBase.setContent("XXXXXXXXXXX");
        entityManager.getTransaction().commit();

        //usunięcie encji
        entityManager.getTransaction().begin();
        Article removedArticle = entityManager.find(Article.class, 2L);
        System.out.println(removedArticle);
        if (removedArticle != null) {
            entityManager.remove(removedArticle);
        } else{
            System.out.println("Nie można usunąć, brak takiego artykułu");
        }
        entityManager.getTransaction().commit();

        //zapytanie w JPQL
        List<Article> articles = entityManager.createQuery("select a from Article a", Article.class).getResultList();
        System.out.println(articles);

        //Wywołanie named query
        Long count = entityManager.createNamedQuery("count", Long.class).getSingleResult();
        System.out.println("Liczba autorów: " + count);

        //Wywołanie named query zwracające listę autorów i liczbę ich artykułów
        class AuthorCount{
            String author;
            long count;

            public String getAuthor() {
                return author;
            }

            public void setAuthor(String author) {
                this.author = author;
            }

            public long getCount() {
                return count;
            }

            public void setCount(long count) {
                this.count = count;
            }

            @Override
            public String toString() {
                return "AuthorCount{" +
                        "author='" + author + '\'' +
                        ", count=" + count +
                        '}';
            }
        }
        List<Object[]> countByAuthor = entityManager.createNamedQuery("countByAuthor").getResultList();
        //przykład przeglądania wyników zapytania, które mapujemy do kolekcji tablic objectów
        for(Object[] row: countByAuthor){
            System.out.print("Autor: " + row[0] +", liczba artykułów: " + row[1]);
            System.out.println();
        }
        //Zamiana wyniku zapytania na mapę
        Map<String, Long> countArticleByAuthor = countByAuthor.stream().collect(Collectors.toMap(
                row -> (String) row[0],
                row -> (Long) row[1]
        ));
        System.out.println(countArticleByAuthor);
    }
}
