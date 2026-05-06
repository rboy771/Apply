package com.apply.service;

import com.apply.model.ApplicationStatus;
import com.apply.model.JobApplication;

import java.util.List;
import java.util.Optional;

public interface JobApplicationService {

    List<JobApplication> findAll();

    Optional<JobApplication> findById(Long id);

    JobApplication save(JobApplication jobApplication);

    void deleteById(Long id);

    List<JobApplication> findByStatus(ApplicationStatus status);

    List<JobApplication> findByCompany(String company);
}
