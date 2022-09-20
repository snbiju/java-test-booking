package com.java.meeting.process;

import com.java.meeting.exception.InvalidInputException;
import com.java.meeting.model.Booking;
import com.java.meeting.model.MeetingsSetup;
import com.java.meeting.util.MeetingScheduleFormatter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
public class BookingSchedulerProcessTest {
    private String meetingRequest;

    private MeetingSchedulerProcess meetingSchedulerProcess;
    private MeetingScheduleFormatter outputGenerator;

    @BeforeEach
    public void setUp() {
        meetingSchedulerProcess = new MeetingSchedulerProcess();
        outputGenerator = new MeetingScheduleFormatter(new MeetingSchedulerProcess());

    }

    @Test
    public void shouldParseOfficeHours() throws InvalidInputException {
        meetingRequest = "0900 1730\n";
        MeetingsSetup bookings = meetingSchedulerProcess.schedule(meetingRequest);
        assertEquals(bookings.getOfficeStartTime().getHour(), 9);
        assertEquals(bookings.getOfficeStartTime().getMinute(), 0);
        assertEquals(bookings.getOfficeFinishTime().getHour(), 17);
        assertEquals(bookings.getOfficeFinishTime().getMinute(), 30);
    }

    @Test
    public void shouldParseMeetingRequest() throws InvalidInputException {
        meetingRequest = "0900 1730\n" + "2011-03-17 10:17:06 EMP001\n"
                + "2011-03-21 09:00 2\n";
        MeetingsSetup bookings = meetingSchedulerProcess.schedule(meetingRequest);

        LocalDate meetingDate =  LocalDate.of(2011, 3, 21);

        assertEquals(1, bookings.getMeetings().get(meetingDate).size());
        Booking booking = bookings.getMeetings().get(meetingDate)
                .toArray(new Booking[0])[0];
        assertEquals("EMP001", booking.getEmployeeId());
        assertEquals(9, booking.getRequestStartTime().getHour());
        assertEquals(0, booking.getRequestStartTime().getMinute());
        assertEquals(11, booking.getRequestFinishTime().getHour());
        assertEquals(0, booking.getRequestFinishTime().getMinute());
    }

    @Test
    public void noPartOfMeetingMayFallOutsideOfficeHours() throws InvalidInputException {
        meetingRequest = "0900 1730\n" + "2011-03-15 17:29:12 EMP005\n"
                + "2011-03-21 16:00 3\n";
        MeetingsSetup bookings = meetingSchedulerProcess.schedule(meetingRequest);
        String expectedMessage = "EmployeeId:: EMP005 has requested booking which is outside office hour.";
        assertEquals(0,bookings.getMeetings().size());


    }

    @Test
    public void shouldProcessMeetingsInChronologicalOrderOfSubmission() throws InvalidInputException {
        meetingRequest = "0900 1730\n" + "2011-03-17 10:17:06 EMP001\n"
                + "2011-03-21 09:00 2\n" + "2011-03-16 12:34:56 EMP002\n"
                + "2011-03-21 09:00 2\n";

        MeetingsSetup bookings = meetingSchedulerProcess.schedule(meetingRequest);

        LocalDate meetingDate =  LocalDate.of(2011, 3, 21);

        assertEquals(1, bookings.getMeetings().get(meetingDate).size());
        Booking booking = bookings.getMeetings().get(meetingDate)
                .toArray(new Booking[0])[0];
        assertEquals("EMP001", booking.getEmployeeId());
        assertEquals(9, booking.getRequestStartTime().getHour());
        assertEquals(0, booking.getRequestStartTime().getMinute());
        assertEquals(11, booking.getRequestFinishTime().getHour());
        assertEquals(0, booking.getRequestFinishTime().getMinute());
    }

    @Test
    public void shouldGroupMeetingsChronologically() throws InvalidInputException {
        meetingRequest = "0900 1730\n" + "2011-03-17 10:17:06 EMP004\n"
                + "2011-03-22 16:00 1\n" + "2011-03-16 09:28:23 EMP003\n"
                + "2011-03-22 14:00 2\n";

        MeetingsSetup bookings = meetingSchedulerProcess.schedule(meetingRequest);
        LocalDate meetingDate =  LocalDate.of(2011, 3, 22);

        assertEquals(1, bookings.getMeetings().size());
        assertEquals(2, bookings.getMeetings().get(meetingDate).size());
        Booking[] meetings = bookings.getMeetings().get(meetingDate)
                .toArray(new Booking[0]);

        assertEquals("EMP003", meetings[0].getEmployeeId());
        assertEquals(14, meetings[0].getRequestStartTime().getHour());
        assertEquals(0, meetings[0].getRequestStartTime().getMinute());
        assertEquals(16, meetings[0].getRequestFinishTime().getHour());
        assertEquals(0, meetings[0].getRequestFinishTime().getMinute());

        assertEquals("EMP004", meetings[1].getEmployeeId());
        assertEquals(16, meetings[1].getRequestStartTime().getHour());
        assertEquals(0, meetings[1].getRequestStartTime().getMinute());
        assertEquals(17, meetings[1].getRequestFinishTime().getHour());
        assertEquals(0, meetings[1].getRequestFinishTime().getMinute());
    }

    @Test
    public void meetingsShouldNotOverlap() throws InvalidInputException {
        meetingRequest = "0900 1730\n" + "2011-03-17 10:17:06 EMP001\n"
                + "2011-03-21 09:00 2\n" + "2011-03-16 12:34:56 EMP002\n"
                + "2011-03-21 10:00 1\n";

        MeetingsSetup bookings = meetingSchedulerProcess.schedule(meetingRequest);
        LocalDate meetingDate =  LocalDate.of(2011, 3, 21);

        assertEquals(1, bookings.getMeetings().size());
        assertEquals(1, bookings.getMeetings().get(meetingDate).size());
        Booking[] meetings = bookings.getMeetings().get(meetingDate)
                .toArray(new Booking[0]);
        assertEquals("EMP001", meetings[0].getEmployeeId());
        assertEquals(9, meetings[0].getRequestStartTime().getHour());
        assertEquals(0, meetings[0].getRequestStartTime().getMinute());
        assertEquals(11, meetings[0].getRequestFinishTime().getHour());
        assertEquals(0, meetings[0].getRequestFinishTime().getMinute());
    }

    @Test
    public void emptyInputDataShouldEndWithNull() {
        meetingRequest = null;
        Exception exception = assertThrows(InvalidInputException.class,()-> meetingSchedulerProcess.schedule(meetingRequest));
        String expectedMessage ="Invalid boardroom booking request. Please enter valid input";
        String actualMessage= exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }



    @Test
    public void shouldPrintMeetingSchedule() throws InvalidInputException {
        String meetingRequest = "0900 1730\n" + "2011-03-17 10:17:06 EMP001\n"
                + "2011-03-21 09:00 2\n" + "2011-03-16 12:34:56 EMP002\n"
                + "2011-03-21 09:00 2\n" + "2011-03-16 09:28:23 EMP003\n"
                + "2011-03-22 14:00 2\n" + "2011-03-17 10:17:06 EMP004\n"
                + "2011-03-22 16:00 1\n" + "2011-03-15 17:29:12 EMP005\n"
                + "2011-03-21 16:00 1\n";

        String actualOutput = outputGenerator.print(meetingRequest);

        String expectedOutput = "2011-03-21\n" + "9:00 11:00 EMP001\n"
                +"16:00 17:00 EMP005\n" +"2011-03-22\n"
                +"14:00 16:00 EMP003\n" +"16:00 17:00 EMP004\n";


        assertEquals(expectedOutput,actualOutput);


    }
}
