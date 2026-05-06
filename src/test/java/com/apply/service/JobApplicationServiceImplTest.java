package com.apply.service;

import com.apply.model.ApplicationStatus;
import com.apply.model.JobApplication;
import com.apply.repository.JobApplicationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JobApplicationServiceImplTest {

    @Mock
    private JobApplicationRepository repository;

    @InjectMocks
    private JobApplicationServiceImpl service;

    private JobApplication app;

    @BeforeEach
    void setUp() {
        app = new JobApplication();
        app.setId(1L);
        app.setCompany("Acme Corp");
        app.setPosition("Engineer Intern");
        app.setDateApplied(LocalDate.now());
        app.setStatus(ApplicationStatus.APPLIED);
    }

    @Test
    void findAll_returnsSortedList() {
        when(repository.findByOrderByDateAppliedDesc()).thenReturn(Arrays.asList(app));
        List<JobApplication> result = service.findAll();
        assertThat(result).hasSize(1);
        verify(repository).findByOrderByDateAppliedDesc();
    }

    @Test
    void findById_existingId_returnsApplication() {
        when(repository.findById(1L)).thenReturn(Optional.of(app));
        Optional<JobApplication> result = service.findById(1L);
        assertThat(result).isPresent();
        assertThat(result.get().getCompany()).isEqualTo("Acme Corp");
    }

    @Test
    void findById_nonExistingId_returnsEmpty() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        Optional<JobApplication> result = service.findById(99L);
        assertThat(result).isEmpty();
    }

    @Test
    void save_persistsApplication() {
        when(repository.save(app)).thenReturn(app);
        JobApplication saved = service.save(app);
        assertThat(saved).isEqualTo(app);
        verify(repository).save(app);
    }

    @Test
    void deleteById_callsRepository() {
        doNothing().when(repository).deleteById(1L);
        service.deleteById(1L);
        verify(repository).deleteById(1L);
    }

    @Test
    void findByStatus_returnsFilteredList() {
        when(repository.findByStatus(ApplicationStatus.APPLIED)).thenReturn(Arrays.asList(app));
        List<JobApplication> result = service.findByStatus(ApplicationStatus.APPLIED);
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getStatus()).isEqualTo(ApplicationStatus.APPLIED);
    }

    @Test
    void findByCompany_returnsFilteredList() {
        when(repository.findByCompanyContainingIgnoreCase("Acme")).thenReturn(Arrays.asList(app));
        List<JobApplication> result = service.findByCompany("Acme");
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCompany()).isEqualTo("Acme Corp");
    }
}
