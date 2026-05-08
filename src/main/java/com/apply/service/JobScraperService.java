package com.apply.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Paths;

@Service
public class JobScraperService {

    private static final Logger logger = LoggerFactory.getLogger(JobScraperService.class);

    public String scrapeJobs(String searchTerm, String location) {
        logger.info("Attempting to scrape jobs for term='{}' and location='{}'", searchTerm, location);
        StringBuilder output = new StringBuilder();
        try {
            // Using just python no .venv
            String pythonCommand = "python";
            String scriptPath = Paths.get("job_scraper.py").toAbsolutePath().toString();

            ProcessBuilder processBuilder = new ProcessBuilder(
                    pythonCommand,
                    scriptPath,
                    searchTerm,
                    location != null ? location : "" // Ensure location is not empty
            );
            processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line);
            }

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                logger.info("Python script executed successfully.");
                // Log the first 500 characters of the output to avoid flooding the console
                logger.info("Python script output (first 500 chars): {}", output.length() > 500 ? output.substring(0, 500) + "..." : output);
            } else {
                logger.error("Python script exited with code: {}", exitCode);
                logger.error("Python script output (if any): {}", output);
            }

        } catch (Exception e) {
            logger.error("Failed to run Python scraper", e);
            return "{\"error\": \"Failed to run scraper\", \"jobs\": []}";
        }

        // This is the raw JSON string returned to the controller
        return output.toString();
    }
}
