import json
import logging
import sys

from jobspy import scrape_jobs

def _silence_jobspy_logs():
    # Keep stdout clean so Java can parse JSON without log noise.
    logging.disable(logging.CRITICAL)

def fetch_jobs(term, location):
    _silence_jobspy_logs()
    
    # Determine the country for Indeed based on the location text
    country = "USA"
    if location:
        loc_lower = location.lower()
        if "uk" in loc_lower or "london" in loc_lower or "england" in loc_lower:
            country = "UK"
        elif "canada" in loc_lower:
            country = "Canada"
        elif "australia" in loc_lower:
            country = "Australia"
    # Standard JobSpy call based on what i want eg indeed, glassdoor, linkedin etc.
    jobs = scrape_jobs(
        site_name=["linkedin","glassdoor"],
        search_term=term,
        location=location,
        results_wanted=15,
        country_indeed=country
    )

    # Convert DataFrame to JSON for Java to consume
    return jobs.to_json(orient='records')

if __name__ == "__main__":
    search_term = sys.argv[1] if len(sys.argv) > 1 else ""
    search_location = sys.argv[2] if len(sys.argv) > 2 else ""
    try:
        print(fetch_jobs(search_term, search_location))
    except Exception as exc:
        # Always emit valid JSON so callers do not crash on parse errors.
        print(json.dumps({"error": str(exc), "jobs": []}))
