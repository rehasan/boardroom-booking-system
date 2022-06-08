package com.example.boardroombookingsystem.model;

import java.time.LocalDateTime;

public class Meeting implements Comparable<Meeting> {
    private final LocalDateTime requestSubmissionDateTime;
    private final String employeeId;

    private LocalDateTime meetingStartDateTime;
    private LocalDateTime meetingEndDateTime;
    private Integer meetingDuration;

    public Meeting(final LocalDateTime requestSubmissionDateTime, final String employeeId) {
        this.requestSubmissionDateTime = requestSubmissionDateTime;
        this.employeeId = employeeId;
    }

    public Meeting(final LocalDateTime requestSubmissionDateTime, final String employeeId, final LocalDateTime meetingStartDateTime, final Integer meetingDuration) {
        this.requestSubmissionDateTime = requestSubmissionDateTime;
        this.employeeId = employeeId;
        this.meetingStartDateTime = meetingStartDateTime;
        this.meetingDuration = meetingDuration;

        if (this.meetingStartDateTime != null && this.meetingDuration != null) {
            calculateMeetingEndDateTime();
        }
    }

    public LocalDateTime getRequestSubmissionDateTime() {
        return this.requestSubmissionDateTime;
    }

    public String getEmployeeId() {
        return this.employeeId;
    }

    public LocalDateTime getMeetingStartDateTime() {
        return this.meetingStartDateTime;
    }

    public void setMeetingStartDateTime(LocalDateTime meetingStartTime) {
        this.meetingStartDateTime = meetingStartTime;
        if (this.meetingDuration != null) {
            calculateMeetingEndDateTime();
        }
    }

    public Integer getMeetingDuration() {
        return this.meetingDuration;
    }

    public void setMeetingDuration(Integer meetingDuration) {
        this.meetingDuration = meetingDuration;
        if (this.meetingStartDateTime != null) {
            calculateMeetingEndDateTime();
        }
    }

    public LocalDateTime getMeetingEndDateTime() {
        return this.meetingEndDateTime;
    }

    @Override
    public int compareTo(Meeting meeting) {
        return this.requestSubmissionDateTime.compareTo(meeting.requestSubmissionDateTime);
    }

    private void calculateMeetingEndDateTime() {
        this.meetingEndDateTime = this.meetingStartDateTime.plusHours(this.meetingDuration);
    }
}
