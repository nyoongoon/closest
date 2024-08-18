package com.closest.www.config.configuration;

import com.closest.www.config.configuration.xss.HtmlCharacterEscapes;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class HttpMessageConverterConfig {

    private final ObjectMapper objectMapper;

    public HttpMessageConverterConfig(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Bean
    public StringHttpMessageConverter textAndFormEscapeConverter() {
        return new StringHttpMessageConverter() {
            @Override
            protected String readInternal(Class<? extends String> clazz, HttpInputMessage inputMessage)
                    throws IOException {
                String body = super.readInternal(clazz, inputMessage);
                return StringEscapeUtils.escapeHtml4(body);
            }

//            @Override
//            protected void writeInternal(String str, HttpOutputMessage outputMessage) throws IOException {
//                String escapedStr = StringEscapeUtils.escapeHtml4(str);
//                super.writeInternal(escapedStr, outputMessage);
//            }
        };
    }

    @Bean
    public MappingJackson2HttpMessageConverter jsonEscapeConverter() {
        objectMapper.getFactory().setCharacterEscapes(new HtmlCharacterEscapes());
        return new MappingJackson2HttpMessageConverter(objectMapper);
    }

    @Bean
    public FormHttpMessageConverter formEscapeConverter() {
        FormHttpMessageConverter converter = new FormHttpMessageConverter();
        List<HttpMessageConverter<?>> partConverters = new ArrayList<>();

        // 커스텀 StringHttpMessageConverter 추가
        partConverters.add(textAndFormEscapeConverter()); //문자열 이스케이프 컨버터 추가

        converter.setPartConverters(partConverters);
        return converter;
    }


//    @Bean
//    public MappingJackson2XmlHttpMessageConverter xmlEscapeConverter() {
//        XmlMapper xmlMapper = new XmlMapper();
//        xmlMapper.getFactory().setCharacterEscapes(new HtmlCharacterEscapes());
//        return new MappingJackson2XmlHttpMessageConverter(xmlMapper);
//    }
}
