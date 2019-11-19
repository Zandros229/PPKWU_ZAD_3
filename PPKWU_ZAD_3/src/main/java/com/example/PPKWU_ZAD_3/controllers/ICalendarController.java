package com.example.PPKWU_ZAD_3.controllers;

import com.example.PPKWU_ZAD_3.services.ICalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/")
public class ICalendarController {

    @Autowired
    private ICalendarService iCalendarService;

    @GetMapping("calendar/thisMonth")
    public ResponseEntity<Resource> thisMonth() throws IOException {
        File file = iCalendarService.generateThisMonth();
        Resource fileSystemResource = new FileSystemResource(file);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("text/calendar"))
                .body(fileSystemResource);
    }

    @GetMapping("calendar/nextMonth")
    public ResponseEntity<Resource> nextMonth() throws IOException {
        File file = iCalendarService.generateNextMonth();
        Resource fileSystemResource = new FileSystemResource(file);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("text/calendar"))
                .body(fileSystemResource);
    }

}
