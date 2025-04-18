package com.energy.community.restapi.model;
import java.time.LocalDateTime;
public class EnergyData {
    private LocalDateTime timestamp;
    private double produced;
    private double consumed;
    public EnergyData(LocalDateTime timestamp, double produced, double consumed) {
        this.timestamp = timestamp;
        this.produced = produced;
        this.consumed = consumed;
    }
    public LocalDateTime getTimestamp() { return timestamp; }
    public double getProduced() { return produced; }
    public double getConsumed() { return consumed; }
}