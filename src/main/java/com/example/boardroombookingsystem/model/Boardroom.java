package com.example.boardroombookingsystem.model;

import java.time.LocalTime;
import java.util.List;

public class Boardroom {
    private final LocalTime startTime;
    private final LocalTime endTime;
    private List<Meeting> meetings;

    public Boardroom(final LocalTime startTime, final LocalTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public LocalTime getStartTime() {
        return this.startTime;
    }

    public LocalTime getEndTime() {
        return this.endTime;
    }

    public List<Meeting> getMeetings() {
        return meetings;
    }

    public void setMeetings(List<Meeting> meetings) {
        this.meetings = meetings;
    }
}
