package com.closest.www.config.configuration.xss;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.commons.text.translate.CharSequenceTranslator;
import org.apache.commons.text.translate.LookupTranslator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.io.IOException;
import java.util.Map;

import static org.apache.commons.text.StringEscapeUtils.ESCAPE_HTML4;

@Configuration

public class XssConfiguration {
    private final ObjectMapper objectMapper;

    public XssConfiguration(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Bean
    public CharSequenceTranslator charSequenceTranslator() {
        CharSequenceTranslator translator = ESCAPE_HTML4;
        LookupTranslator lookupTranslator = new LookupTranslator(
                // 커스텀 이스케이프 설정
                Map.of(
                        "'", "&apos;"
                )
        );
        return translator.with(lookupTranslator);
    }

    /**
     * http body 값이 text/plain인 경우
     * - multipart/form-data
     * @return
     */
    @Bean
    public StringHttpMessageConverter textEscapeConverter() {
        return new StringHttpMessageConverter() {
            @Override
            protected String readInternal(Class<? extends String> clazz, HttpInputMessage inputMessage)
                    throws IOException {
                String body = super.readInternal(clazz, inputMessage);
                return StringEscapeUtils.escapeHtml4(body);
            }
        };
    }

    /**
     * @ResponseBody가 사용되는
     * application/json 혹은
     * @return
     */
    @Bean
    public MappingJackson2HttpMessageConverter jsonEscapeConverter() {
        objectMapper.getFactory().setCharacterEscapes(new XssCharacterEscapes(charSequenceTranslator()));
        return new MappingJackson2HttpMessageConverter(objectMapper){
            @Override
            public boolean canWrite(Class<?> clazz, MediaType mediaType) {
                return false; //응답시 작동하지 않음
            }
        };
    }
}
