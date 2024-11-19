package me.shinsunyoung.springBootDeveloper.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.*;
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

    //
    public static String serialize(Object obj) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {

            // 객체를 바이트 배열로 직렬화
            objectOutputStream.writeObject(obj);
            objectOutputStream.flush();

            // 바이트 배열을 Base64 문자열로 변환
            return Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());

        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize object: " + obj.getClass().getName(), e);
        }
    }
    // 1. obj를 바이트 배열로 직렬화
    // 2. 바이트 배열을 쿠키, URL, 또는 JSON과 같은 텍스트 기반 저장소에 저장할 수 없으므로, Base64형식의 문자열로 다시 변환

    @SuppressWarnings("unchecked")
    public static <T> T deserialize(Cookie cookie, Class<T> cls) {
        try {
            if (cookie == null || cookie.getValue() == null) {
                throw new IllegalArgumentException("Cookie or cookie value is null for class: " + cls.getName());
            }

            // Base64 문자열을 바이트 배열로 디코딩
            byte[] data = Base64.getDecoder().decode(cookie.getValue());

            // 바이트 배열을 객체로 역직렬화
            try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
                 ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream)) {

                Object obj = objectInputStream.readObject();
                return cls.cast(obj); // 역직렬화된 객체를 캐스팅
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to deserialize cookie value to class: " + cls.getName(), e);
        }
    }
}
