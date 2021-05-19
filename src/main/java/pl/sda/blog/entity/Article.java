package pl.sda.blog.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@NamedQuery(name = "count", query = "Select count(a.id) from Article a")
@NamedQuery(name = "countByAuthor", query = "select a.author, count(a) as count from Article a group by a.author")
@NamedQuery(name = "noNullContent", query =  "select a from Article a where a.content is not null")
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String author;

    @Column(unique = true, length = 100)
    private String title;

    @Column(length = 10_000)
    private String content;

    @CreationTimestamp
    private Timestamp published;

    private int rating;

    @PrePersist
    private void setDefaultTimestamp(){
        System.out.println("Wywołanie metody PrePersist");
        //lepiej korzystać z adnotacji automatycznego tworzenia timestamp
        //published = Timestamp.valueOf(LocalDateTime.now());
    }
}
