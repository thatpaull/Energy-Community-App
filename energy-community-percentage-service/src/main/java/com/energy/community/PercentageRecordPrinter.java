package com.energy.community;

import java.text.DecimalFormat;
import java.util.List;

public class PercentageRecordPrinter {

	private static final DecimalFormat df = new DecimalFormat("#.00");

	public static void printRecords(List<PercentageRecord> records) {
		records.sort((r1, r2) -> r1.getHour().compareTo(r2.getHour()));

		for (PercentageRecord record : records) {
			System.out.println("Time: " + record.getHour());
			System.out.println("Grid Portion: " + df.format(record.getGridPortion()));
			System.out.println("Community Depleted: " + df.format(record.getCommunityDepleted()));
			System.out.println();
		}
	}
}
