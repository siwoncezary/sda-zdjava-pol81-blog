package pl.sda.blog.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

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


    @ManyToOne(fetch = FetchType.LAZY)
    private Author author;

    @Column(unique = true, length = 100)
    private String title;

    @Column(length = 10_000)
    private String content;

    @CreationTimestamp
    private Timestamp published;

    private int rating;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(cascade = CascadeType.PERSIST)
    private Set<Tag> tags = new HashSet<>();

    public void addTag(Tag tag){
        if (tags == null){
            tags = new HashSet<>();
        }
        tags.add(tag);
    }
}
