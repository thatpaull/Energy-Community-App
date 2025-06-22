package com.energy.community;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/energy")
public class EnergyController {

	@Autowired
	private UsageRepository usageRepository;

	@Autowired
	private PercentageRepository percentageRepository;

	@GetMapping("/current")
	public ResponseEntity<List<UsageRecord>> getCurrentHourData() {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime startOfCurrentHour = now.withMinute(0).withSecond(0).withNano(0);
		LocalDateTime startOfNextHour = startOfCurrentHour.plusHours(1);

		List<UsageRecord> records = usageRepository.findByHourBetween(startOfCurrentHour, startOfNextHour);
		return ResponseEntity.ok(records);
	}





	@GetMapping("/historical")
	public ResponseEntity<List<UsageRecord>> getHistoricalData(
			@RequestParam LocalDateTime start,
			@RequestParam LocalDateTime end) {

		List<UsageRecord> results = usageRepository.findByHourBetween(start, end);
		return ResponseEntity.ok(results);
	}
}
