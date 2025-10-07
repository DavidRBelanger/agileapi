package com.dbelanger.spring.agileapi.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dbelanger.spring.agileapi.model.Organization;
import com.dbelanger.spring.agileapi.model.User;
import com.dbelanger.spring.agileapi.security.JwtAuthenticationFilter;
import com.dbelanger.spring.agileapi.service.UserService;

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;
    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private User principal;
    private Organization org;

    @BeforeEach
    void setup() {
        org = new Organization(); org.setId(1L);
        principal = new User(); principal.setId(5L); principal.setOrganization(org);
    }

    private Authentication auth() {
        return new Authentication() {
            @Override public String getName() { return "u"; }
            @Override public void setAuthenticated(boolean isAuthenticated) {}
            @Override public boolean isAuthenticated() { return true; }
            @Override public Object getPrincipal() { return principal; }
            @Override public Object getDetails() { return null; }
            @Override public Object getCredentials() { return null; }
            @Override public java.util.Collection<org.springframework.security.core.GrantedAuthority> getAuthorities(){return java.util.List.of();}
        };
    }

    @Test
    void getUser() throws Exception {
        User u = new User(); u.setId(5L); u.setName("Tester"); u.setEmail("t@example.com"); u.setOrganization(org);
        when(userService.getUserById(5L, principal)).thenReturn(u);
        mockMvc.perform(get("/api/v1/users/5").principal(auth()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5));
    }

    @Test
    void patchUser() throws Exception {
        User u = new User(); u.setId(5L); u.setName("NewName"); u.setEmail("t@example.com"); u.setOrganization(org);
        when(userService.updateUserById(anyLong(), any(User.class), any(User.class))).thenReturn(u);
        mockMvc.perform(patch("/api/v1/users/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"NewName\"}")
                        .principal(auth()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("NewName"));
    }
}
