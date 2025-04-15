package com.example.sunny.util;

import java.security.SecureRandom;

public class HashGenerator {

    private static final String CHARACTERS = "0123456789abcdef";
    private static final SecureRandom random = new SecureRandom();

    public static String generateRandomHash(int length) {
        if (length <= 0) throw new IllegalArgumentException("길이는 1 이상이어야 합니다.");

        StringBuilder hash = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            hash.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return hash.toString();
    }
}
