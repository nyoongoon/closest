package com.closest.www.support;

import com.closest.www.api.controller.auth.AuthController;
import com.closest.www.api.service.auth.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = {
        AuthController.class
})
public class ControllerTestSupport {
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected AuthService authService;

    @MockBean
    private UserDetailsService userDetailsService;


//    @BeforeEach
//    void setUp(WebApplicationContext context) {
//        MockitoAnnotations.openMocks(this);
//        this.mockMvc = MockMvcBuilders
//                .webAppContextSetup(context)
//                .addFilters(
//
//                )
//                .build();
//    }
}
