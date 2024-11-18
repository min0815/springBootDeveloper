package me.shinsunyoung.springBootDeveloper.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Base64;

public class CookieUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

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

    //
    public static String serialize(Object obj) {
        try {
            String jsonString = objectMapper.writeValueAsString(obj);
            return Base64.getEncoder().encodeToString(jsonString.getBytes());
        } catch (Exception e) {
            throw new RuntimeException("Serialization failed", e);
        }
    }
    // 1. obj를 바이트 배열로 직렬화
    // 2. 바이트 배열을 쿠키, URL, 또는 JSON과 같은 텍스트 기반 저장소에 저장할 수 없으므로, Base64형식의 문자열로 다시 변환

    public static <T> T deserialize(Cookie cookie, Class<T> cls) {
        try {
            if (cookie == null || cookie.getValue() == null) {
                throw new IllegalArgumentException("Cookie or cookie value is null");
            }
            String jsonString = new String(Base64.getDecoder().decode(cookie.getValue()));
            return objectMapper.readValue(jsonString, cls);
        } catch (Exception e) {
            throw new RuntimeException("Deserialization failed", e);
        }
    }
}
