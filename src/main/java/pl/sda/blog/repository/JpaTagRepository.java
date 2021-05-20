package pl.sda.blog.repository;

import pl.sda.blog.entity.Article;
import pl.sda.blog.entity.Tag;

import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class JpaTagRepository extends JpaRepository<Tag, Long> implements  TagRepository<Tag, Long>{
    public JpaTagRepository(EntityManagerFactory factory, Class<Tag> entityClass) {
        super(factory, entityClass);
    }

    @Override
    public List<Article> findArticleByTag(long id) {
        Optional<Tag> tag = findById(id);
        return tag.map(t -> new ArrayList(t.getArticles())).orElse(new ArrayList<Article>());
    }
}
