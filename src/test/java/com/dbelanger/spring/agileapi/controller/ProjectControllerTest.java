package com.dbelanger.spring.agileapi.controller;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
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
import com.dbelanger.spring.agileapi.model.User;
import com.dbelanger.spring.agileapi.security.JwtAuthenticationFilter;
import com.dbelanger.spring.agileapi.service.ProjectService;

@WebMvcTest(controllers = ProjectController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProjectService projectService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter; // prevent missing bean

    private User principal;

    @BeforeEach
    void setup() {
        Organization org = new Organization(); org.setId(1L);
        principal = new User(); principal.setId(10L); principal.setOrganization(org);
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
    void listProjects() throws Exception {
        when(projectService.getProjectsByOrganizationId(1L, principal)).thenReturn(List.of(new Project()));
        mockMvc.perform(get("/api/v1/projects").principal(auth()))
                .andExpect(status().isOk());
    }

    @Test
    void createProject() throws Exception {
        Project p = new Project(); p.setName("Test Project");
        when(projectService.createNewProject(any(Project.class), any(User.class))).thenReturn(p);
        mockMvc.perform(post("/api/v1/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Test Project\"}")
                        .principal(auth()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Test Project"));
    }
}
