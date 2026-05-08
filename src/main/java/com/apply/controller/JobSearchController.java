package com.apply.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class JobSearchController {

    @GetMapping("/job-search")
    public String jobSearch() {
        return "job-search";
    }
}