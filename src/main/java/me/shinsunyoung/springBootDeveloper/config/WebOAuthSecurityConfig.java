package me.shinsunyoung.springBootDeveloper.config;

import lombok.RequiredArgsConstructor;
import me.shinsunyoung.springBootDeveloper.config.jwt.TokenProvider;
import me.shinsunyoung.springBootDeveloper.config.oauth.OAuth2AuthorizationRequestBasedOnCookieRepository;
import me.shinsunyoung.springBootDeveloper.config.oauth.OAuth2SuccessHandler;
import me.shinsunyoung.springBootDeveloper.config.oauth.OAuth2UserCustomService;
import me.shinsunyoung.springBootDeveloper.repository.RefreshTokenRepository;
import me.shinsunyoung.springBootDeveloper.service.UserService;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@RequiredArgsConstructor
@Configuration
public class WebOAuthSecurityConfig {

    private final OAuth2UserCustomService oAuth2UserCustomService;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserService userService;

    @Bean
    public WebSecurityCustomizer configure() {
        return (web) -> web.ignoring()
                .requestMatchers(PathRequest.toH2Console())
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }


    // Spring Boot는 애플리케이션 컨텍스트에서 @Bean으로 정의된 SecurityFilterChain을 자동 검색

    // 1. 클라이언트가 HTTP 요청을 보낸다.
    // 2. DelegatingFilterProxy가 요청을 가로채고 Spring Security의 필터 체인으로 전달한다.
    // 3. filterChain 메소드에 설정한 내용에 따라 필터가 순차적으로 시행된다.

    // 즉, filterChain 메서드는 애플리케이션이 시작될 때 한 번 호출되고, 설정된 필터 체인은 모든 요청마다 작동합니다.😊
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable);

        http.sessionManagement(sessionManagement -> sessionManagement
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/token").permitAll()
                .requestMatchers("/api/**").authenticated()
                .anyRequest().permitAll());

        http.oauth2Login(oauth2 -> oauth2
                .loginPage("/login")
                .authorizationEndpoint(authEndpoint -> authEndpoint
                        .authorizationRequestRepository(oAuth2AuthorizationRequestBasedOnCookieRepository()) // Custom Repository
                )
                .successHandler(oAuth2SuccessHandler())
                .userInfoEndpoint(userInfo -> userInfo
                        .userService(oAuth2UserCustomService)
                )
        );

        http.logout(logout -> logout.logoutSuccessUrl("/login"));

        http.exceptionHandling(exceptionHandling -> exceptionHandling
                .defaultAuthenticationEntryPointFor(
                        new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED),
                        new AntPathRequestMatcher("/api/**")
                )
        );

        return http.build();
    }

    @Bean
    public OAuth2SuccessHandler oAuth2SuccessHandler() {
        return new OAuth2SuccessHandler(tokenProvider,
                refreshTokenRepository,
                oAuth2AuthorizationRequestBasedOnCookieRepository(),
                userService
        );
    }

    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter(tokenProvider);
    }

    @Bean
    public OAuth2AuthorizationRequestBasedOnCookieRepository oAuth2AuthorizationRequestBasedOnCookieRepository() {
        return new OAuth2AuthorizationRequestBasedOnCookieRepository();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
