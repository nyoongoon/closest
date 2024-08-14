package com.closest.www.config.configuration;

import com.closest.www.config.configuration.xss.HtmlCharacterEscapes;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@Configuration
public class HttpMessageConverterConfig {

    private final ObjectMapper objectMapper;

    public HttpMessageConverterConfig(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * HttpMessageConverter가 Bean으로 등록될 경우
     * 스프링 컨텍스트의 Converter 리스트에 이를 자동으로 추가해준다.
     *
     * @return
     */
    @Bean
    public MappingJackson2HttpMessageConverter jsonEscapeConverter() {

        objectMapper.getFactory().setCharacterEscapes(new HtmlCharacterEscapes());
        return new MappingJackson2HttpMessageConverter(objectMapper);
    }
}
