package com.apply.controller;

import com.apply.service.JobScraperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class JobSearchController {

    private final JobScraperService jobScraperService;

    @Autowired
    public JobSearchController(JobScraperService jobScraperService) {
        this.jobScraperService = jobScraperService;
    }

    @GetMapping("/job-search")
    public String jobSearch(
            @RequestParam(required = false) String term,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String job_type,
            Model model) {

        if (term != null && !term.isEmpty()) {
            try {
                // Execute the Python script using the service
                String jsonResults = jobScraperService.scrapeJobs(term, location);

                // Pass the raw JSON
                model.addAttribute("jobs", jsonResults);
            } catch (Exception e) {
                model.addAttribute("error", "Error running scraper: " + e.getMessage());
            }
        }
        return "job-search";
    }
}
