package com.pm.urlshortner.service;

import com.pm.urlshortner.repository.UrlRepository;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class ShortCodeGenerator {

    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int CODE_LENGTH = 7;
    private final SecureRandom random = new SecureRandom();

    /**
     * Generates a 7-character random Base62 code.
     * @return generated code
     */
    public String generate() {
        StringBuilder sb = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }

    /**
     * Generates a unique code by checking against the repository.
     * @param repo the UrlRepository to check uniqueness
     * @return unique generated code
     */
    public String generateUnique(UrlRepository repo) {
        String code;
        do {
            code = generate();
        } while (repo.existsByShortCode(code));
        return code;
    }

    /**
     * Validates if a custom code is alphanumeric and between 4 and 20 characters.
     * @param code custom code to validate
     * @return true if valid
     */
    public boolean isValidCustomCode(String code) {
        if (code == null || code.length() < 4 || code.length() > 20) {
            return false;
        }
        return code.matches("^[a-zA-Z0-9]+$");
    }
}

