package com.java.meeting.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;
import java.util.Set;

public class MeetingsSetup {

    private final LocalTime officeStartTime;

    private final LocalTime officeFinishTime;

    private final Map<LocalDate, Set<Booking>> meetings;

    public MeetingsSetup(LocalTime officeStartTime,
                         LocalTime officeFinishTime, Map<LocalDate, Set<Booking>> meetings) {
        this.officeStartTime = officeStartTime;
        this.officeFinishTime = officeFinishTime;
        this.meetings = meetings;
    }

    public LocalTime getOfficeStartTime() {
        return officeStartTime;
    }

    public LocalTime getOfficeFinishTime() {
        return officeFinishTime;
    }

    public Map<LocalDate, Set<Booking>> getMeetings() {
        return meetings;
    }
}
