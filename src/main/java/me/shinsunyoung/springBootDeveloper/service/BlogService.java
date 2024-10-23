package me.shinsunyoung.springBootDeveloper.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.shinsunyoung.springBootDeveloper.domain.Article;
import me.shinsunyoung.springBootDeveloper.dto.AddArticleRequest;
import me.shinsunyoung.springBootDeveloper.dto.UpdateArticleRequest;
import me.shinsunyoung.springBootDeveloper.repository.BlogRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BlogService {

    private final BlogRepository blogRepository;

    public Article save(AddArticleRequest request) {
        return blogRepository.save(request.toEntity());
    }

    public List<Article> findAll() {
        return blogRepository.findAll();
    }

    public Article findById(long id) {
        return blogRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("not found : " + id));
    }

    public void deleteById(long id) {
        blogRepository.deleteById(id);
    }

    @Transactional
    public Article update(long id, UpdateArticleRequest request) {
        Article article = blogRepository.findById(id).orElseThrow(()->new IllegalArgumentException("not found : " + id));
        article.update(request.getTitle(), request.getContent());
        return article;
    }
}
