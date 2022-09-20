package com.java.meeting.service;

import com.java.meeting.exception.InvalidInputException;
import com.java.meeting.model.Booking;
import com.java.meeting.model.MeetingsSetup;
import com.java.meeting.model.Schedule;
import com.java.meeting.process.MeetingSchedulerProcess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

@Service
public class MeetingSchedulerService {
    @Autowired
    MeetingSchedulerProcess process;

    public Schedule[] schedule(String meetingRequest) throws InvalidInputException {
        MeetingsSetup setup = process.schedule(meetingRequest);
        Schedule[] schedules = new Schedule[setup.getMeetings().size()];
        int count = 0;
        for (Map.Entry<LocalDate, Set<Booking>> localDateSetEntry : setup.getMeetings().entrySet()) {
            Schedule schedule = new Schedule();
            Set<Booking> booking = localDateSetEntry.getValue();
            schedule.data = localDateSetEntry.getKey().toString();
            schedule.bookings = booking;
            schedules[count] = schedule;
            count++;
        }
        return schedules;
    }
}
