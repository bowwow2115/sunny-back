package com.sunny.util;

import jakarta.servlet.http.Cookie;

public class ServletUtil {
    public static boolean isInCookies(Cookie[] cookies, String name){
        if(cookies == null) return false;
        for (Cookie cookie : cookies) {
            if(name.equals(cookie.getName())) return true;
        }
        return false;
    }

    public static String isInCookiesValue(Cookie[] cookies, String name) {
        if (cookies == null)
            return null;
        for (Cookie cookie : cookies) {
            if (name.equals(cookie.getName()))
                return cookie.getValue();
        }
        return null;
    }

    public static Cookie getCookies(Cookie[] cookies, String name){
        if(cookies == null) return null;
        for (Cookie cookie : cookies) {
            if(name.equals(cookie.getName())) return cookie;
        }
        return null;

    }
}