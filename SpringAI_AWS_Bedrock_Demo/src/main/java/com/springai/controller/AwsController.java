package com.springai.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springai.dto.Prompt;
import com.springai.service.AwsService;

@RestController
@RequestMapping("/api/assist")
public class AwsController {

    @Autowired
    private AwsService awsService;

    @PostMapping(value = "/public/ask")
    public ResponseEntity<String> askAssistant(@RequestBody Prompt prompt) {

        String response = awsService.askAssistant(prompt);

        return new ResponseEntity<String>(response, HttpStatus.OK);
    }

}
