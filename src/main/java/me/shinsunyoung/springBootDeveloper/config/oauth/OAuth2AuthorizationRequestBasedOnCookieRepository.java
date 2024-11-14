package me.shinsunyoung.springBootDeveloper.config.oauth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.shinsunyoung.springBootDeveloper.util.CookieUtil;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.web.util.WebUtils;

// OAuth2 인증 요청 데이터를 쿠키에 저장하고 관리하는 역할
// 사용자가 OAuth2 인증을 수행할 때 해당 인증 요청 정보를 쿠키를 통해 저장하고, 필요에 따라 불러오거나 삭제하는 기능 제공
public class OAuth2AuthorizationRequestBasedOnCookieRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    // OAuth2 인증요청 데이터를 저장할 쿠키. 쿠키 이름은 'oauth2_auth_request', 만료 시간은 18,000초(5시간)
    public final static String OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME = "oauth2_auth_request";
    private final static int COOKIE_EXPIRE_SECONDS = 18000;

    // HttpServletRequest 객체로부터 OAuth2 인증 요청을 불러오고 삭제하는 역할
    // loadAuthorizationRequest를 호출해 요청을 불러오고, 삭제는 별도로 removeAuthorizationRequestCookies 메서드를 통해 수행
    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request, HttpServletResponse response) {
        return this.loadAuthorizationRequest(request);
    }

    // 요청 객체(HttpServletRequest)로부터 OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME 쿠키를 찾아,
    // 해당 쿠키의 값을 역직렬화하여 OAuth2AuthorizationRequest 객체로 반환
    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
        return CookieUtil.deserialize(cookie, OAuth2AuthorizationRequest.class);
    }

    // OAuth2 인증요청(authorizationRequest)을 받아서 쿠키에 저장
    // authorizationRequest가 null이면 이전에 남아 있을 수 있는 쿠키 삭제 후 return
    // 메서드의 반환값이 void라는 것은 값을 직접 반환하지 않는다는 의미일 뿐,
    // HTTP 응답 객체 response에 쿠키를 추가하여 클라이언트로 전송될 응답에 쿠키가 포함되도록 설정하는 것임
    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request, HttpServletResponse response) {
        if (authorizationRequest == null) {
            removeAuthorizationRequestCookies(request, response);
            return;
        }
        CookieUtil.addCookie(response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME, CookieUtil.serialize(authorizationRequest), COOKIE_EXPIRE_SECONDS);
    }

    private void removeAuthorizationRequestCookies(HttpServletRequest request, HttpServletResponse response) {
        CookieUtil.deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
    }

}
