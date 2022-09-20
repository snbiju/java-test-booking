package com.java.meeting.controller;

import com.java.meeting.exception.InvalidInputException;
import com.java.meeting.model.Schedule;
import com.java.meeting.service.MeetingSchedulerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MeetingSchedulerController {
    @Autowired
    MeetingSchedulerService service;
    @PostMapping(path= "/meeting", produces = "application/json")
    public ResponseEntity<Schedule[]> schedule(@RequestBody String request) throws InvalidInputException {
         ResponseEntity<Schedule[]> response;
        response = new ResponseEntity<Schedule[]>(service.schedule(request), HttpStatus.OK);
        return response;
    }
}
