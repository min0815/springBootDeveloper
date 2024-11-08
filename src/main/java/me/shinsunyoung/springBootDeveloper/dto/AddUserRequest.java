package me.shinsunyoung.springBootDeveloper.dto;

import lombok.Getter;
import lombok.Setter;

// 데이터를 단순히 dto로만 전달하는 역할
// AddArticleRequest는 데이터 전송뿐만 아니라, 해당 데이터를 엔티티로 변환하는 로직도 포함
@Getter
@Setter
public class AddUserRequest {
    private String email;
    private String password;

}
