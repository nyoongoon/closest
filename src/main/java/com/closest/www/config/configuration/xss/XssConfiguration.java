package com.closest.www.config.configuration.xss;

import org.apache.commons.text.translate.AggregateTranslator;
import org.apache.commons.text.translate.CharSequenceTranslator;
import org.apache.commons.text.translate.LookupTranslator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.HashMap;
import java.util.Map;

/**
 * 기존 lucy-filter와 CharacterEscapes를 사용한 HttpMessageConverter 방식은 입력값이 아닌 출력값에 대한 문자열 이스케이프 적용 중이었음
 */
@Configuration
public class XssConfiguration {
    @ModelAttribute
    @Bean
    public CharSequenceTranslator charSequenceTranslator() {
        // 커스텀 이스케이프 설정
        Map<CharSequence, CharSequence> escapes = new HashMap<>() {{
            put("<", "&lt;");
            put(">", "&gt;");
            put("(", "&#40;");
            put(")", "&#41;");
            put("#", "&#35;");
            put("'", "&apos;");
            put("&", "&amp;");
            put(Character.toString(8226), "·");
            put(Character.toString(8729), "·");
            put(Character.toString(8901), "·");
            put(Character.toString(9702), "·");
        }};

        return new AggregateTranslator(
                new LookupTranslator(
                        escapes
                )
        );
    }
}