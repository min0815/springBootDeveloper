package me.shinsunyoung.springBootDeveloper.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateArticleRequest {

    public String title;
    public String content;
}
