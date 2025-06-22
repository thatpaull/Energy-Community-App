package com.energy.community;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class UsageRecordPrinter {

	private static final DecimalFormat df;

	static {
		DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
		symbols.setDecimalSeparator(','); // Используем запятую вместо точки
		df = new DecimalFormat("#0.00", symbols);
	}

	public static void printUsageRecords(List<UsageRecord> records) {
		records.stream()
			.sorted(Comparator.comparing(UsageRecord::getHour))
			.forEach(record -> {
				System.out.println("Time: " + record.getHour());
				System.out.println("Community Produced: " + df.format(record.getCommunityProduced()));
				System.out.println("Community Used:     " + df.format(record.getCommunityUsed()));
				System.out.println("Grid Used:          " + df.format(record.getGridUsed()));
				System.out.println();
			});
	}

	public static List<UsageRecordDTO> toDtoList(List<UsageRecord> records) {
		return records.stream()
			.sorted(Comparator.comparing(UsageRecord::getHour))
			.map(UsageRecordDTO::new)
			.collect(Collectors.toList());
	}

	public static class UsageRecordDTO {
		public String hour;
		public String communityProduced;
		public String communityUsed;
		public String gridUsed;

		public UsageRecordDTO(UsageRecord r) {
			this.hour = r.getHour().toString();
			this.communityProduced = df.format(r.getCommunityProduced());
			this.communityUsed = df.format(r.getCommunityUsed());
			this.gridUsed = df.format(r.getGridUsed());
		}
	}
}
