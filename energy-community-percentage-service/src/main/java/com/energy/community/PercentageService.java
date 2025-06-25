package com.energy.community;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;


@Service
public class PercentageService {

	private final UsageRepository usageRepo;
	private final PercentageRepository percRepo;

	private static final DateTimeFormatter ISO = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

	public PercentageService(UsageRepository usageRepo,
							 PercentageRepository percRepo) {
		this.usageRepo = usageRepo;
		this.percRepo  = percRepo;
	}

	@RabbitListener(queues = RabbitConfig.USAGE_UPDATED_Q)
	public void handleUsageUpdated(String hourIso) {

		LocalDateTime hour = LocalDateTime.parse(hourIso, ISO);

		UsageRecord ur = usageRepo.findById(hour).orElse(null);
		if (ur == null) {
			System.out.printf("[!] No usage record found for %s%n", hour);
			return;
		}

		double produced = ur.getCommunityProduced();
		double used     = ur.getCommunityUsed();

		if (produced == 0) {
			System.out.printf("[!] Produced energy is zero for %s, skipping%n", hour);
			return;
		}

		double gridPortion        = Math.max(0, (used - produced) / used);
		double communityDepletion = Math.max(0, (produced - used) / produced);

		PercentageRecord pr = new PercentageRecord(hour, gridPortion, communityDepletion);

		percRepo.save(pr);

		percentageCalculationLogic(hour);
	}

	private void percentageCalculationLogic(LocalDateTime hour) {
		hour = hour.withMinute(0).withSecond(0).withNano(0);

		Optional<UsageRecord> usageOpt = usageRepo.findById(hour);

		if (usageOpt.isPresent()) {
			UsageRecord usage = usageOpt.get();

			double produced = usage.getCommunityProduced();
			double used = usage.getCommunityUsed();

			double communityDepleted = 0.0;
			double gridPortion = 1.0;

			if (used > 0) {
				communityDepleted = Math.min(produced, used) / used;
				gridPortion = (used - Math.min(produced, used)) / used;
			}

			PercentageRecord record = new PercentageRecord(hour, gridPortion, communityDepleted);
			percRepo.save(record);

			System.out.printf("[✓] Calculated for %s: depleted=%.2f, grid=%.2f%n", hour, communityDepleted, gridPortion);
		} else {
			System.out.printf("[!] No usage data found for %s%n", hour);
		}
	}

}
