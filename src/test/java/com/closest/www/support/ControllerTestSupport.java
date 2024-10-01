package com.closest.www.support;

import com.closest.www.api.controller.auth.AuthController;
import com.closest.www.api.controller.blog.BlogController;
import com.closest.www.api.service.auth.AuthService;
import com.closest.www.api.service.blog.BlogService;
import com.closest.www.config.filter.JwtAuthenticationFilter;
import com.closest.www.utility.jwt.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@WebMvcTest(controllers = {
        AuthController.class,
        BlogController.class
})
public class ControllerTestSupport {
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected AuthService authService;
    @MockBean
    protected BlogService blogService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp(WebApplicationContext context) {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .addFilters(new JwtAuthenticationFilter(jwtTokenProvider))
                .build();
    }
}
