package me.shinsunyoung.springBootDeveloper.config.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

// JWT 관련 설정을 외부설정파일(application.properites 또는 application.yml)에서 가져와서 사용하는데 사용
// JWT 토큰을 생성하고 검증하는 데 필요한 정보를 외부에서 설정할 수 있게 해줌

@Setter
@Getter
@Component
@ConfigurationProperties("jwt")
public class JwtProperties {
    private String issuer;
    private String secretKey;
}

// @Component
// JwtProperties 클래스를 빈으로 등록, 이 객체를 다른 컴포넌트에서 주입받아 사용 가능

// @ConfigurationProperties("jwt")
// application.properties나 application.yml 파일에 설정된 값을 자동으로 매핑해주는 역할
// 이 경우, "jwt" 접두어로 시작하는 프로퍼티를 JwtProperties 클래스의 필드에 매핑
// yml파일의 issuer, secretKey 값이 JwtProperties 클래스의 issuer와 secretKey 필드에 자동으로 할당