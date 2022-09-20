package com.java.meeting.process;

import com.java.meeting.exception.InvalidInputException;
import com.java.meeting.model.Booking;
import com.java.meeting.model.MeetingsSetup;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static java.lang.Integer.parseInt;

@Component
public class MeetingSchedulerProcess{

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final String INVALID_INPUT= "Invalid boardroom booking request. Please enter valid input";

    public MeetingsSetup schedule(String meetingRequest) throws InvalidInputException{

        if (meetingRequest == null || meetingRequest.isEmpty()) {
            throw new InvalidInputException(INVALID_INPUT);
        }
        Map<LocalDate, Set<Booking>> meetings = new TreeMap<>();
        LocalTime officeStartTime=null;
        LocalTime officeFinishTime=null;
        try{
        String[] requestLines = meetingRequest.split("\n");

        String[] officeHoursTokens = requestLines[0].split(" ");
         officeStartTime =  LocalTime.of(
                parseInt(officeHoursTokens[0].substring(0, 2)),
                parseInt(officeHoursTokens[0].substring(2, 4)));
         officeFinishTime =  LocalTime.of(
                parseInt(officeHoursTokens[1].substring(0, 2)),
                parseInt(officeHoursTokens[1].substring(2, 4)));

        for (int i = 1; i < requestLines.length; i = i + 2) {

            String[] meetingSlotRequest = requestLines[i + 1].split(" ");
            LocalDate meetingDate = LocalDate.
                    parse(meetingSlotRequest[0]);
            dateFormatter.format(meetingDate);

            Booking booking = extractMeeting(requestLines[i],
                    officeStartTime, officeFinishTime, meetingSlotRequest);
            if(booking!=null)
            if (meetings.containsKey(meetingDate)) {
                if(IsMeetingTimeIsInsideExistingBooking(meetings.get(meetingDate),booking))
                meetings.get(meetingDate).add(booking);
            } else {
                Set<Booking> meetingsForDay = new TreeSet<>();
                meetingsForDay.add(booking);
                meetings.put(meetingDate, meetingsForDay);
            }
        }}catch (Exception e){
           throw new InvalidInputException(INVALID_INPUT);
       }

        return new MeetingsSetup(officeStartTime, officeFinishTime,
                meetings);

    }

    private Booking extractMeeting(String requestLine,
                                   LocalTime officeStartTime, LocalTime officeFinishTime,
                                   String[] meetingSlotRequest)  {
        String[] employeeRequest = requestLine.split(" ");
        String employeeId = employeeRequest[2];

        LocalTime meetingStartTime = LocalTime.parse(meetingSlotRequest[1]);

        LocalTime meetingFinishTime =  LocalTime.of(meetingStartTime.getHour(),meetingStartTime.getMinute()).plusHours(parseInt(meetingSlotRequest[2]));


        if (!IsMeetingTimeOutsideOfficeHours(officeStartTime, officeFinishTime,
                meetingStartTime, meetingFinishTime)) {

            return new Booking(employeeId, meetingStartTime, meetingFinishTime);
        }
        return null;
    }

    private boolean IsMeetingTimeOutsideOfficeHours(LocalTime officeStartTime,
                                                    LocalTime officeFinishTime, LocalTime meetingStartTime,
                                                    LocalTime meetingFinishTime) {
        return meetingStartTime.isBefore(officeStartTime)
                || meetingStartTime.isAfter(officeFinishTime)
                || meetingFinishTime.isAfter(officeFinishTime)
                || meetingFinishTime.isBefore(officeStartTime);
    }

    private boolean IsMeetingTimeIsInsideExistingBooking(Set<Booking> bookings,Booking booking) {
        for (Booking existBooking:bookings
             ) {

            if((booking.getRequestStartTime().isAfter(existBooking.getRequestStartTime())
                    && booking.getRequestStartTime().isBefore(existBooking.getRequestFinishTime()))
                    || ((booking.getRequestFinishTime().isAfter(existBooking.getRequestStartTime()))
                    &&(booking.getRequestFinishTime().isBefore(existBooking.getRequestFinishTime())))){
                return false;
            }
        }
        return true;
    }
}
