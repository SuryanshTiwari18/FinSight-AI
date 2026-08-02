package com.vedansh.smssyncserver.repository;

import com.vedansh.smssyncserver.entity.SmsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SmsRepository
        extends JpaRepository<SmsEntity, Long> {
}