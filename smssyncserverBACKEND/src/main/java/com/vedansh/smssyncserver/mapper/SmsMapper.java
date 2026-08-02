package com.vedansh.smssyncserver.mapper;

import com.vedansh.smssyncserver.dto.SmsRequest;
import com.vedansh.smssyncserver.entity.SmsEntity;

import java.time.LocalDateTime;

public class SmsMapper {

    public static SmsEntity toEntity(SmsRequest request) {

        return SmsEntity.builder()
                .sender(request.getSender())
                .message(request.getMessage())
                .timestamp(request.getTimestamp())
                .createdAt(LocalDateTime.now())
                .build();
    }

}