package me.shinsunyoung.springBootDeveloper.controller;

import lombok.RequiredArgsConstructor;
import me.shinsunyoung.springBootDeveloper.domain.Article;
import me.shinsunyoung.springBootDeveloper.dto.AddArticleRequest;
import me.shinsunyoung.springBootDeveloper.dto.ArticleResponse;
import me.shinsunyoung.springBootDeveloper.dto.UpdateArticleRequest;
import me.shinsunyoung.springBootDeveloper.service.BlogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;


@RequiredArgsConstructor
@RestController
public class BlogApiController {

    private final BlogService blogService;

    @PostMapping("/api/articles")
    public ResponseEntity<Article> addArticle(@RequestBody AddArticleRequest request, Principal principal) {
        return ResponseEntity.status(HttpStatus.CREATED).body(blogService.save(request, principal.getName()));
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

    @GetMapping("/api/articles/{id}")
    public ResponseEntity<ArticleResponse> findArticle(@PathVariable long id) {
        Article article = blogService.findById(id);
        return ResponseEntity.ok().body(new ArticleResponse(article));
    }

    @DeleteMapping("/api/articles/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable long id) {
        blogService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/api/articles/{id}")
    public ResponseEntity<Article> updateArticle(@PathVariable long id, @RequestBody UpdateArticleRequest request) {
        Article updatedArticle = blogService.update(id, request);
        return ResponseEntity.ok().body(updatedArticle);
    }

}
