package com.event.events.util;

import java.security.SecureRandom;
import java.util.Base64;

public class TokenUtil {

    private static final SecureRandom secureRandom = new SecureRandom();

    public static String generateSecureToken(int length) {
        byte[] bytes = new byte[length];
        secureRandom.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
