package com.java.meeting.util;

import com.java.meeting.exception.InvalidInputException;
import com.java.meeting.model.Booking;
import com.java.meeting.process.MeetingSchedulerProcess;
import com.java.meeting.model.MeetingsSetup;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.util.Map;
import java.util.Set;

public class MeetingScheduleFormatter {
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final DateTimeFormatter timeFormatter= DateTimeFormatter.ofPattern("H:mm");

    private final MeetingSchedulerProcess meetingSchedulerProcess;

    public MeetingScheduleFormatter(MeetingSchedulerProcess meetingSchedulerProcess) {
        this.meetingSchedulerProcess = meetingSchedulerProcess;
    }

    public String print(String meetingRequest) throws InvalidInputException {
        MeetingsSetup meetingsSetupBooked = meetingSchedulerProcess.schedule(meetingRequest);

        return buildMeetingScheduleString(meetingsSetupBooked);

    }

    private String buildMeetingScheduleString(MeetingsSetup meetingsSetupBooked) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<LocalDate, Set<Booking>> meetingEntry : meetingsSetupBooked.getMeetings().entrySet()) {

            LocalDate meetingDate = meetingEntry.getKey();
            sb.append(dateFormatter.format(meetingDate)).append("\n");
            Set<Booking> bookings = meetingEntry.getValue();
            for (Booking booking : bookings) {
                sb.append(timeFormatter.format(booking.getRequestStartTime())).append(" ");
                sb.append(timeFormatter.format(booking.getRequestFinishTime())).append(" ");
                sb.append(booking.getEmployeeId()).append("\n");
            }

        }
        return sb.toString();
    }
}
