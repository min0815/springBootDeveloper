package me.shinsunyoung.springBootDeveloper.dto;

import lombok.Getter;
import me.shinsunyoung.springBootDeveloper.domain.Article;

@Getter
public class ArticleListViewResponse {
    private final long id;
    private final String title;
    private final String content;

    public ArticleListViewResponse(Article article){
        this.id = article.getId();
        this.title = article.getTitle();
        this.content = article.getContent();
    }

}
