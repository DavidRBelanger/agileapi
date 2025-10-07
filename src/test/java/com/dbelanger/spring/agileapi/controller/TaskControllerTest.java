package com.dbelanger.spring.agileapi.controller;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
import com.dbelanger.spring.agileapi.model.Task;
import com.dbelanger.spring.agileapi.model.User;
import com.dbelanger.spring.agileapi.security.JwtAuthenticationFilter;
import com.dbelanger.spring.agileapi.service.TaskService;

@WebMvcTest(controllers = TaskController.class)
@AutoConfigureMockMvc(addFilters = false)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;
    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private User principal;
    private Sprint sprint;

    @BeforeEach
    void setup() {
        Organization org = new Organization(); org.setId(1L);
        principal = new User(); principal.setOrganization(org);
        Project project = new Project(); project.setOrganization(org);
        sprint = new Sprint(); sprint.setId(3L); sprint.setProject(project);
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
    void listTasks() throws Exception {
        when(taskService.getTasksBySprintId(3L, principal, null, null, null)).thenReturn(List.of(new Task()));
        mockMvc.perform(get("/api/v1/sprints/3/tasks").principal(auth()))
                .andExpect(status().isOk());
    }

    @Test
    void createTask() throws Exception {
        Task t = new Task(); t.setTitle("Test Task");
        when(taskService.createTask(any(Task.class), eq(3L), eq(8L), eq(principal))).thenReturn(t);
        mockMvc.perform(post("/api/v1/sprints/3/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Test Task\",\"priority\":2,\"assigneeId\":8}")
                        .principal(auth()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Test Task"));
    }
}
