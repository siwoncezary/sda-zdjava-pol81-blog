package pl.sda.blog.entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String firstName;
    private String lastName;

    @Column(unique = true)
    private String nick;

    private Timestamp registered;

    @Embedded
    private EmailAddress address;

    @OneToOne(cascade = CascadeType.ALL)
    private Address location;

    @OneToMany
    private Set<Article> articles = new HashSet<>();
}






