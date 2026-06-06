package com.pm.urlshortner.service;

import com.pm.urlshortner.repository.UrlRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShortCodeGeneratorTest {

    @InjectMocks
    private ShortCodeGenerator generator;

    @Mock
    private UrlRepository urlRepository;

    @Test
    void shouldGenerateSevenCharacterCode() {
        String code = generator.generate();
        assertNotNull(code);
        assertEquals(7, code.length());
        assertTrue(code.matches("^[a-zA-Z0-9]+$"));
    }

    @Test
    void shouldGenerateUniqueCode() {
        when(urlRepository.existsByShortCode(anyString()))
                .thenReturn(true) // First code exists
                .thenReturn(false); // Second code is unique

        String code = generator.generateUnique(urlRepository);

        assertNotNull(code);
        assertEquals(7, code.length());
        verify(urlRepository, times(2)).existsByShortCode(anyString());
    }

    @Test
    void shouldValidateCustomCode() {
        assertTrue(generator.isValidCustomCode("custom123"));
        assertTrue(generator.isValidCustomCode("ValidCode"));

        assertFalse(generator.isValidCustomCode("abc")); // Too short
        assertFalse(generator.isValidCustomCode("thiscodeiswaytoolongforoursystem")); // Too long
        assertFalse(generator.isValidCustomCode("not_alphanumeric!")); // Special characters
        assertFalse(generator.isValidCustomCode(null));
    }

    @Test
    void shouldBeDifferentOnSubsequentCalls() {
        String code1 = generator.generate();
        String code2 = generator.generate();
        assertNotEquals(code1, code2);
    }
}
