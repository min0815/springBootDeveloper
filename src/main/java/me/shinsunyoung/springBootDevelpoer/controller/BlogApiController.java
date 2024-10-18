package me.shinsunyoung.springBootDevelpoer.controller;

import lombok.RequiredArgsConstructor;
import me.shinsunyoung.springBootDevelpoer.domain.Article;
import me.shinsunyoung.springBootDevelpoer.dto.ArticleResponse;
import me.shinsunyoung.springBootDevelpoer.service.BlogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class BlogApiController {

    private BlogService blogService;

    @PostMapping("/api/article")
    public ResponseEntity<Article> save(@RequestBody Article article) {
        return ResponseEntity.status(HttpStatus.CREATED).body(blogService.save(article));
    }

    @GetMapping("/api/articles")
    public ResponseEntity<List<ArticleResponse>> findAllArticles() {
        /*
        List<Article> allArticle = blogService.findAll();
        List<ArticleResponse> articles = new ArrayList<>();
        for (Article article : articles) {
           articles.add(new ArticleResponse(allArticle));
        }
        */

        List<ArticleResponse> articles = blogService.findAll()
                .stream()
                .map(ArticleResponse::new)
                .toList();

        return ResponseEntity.ok().body(articles);
    }

}
