package me.shinsunyoung.springBootDeveloper;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.shinsunyoung.springBootDeveloper.domain.Article;
import me.shinsunyoung.springBootDeveloper.dto.AddArticleRequest;
import me.shinsunyoung.springBootDeveloper.repository.BlogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.MockMvcBuilder;
// MockMvc를 생성하는 데 사용되는 빌더 클라스의 인터페이스
// 일반적으로 구체적인 구현 클래스인 DefaultMockMvcBuilder를 통해 사용되며, 이를 통해 세부적인 설정을 조정할 수 있습니다.

import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
// 이 클래스는 주로 webAppContextSetup(WebApplicationContext)와 같은 메서드를 사용하여 MockMvcBuilder를 생성하고,
// 이후 build() 메서드를 통해 최종 MockMvc 인스턴스를 생성

import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest // Spring Boot 애플리케이션의 모든 빈을 로드, 애플리케이션 컨텍스트를 생성하여 통합 테스트 환경 설정
@AutoConfigureMockMvc
class BlogApiControllerTest {

    @Autowired
    protected MockMvc mockMvc;
    // 서버를 실제로 실행하지 않고 컨트롤러의 HTTP 요청 및 응답을 모의로 처리
    // 컨트롤러 메서드를 직접 호출하지 않고도 요청 보내는 것처럼 테스트 가능

    @Autowired
    protected ObjectMapper objectMapper;
    // JSON을 Java 객체로 변환하거나 Java 객체를 JSON으로 변환할 때 사용
    // 테스트 시, 요청 본문을 JSON 형식으로 직렬화, 혹은 응답을 역질렬화 하는데 유용

    @Autowired
    private WebApplicationContext context;
    // MockMvc를 초기화할 때 컨텍스트를 사용하여 모든 웹 관련 빈들 설정 가능

    @Autowired
    BlogRepository blogRepository;
    // 메인 애플리케이션의 blogRepository와는 별도로 동작
    // 분리된 테스트용 컨텍스트에서 실행됨. 테스트 데이터베이스를 사용하므로, 실제 데이터와 격리된 상태로 동작
    // 여기서는 매번 초기화 하기 위해 context 의존성 주입을 한 듯?

    @BeforeEach
    public void mockMvcSetup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        blogRepository.deleteAll();
    }

    @DisplayName("addArticle: 블로그 글 추가에 성공한다.")
    @Test
    public void addArticle() throws Exception {
        // given
        // 가상의 데이터 생성 작업
        final String url = "/api/articles";
        final String title = "title";
        final String content = "content";
        final AddArticleRequest userRequest = new AddArticleRequest(title, content);

        final String requestBody = objectMapper.writeValueAsString(userRequest);
        // Java 객체를 JSON 문자열로 변환

        // when
        ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));
        // MockMvc를 사용해서 HTTP post 요청을 보낼 것이며, 요청 타입은 JSON, 매개변수는 requestBody

        // then
        result.andExpect(status().isCreated());
        // 컨트롤러에서 설정한 요청(created)가 반환됐는지 확인

        List<Article> articles = blogRepository.findAll();

        assertThat(articles.size()).isEqualTo(1);
        assertThat(articles.get(0).getTitle()).isEqualTo(title);
        assertThat(articles.get(0).getContent()).isEqualTo(content);
    }

}
