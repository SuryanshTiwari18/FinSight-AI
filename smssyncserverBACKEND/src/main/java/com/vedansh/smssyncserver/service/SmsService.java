package com.vedansh.smssyncserver.service;

import com.vedansh.smssyncserver.dto.SmsRequest;
import com.vedansh.smssyncserver.entity.SmsEntity;
import com.vedansh.smssyncserver.mapper.SmsMapper;
import com.vedansh.smssyncserver.repository.SmsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SmsService {

    private final SmsRepository repository;

    public void save(SmsRequest request) {

        SmsEntity entity = SmsMapper.toEntity(request);

        repository.save(entity);
    }

}