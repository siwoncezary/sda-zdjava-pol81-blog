package pl.sda.blog.repository;

import pl.sda.blog.entity.Article;
import pl.sda.blog.entity.Tag;

import java.util.List;

public interface TagRepository<T, L extends Number> extends Repository<Tag, Long>{
    List<Article> findArticleByTag(long id);
}
