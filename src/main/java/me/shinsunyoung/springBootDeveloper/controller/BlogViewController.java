package me.shinsunyoung.springBootDeveloper.controller;

import lombok.RequiredArgsConstructor;
import me.shinsunyoung.springBootDeveloper.domain.Article;
import me.shinsunyoung.springBootDeveloper.dto.ArticleListViewResponse;
import me.shinsunyoung.springBootDeveloper.dto.ArticleResponse;
import me.shinsunyoung.springBootDeveloper.dto.ArticleViewResponse;
import me.shinsunyoung.springBootDeveloper.service.BlogService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BlogViewController {

    private final BlogService blogService;

    @GetMapping("/articles")
    public String getArticles(Model model) {
        List<ArticleListViewResponse> articles = blogService.findAll()
                .stream()
                .map(ArticleListViewResponse::new)
                .toList();
        model.addAttribute("articles", articles);

        return "articleList";
    }

    @GetMapping("/articles/{id}")
    public String getArticle(Model model, @PathVariable Long id) {
        ArticleViewResponse article = new ArticleViewResponse(blogService.findById(id));
        model.addAttribute("article", article);
        return "article";
    }

    @GetMapping("/new-article")
    public String newArticle(Model model, @RequestParam(required = false) Long id) {
        if (id == null) {
            model.addAttribute("article", new ArticleViewResponse());
        }else {
            Article article = blogService.findById(id);
            model.addAttribute("article", new ArticleViewResponse(article));
        }

        return "newArticle";
    }
}
