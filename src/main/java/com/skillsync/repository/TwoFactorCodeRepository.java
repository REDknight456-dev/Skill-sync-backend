package com.skillsync.repository;

import com.skillsync.entity.TwoFactorCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.Optional;

public interface TwoFactorCodeRepository extends JpaRepository<TwoFactorCode, Long> {
    Optional<TwoFactorCode> findFirstByUserEmailAndCodeAndConsumedFalseAndExpiresAtAfter(String email, String code, Instant now);
    void deleteByUserEmailOrExpiresAtBefore(String email, Instant expiresBefore);
}
