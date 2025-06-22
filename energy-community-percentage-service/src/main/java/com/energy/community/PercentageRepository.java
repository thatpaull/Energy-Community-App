package com.energy.community;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;

@Repository
public interface PercentageRepository extends JpaRepository<PercentageRecord, LocalDateTime> {
}
