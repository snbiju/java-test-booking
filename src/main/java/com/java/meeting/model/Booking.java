package com.java.meeting.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Duration;
import java.time.LocalTime;

public class Booking implements Comparable<Booking>{
    @JsonProperty("emp_id")
    private final String employeeId;
    @JsonProperty("start_time")
    @JsonFormat(pattern="HH:mm")
    private final LocalTime requestStartTime;
    @JsonProperty("end_time")
    @JsonFormat(pattern="HH:mm")
    private final LocalTime requestFinishTime;

    public Booking(String employeeId, LocalTime startTime, LocalTime finishTime) {
        this.employeeId = employeeId;
        this.requestStartTime = startTime;
        this.requestFinishTime = finishTime;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public LocalTime getRequestStartTime() {
        return requestStartTime;
    }

    public LocalTime getRequestFinishTime() {
        return requestFinishTime;
    }

    @Override
    public int compareTo(Booking booking) {
        LocalTime localStartTime = LocalTime.of(requestStartTime.getHour(),requestStartTime.getMinute());
        LocalTime localFinishTime = LocalTime.of(requestFinishTime.getHour(),requestFinishTime.getMinute());
        LocalTime compareStartTime = LocalTime.of(booking.getRequestStartTime().getHour(), booking.getRequestStartTime().getMinute());
        LocalTime compareFinishTime= LocalTime.of(booking.getRequestFinishTime().getHour(), booking.getRequestFinishTime().getMinute());
        long timeDiff = Duration.between(localStartTime,localFinishTime).toMinutes();
        long compareDiff=Duration.between(compareStartTime,compareFinishTime).toMinutes();
        if(timeDiff==compareDiff){
            return 0;
        }else if(timeDiff>compareDiff){
            return this.getRequestStartTime().compareTo(booking.getRequestStartTime());
        }else{
        return this.getRequestStartTime().compareTo(booking.getRequestStartTime());
    }
    }

}
