package com.aivle0102.bigproject.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class OpenAiJsonUtilsTest {

    @Test
    @DisplayName("OpenAI 마크다운 코드블록에서 순수한 JSON 문자열 추출 검증")
    void testExtractJsonBlock_withMarkdownCodeBlock() {
        // Given
        String rawContent = "```json\n{\n  \"key\": \"value\"\n}\n```";
        
        // When
        String result = OpenAiJsonUtils.extractJsonBlock(rawContent, '{', '}');
        
        // Then
        assertEquals("{\n  \"key\": \"value\"\n}", result);
    }

    @Test
    @DisplayName("마크다운 코드블록 태그가 없는 일반 JSON 문자열 추출 검증")
    void testExtractJsonBlock_withoutMarkdown() {
        // Given
        String rawContent = "   {\n  \"key\": \"value\"\n}   ";
        
        // When
        String result = OpenAiJsonUtils.extractJsonBlock(rawContent, '{', '}');
        
        // Then
        assertEquals("{\n  \"key\": \"value\"\n}", result);
    }

    @Test
    @DisplayName("입력값이 null이거나 형식이 맞지 않을 때 빈 문자열 반환 검증")
    void testExtractJsonBlock_withInvalidOrNullInput() {
        // Given & When & Then
        assertEquals("", OpenAiJsonUtils.extractJsonBlock(null, '{', '}'));
        assertEquals("invalid content", OpenAiJsonUtils.extractJsonBlock("invalid content", '{', '}'));
    }
}
