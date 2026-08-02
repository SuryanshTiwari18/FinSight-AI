package com.vedansh.smssyncserver.controller;

import com.vedansh.smssyncserver.dto.SmsRequest;
import com.vedansh.smssyncserver.dto.SmsResponse;
import com.vedansh.smssyncserver.service.SmsService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sms")
@RequiredArgsConstructor
public class SmsController {

    private final SmsService service;

    @PostMapping
    public ResponseEntity<SmsResponse> save(
            @RequestBody SmsRequest request) {

        service.save(request);

        return ResponseEntity.ok(
                new SmsResponse(
                        true,
                        "SMS Saved"
                )
        );
    }
}