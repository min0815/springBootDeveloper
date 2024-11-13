package me.shinsunyoung.springBootDeveloper.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.SerializationUtils;

import java.util.Base64;

public class CookieUtil {

    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);

        response.addCookie(cookie);
    }

    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            return;
        }

        for (Cookie cookie : cookies) {
           if (name.equals(cookie.getName())) {
               cookie.setValue("");
               cookie.setPath("/");
               cookie.setMaxAge(0);
               response.addCookie(cookie);
           }
        }
    }

    public static String serialize(Object obj) {
        return Base64.getEncoder().encodeToString(SerializationUtils.serialize(obj));
    }
    // 1. obj를 바이트 배열로 직렬화
    // 2. 바이트 배열을 쿠키, URL, 또는 JSON과 같은 텍스트 기반 저장소에 저장할 수 없으므로, Base64형식의 문자열로 다시 변환
}
