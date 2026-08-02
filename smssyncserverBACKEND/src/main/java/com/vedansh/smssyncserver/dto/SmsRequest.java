package com.vedansh.smssyncserver.dto;

import lombok.Data;

@Data
public class SmsRequest {

    private String sender;

    private String message;

    private long timestamp;
}