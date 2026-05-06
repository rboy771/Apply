package com.apply.model;

public enum ApplicationStatus {
    APPLIED("Applied"),
    PHONE_SCREEN("Phone Screen"),
    INTERVIEW("Interview"),
    TECHNICAL("Technical Assessment"),
    OFFER("Offer"),
    ACCEPTED("Accepted"),
    REJECTED("Rejected"),
    WITHDRAWN("Withdrawn");

    private final String displayName;

    ApplicationStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
