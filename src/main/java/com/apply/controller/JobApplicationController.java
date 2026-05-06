package com.apply.controller;

import com.apply.model.ApplicationStatus;
import com.apply.model.JobApplication;
import com.apply.service.JobApplicationService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/applications")
public class JobApplicationController {

    private final JobApplicationService service;

    public JobApplicationController(JobApplicationService service) {
        this.service = service;
    }

    /** Redirect root to applications list */
    @GetMapping("/")
    public String home() {
        return "redirect:/applications";
    }

    /** List all applications, with optional search/filter */
    @GetMapping
    public String list(
            @RequestParam(required = false) String company,
            @RequestParam(required = false) ApplicationStatus status,
            Model model) {

        List<JobApplication> applications;

        if (company != null && !company.isBlank()) {
            applications = service.findByCompany(company);
            model.addAttribute("searchCompany", company);
        } else if (status != null) {
            applications = service.findByStatus(status);
            model.addAttribute("searchStatus", status);
        } else {
            applications = service.findAll();
        }

        model.addAttribute("applications", applications);
        model.addAttribute("statuses", ApplicationStatus.values());
        return "index";
    }

    /** Show the "add new application" form */
    @GetMapping("/new")
    public String showAddForm(Model model) {
        JobApplication application = new JobApplication();
        application.setDateApplied(LocalDate.now());
        application.setStatus(ApplicationStatus.APPLIED);
        model.addAttribute("application", application);
        model.addAttribute("statuses", ApplicationStatus.values());
        model.addAttribute("formTitle", "Add Job Application");
        return "form";
    }

    /** Process the add form */
    @PostMapping("/new")
    public String saveNew(
            @Valid @ModelAttribute("application") JobApplication application,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("statuses", ApplicationStatus.values());
            model.addAttribute("formTitle", "Add Job Application");
            return "form";
        }
        service.save(application);
        redirectAttributes.addFlashAttribute("successMessage", "Application added successfully!");
        return "redirect:/applications";
    }

    /** Show the edit form for an existing application */
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        JobApplication application = service.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid application id: " + id));
        model.addAttribute("application", application);
        model.addAttribute("statuses", ApplicationStatus.values());
        model.addAttribute("formTitle", "Edit Job Application");
        return "form";
    }

    /** Process the edit form */
    @PostMapping("/{id}/edit")
    public String saveEdit(
            @PathVariable Long id,
            @Valid @ModelAttribute("application") JobApplication application,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("statuses", ApplicationStatus.values());
            model.addAttribute("formTitle", "Edit Job Application");
            return "form";
        }
        application.setId(id);
        service.save(application);
        redirectAttributes.addFlashAttribute("successMessage", "Application updated successfully!");
        return "redirect:/applications";
    }

    /** View details of a single application */
    @GetMapping("/{id}")
    public String view(@PathVariable Long id, Model model) {
        JobApplication jobApp = service.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid application id: " + id));
        model.addAttribute("jobApp", jobApp);
        return "view";
    }

    /** Delete an application */
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        service.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Application deleted successfully!");
        return "redirect:/applications";
    }
}
