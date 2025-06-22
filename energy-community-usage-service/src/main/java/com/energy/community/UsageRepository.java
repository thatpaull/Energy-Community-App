package com.energy.community;

import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UsageRepository extends JpaRepository<UsageRecord, LocalDateTime> {
    List<UsageRecord> findByHourBetween(LocalDateTime start, LocalDateTime end);

}
