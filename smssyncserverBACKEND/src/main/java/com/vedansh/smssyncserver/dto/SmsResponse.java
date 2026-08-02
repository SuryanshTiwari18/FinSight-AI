package com.vedansh.smssyncserver.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SmsResponse {

    private boolean success;
    private String message;
}