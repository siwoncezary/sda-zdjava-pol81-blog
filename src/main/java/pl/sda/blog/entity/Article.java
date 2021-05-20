package pl.sda.blog.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Builder
@NamedQuery(name = "count", query = "Select count(a.id) from Article a")
@NamedQuery(name = "countByAuthor", query = "select a.author, count(a) as count from Article a group by a.author")
@NamedQuery(name = "noNullContent", query =  "select a from Article a where a.content is not null")
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private Author author;

    @Column(unique = true, length = 100)
    private String title;

    @Column(length = 10_000)
    private String content;

    @CreationTimestamp
    private Timestamp published;

    private int rating;
}
