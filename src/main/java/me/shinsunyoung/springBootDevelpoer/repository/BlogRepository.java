package me.shinsunyoung.springBootDevelpoer.repository;

import me.shinsunyoung.springBootDevelpoer.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogRepository extends JpaRepository<Article, Long> {
}
