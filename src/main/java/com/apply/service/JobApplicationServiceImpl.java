package com.apply.service;

import com.apply.model.ApplicationStatus;
import com.apply.model.JobApplication;
import com.apply.repository.JobApplicationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class JobApplicationServiceImpl implements JobApplicationService {

    private final JobApplicationRepository repository;

    public JobApplicationServiceImpl(JobApplicationRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<JobApplication> findAll() {
        return repository.findByOrderByDateAppliedDesc();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<JobApplication> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public JobApplication save(JobApplication jobApplication) {
        return repository.save(jobApplication);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<JobApplication> findByStatus(ApplicationStatus status) {
        return repository.findByStatus(status);
    }

    @Override
    @Transactional(readOnly = true)
    public List<JobApplication> findByCompany(String company) {
        return repository.findByCompanyContainingIgnoreCase(company);
    }
}
