package me.shinsunyoung.springBootDeveloper.repository;

import me.shinsunyoung.springBootDeveloper.domain.Article;
import me.shinsunyoung.springBootDeveloper.dto.ArticleResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogRepository extends JpaRepository<Article, Long> {
}
