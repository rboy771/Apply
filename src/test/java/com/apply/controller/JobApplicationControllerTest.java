package com.apply.controller;

import com.apply.model.ApplicationStatus;
import com.apply.model.JobApplication;
import com.apply.service.JobApplicationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {JobApplicationController.class, GlobalExceptionHandler.class})
class JobApplicationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JobApplicationService service;

    private JobApplication sampleApp;

    @BeforeEach
    void setUp() {
        sampleApp = new JobApplication();
        sampleApp.setId(1L);
        sampleApp.setCompany("Acme Corp");
        sampleApp.setPosition("Software Engineer Intern");
        sampleApp.setDateApplied(LocalDate.of(2024, 1, 15));
        sampleApp.setStatus(ApplicationStatus.APPLIED);
        sampleApp.setLocation("Remote");
    }

    @Test
    void listApplications_returnsIndexView() throws Exception {
        when(service.findAll()).thenReturn(Arrays.asList(sampleApp));

        mockMvc.perform(get("/applications"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("applications"))
                .andExpect(model().attribute("applications", hasSize(1)));
    }

    @Test
    void listApplications_filterByStatus_callsService() throws Exception {
        when(service.findByStatus(ApplicationStatus.APPLIED)).thenReturn(Arrays.asList(sampleApp));

        mockMvc.perform(get("/applications").param("status", "APPLIED"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("applications", hasSize(1)));

        verify(service, times(1)).findByStatus(ApplicationStatus.APPLIED);
    }

    @Test
    void listApplications_searchByCompany_callsService() throws Exception {
        when(service.findByCompany("Acme")).thenReturn(Arrays.asList(sampleApp));

        mockMvc.perform(get("/applications").param("company", "Acme"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("applications", hasSize(1)));

        verify(service, times(1)).findByCompany("Acme");
    }

    @Test
    void showAddForm_returnsFormView() throws Exception {
        mockMvc.perform(get("/applications/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("form"))
                .andExpect(model().attributeExists("application"))
                .andExpect(model().attributeExists("statuses"));
    }

    @Test
    void saveNew_validData_redirectsToList() throws Exception {
        when(service.save(any())).thenReturn(sampleApp);

        mockMvc.perform(post("/applications/new")
                        .param("company", "Acme Corp")
                        .param("position", "Software Engineer Intern")
                        .param("dateApplied", "2024-01-15")
                        .param("status", "APPLIED"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/applications"));
    }

    @Test
    void saveNew_missingCompany_returnsFormWithErrors() throws Exception {
        mockMvc.perform(post("/applications/new")
                        .param("company", "")
                        .param("position", "Software Engineer Intern")
                        .param("dateApplied", "2024-01-15")
                        .param("status", "APPLIED"))
                .andExpect(status().isOk())
                .andExpect(view().name("form"))
                .andExpect(model().attributeHasFieldErrors("application", "company"));
    }

    @Test
    void showEditForm_existingId_returnsFormView() throws Exception {
        when(service.findById(1L)).thenReturn(Optional.of(sampleApp));

        mockMvc.perform(get("/applications/1/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("form"))
                .andExpect(model().attribute("application", sampleApp));
    }

    @Test
    void showEditForm_nonExistingId_returnsErrorView() throws Exception {
        when(service.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/applications/99/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }

    @Test
    void viewApplication_existingId_returnsViewTemplate() throws Exception {
        when(service.findById(1L)).thenReturn(Optional.of(sampleApp));

        mockMvc.perform(get("/applications/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("view"))
                .andExpect(model().attribute("jobApp", sampleApp));
    }

    @Test
    void deleteApplication_redirectsToList() throws Exception {
        doNothing().when(service).deleteById(1L);

        mockMvc.perform(post("/applications/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/applications"));

        verify(service, times(1)).deleteById(1L);
    }

    @Test
    void homeRedirect_redirectsToApplications() throws Exception {
        mockMvc.perform(get("/applications/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/applications"));
    }
}
