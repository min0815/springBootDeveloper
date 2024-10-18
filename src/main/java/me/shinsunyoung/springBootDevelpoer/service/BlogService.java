package me.shinsunyoung.springBootDevelpoer.service;

import lombok.RequiredArgsConstructor;
import me.shinsunyoung.springBootDevelpoer.domain.Article;
import me.shinsunyoung.springBootDevelpoer.repository.BlogRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BlogService {

    private final BlogRepository blogRepository;

    public Article save(Article article) {
        return blogRepository.save(article);
    }

    public List<Article> findAll() {
        return blogRepository.findAll();
    }
}
