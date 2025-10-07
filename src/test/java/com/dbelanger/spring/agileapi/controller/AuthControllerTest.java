package com.dbelanger.spring.agileapi.controller;

import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dbelanger.spring.agileapi.auth.AuthController;
import com.dbelanger.spring.agileapi.auth.AuthService;
import com.dbelanger.spring.agileapi.model.Organization;
import com.dbelanger.spring.agileapi.model.User;
import com.dbelanger.spring.agileapi.security.JwtAuthenticationFilter;

@WebMvcTest(controllers = AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    void register() throws Exception {
        Organization org = new Organization(); org.setId(1L);
        User user = new User(); user.setId(55L); user.setName("Tester"); user.setEmail("t@example.com"); user.setOrganization(org);
        when(authService.register(anyString(), anyString(), anyString(), anyString(), anyString())).thenReturn(user);
        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"tester@example.com\",\"password\":\"password123\",\"name\":\"Test User\",\"organizationName\":\"Test Organization\",\"organizationSlug\":\"test-org\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(55));
    }

    @Test
    void login() throws Exception {
        AuthService.AuthResponse resp = new AuthService.AuthResponse("tok", "Bearer");
        when(authService.login("tester@example.com", "password123")).thenReturn(resp);
        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"tester@example.com\",\"password\":\"password123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("tok"));
    }
}
