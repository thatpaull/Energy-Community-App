package com.energy.community;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/usage")
public class UsageService {

	private final UsageRepository repo;
	private final RabbitTemplate rabbitTemplate;

	public UsageService(UsageRepository repo, RabbitTemplate rabbitTemplate) {
		this.repo = repo;
		this.rabbitTemplate = rabbitTemplate;
	}

	@RabbitListener(queues = RabbitConfig.ENERGY_DATA_Q)
	public void handle(EnergyMessage msg) {
		UsageRecord rec = repo.findById(msg.getHour())
			.orElse(new UsageRecord(msg.getHour()));

		if ("PRODUCER".equalsIgnoreCase(msg.getSource())) {
			rec.setCommunityProduced(rec.getCommunityProduced() + msg.getAmount());
		} else if ("USER".equalsIgnoreCase(msg.getSource())) {
			rec.setCommunityUsed(rec.getCommunityUsed() + msg.getAmount());
		}

		repo.save(rec);
		rabbitTemplate.convertAndSend(RabbitConfig.USAGE_UPDATED_Q, msg.getHour());
	}

	@GetMapping
	public List<UsageRecordPrinter.UsageRecordDTO> getAll() {
		return UsageRecordPrinter.toDtoList(repo.findAll());
	}

	@GetMapping("/summary")
	public Map<String, Double> getSummary() {
		List<UsageRecord> all = repo.findAll();

		double totalProduced = all.stream().mapToDouble(UsageRecord::getCommunityProduced).sum();
		double totalUsed = all.stream().mapToDouble(UsageRecord::getCommunityUsed).sum();
		double totalGrid = all.stream().mapToDouble(UsageRecord::getGridUsed).sum();

		double poolUsedPercent = totalProduced == 0 ? 0 : (totalUsed / totalProduced) * 100;
		double gridPortionPercent = totalUsed == 0 ? 0 : (totalGrid / totalUsed) * 100;

		Map<String, Double> summary = new HashMap<>();
		summary.put("poolUsedPercent", poolUsedPercent);
		summary.put("gridPortionPercent", gridPortionPercent);
		summary.put("totalProduced", totalProduced);
		summary.put("totalUsed", totalUsed);
		summary.put("totalGrid", totalGrid);

		return summary;
	}

}
