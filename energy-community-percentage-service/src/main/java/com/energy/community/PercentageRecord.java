package com.energy.community;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "percentage_records")
public class PercentageRecord {

	@Id
	private LocalDateTime hour;

	private double gridPortion;
	private double communityDepleted;

	public PercentageRecord() {
	}

	public PercentageRecord(LocalDateTime hour, double gridPortion, double communityDepleted) {
		this.hour = hour;
		this.gridPortion = gridPortion;
		this.communityDepleted = communityDepleted;
	}

	public LocalDateTime getHour() {
		return hour;
	}

	public void setHour(LocalDateTime hour) {
		this.hour = hour;
	}

	public double getGridPortion() {
		return gridPortion;
	}

	public void setGridPortion(double gridPortion) {
		this.gridPortion = gridPortion;
	}

	public double getCommunityDepleted() {
		return communityDepleted;
	}

	public void setCommunityDepleted(double communityDepleted) {
		this.communityDepleted = communityDepleted;
	}
}
