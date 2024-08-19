package com.closest.www.config.configuration;

import com.closest.www.config.configuration.xss.HtmlCharacterEscapes;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.util.stream.Collectors;

/**
 * @EnableWebMvc 이 활성화되면 이들 Converter들이 모두 @EnableWebMvc가 들어간 설정으로 덮어씌워짐
 * -> WebMvcConfig를 구현하지 않고 컨버터 빈 등록만 하는 것으로 해결
 * -> 컨버터 빈 등록하면 스프링이 자동으로 컨버터를 프로젝트 실행 시 추가한다.
 */
@Configuration
public class HttpMessageConverterConfig  {

    private final ObjectMapper objectMapper;

    public HttpMessageConverterConfig(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * application/x-www-form-urlencoded
     * - @RequestParam은 파라미터 요청이므로 컨버터를 타지 않아서 @RequestBody를 파라미터로 받아야함
     * @return
     */
    @Bean
    public FormHttpMessageConverter formUrlEncodedEscapeConverter() {
        return new FormHttpMessageConverter() {
            @Override
            public MultiValueMap<String, String> read(Class<? extends MultiValueMap<String, ?>> clazz,
                                                         HttpInputMessage inputMessage) throws IOException {
                MultiValueMap<String, String> body = super.read(clazz, inputMessage);
                return escapeMap(body);
            }

            private MultiValueMap<String, String> escapeMap(MultiValueMap<String, String> map) {
                MultiValueMap<String, String> escapedMap = new LinkedMultiValueMap<>();
                map.forEach((key, valueList) ->
                        escapedMap.put(StringEscapeUtils.escapeHtml4(key),
                                valueList.stream()
                                        .map(StringEscapeUtils::escapeHtml4)
                                        .collect(Collectors.toList()))
                );
                return escapedMap;
            }
        };
    }


    /**
     * text/plain & multipart/form-data 문자열 파라미터
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
     * application/json & multipart/form-data 객체 파라미터
     * @return
     */
    @Bean
    public MappingJackson2HttpMessageConverter jsonEscapeConverter() {
        objectMapper.getFactory().setCharacterEscapes(new HtmlCharacterEscapes());

        return new MappingJackson2HttpMessageConverter(objectMapper) {
            @Override
            public boolean canWrite(Class<?> clazz, MediaType mediaType) {
                // 응답인 경우에 요청 태우지 않는다.
                return false;
            }
        };
    }
}
