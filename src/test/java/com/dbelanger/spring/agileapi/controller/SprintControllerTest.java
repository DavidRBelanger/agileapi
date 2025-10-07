package com.dbelanger.spring.agileapi.controller;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dbelanger.spring.agileapi.model.Organization;
import com.dbelanger.spring.agileapi.model.Project;
import com.dbelanger.spring.agileapi.model.Sprint;
import com.dbelanger.spring.agileapi.model.User;
import com.dbelanger.spring.agileapi.security.JwtAuthenticationFilter;
import com.dbelanger.spring.agileapi.service.SprintService;

@WebMvcTest(controllers = SprintController.class)
@AutoConfigureMockMvc(addFilters = false)
class SprintControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SprintService sprintService;
    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private User principal;
    private Project project;

    @BeforeEach
    void setup() {
        Organization org = new Organization(); org.setId(1L);
        principal = new User(); principal.setOrganization(org);
        project = new Project(); project.setId(7L); project.setOrganization(org);
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
    void listSprints() throws Exception {
        when(sprintService.getSprintsByProjectId(7L, principal)).thenReturn(List.of(new Sprint()));
        mockMvc.perform(get("/api/v1/projects/7/sprints").principal(auth()))
                .andExpect(status().isOk());
    }

    @Test
    void createSprint() throws Exception {
        Sprint s = new Sprint(); s.setName("Sprint 1");
        when(sprintService.createSprint(org.mockito.ArgumentMatchers.any(Sprint.class), org.mockito.ArgumentMatchers.eq(7L), org.mockito.ArgumentMatchers.eq(principal))).thenReturn(s);
        mockMvc.perform(post("/api/v1/projects/7/sprints")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Sprint 1\",\"startDate\":\"2025-10-01\",\"endDate\":\"2025-10-14\"}")
                        .principal(auth()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Sprint 1"));
    }
}
