package com.energy.community;

import java.io.Serializable;
import java.time.LocalDateTime;


public class EnergyMessage implements Serializable {

    private String source;
    private double amount;
    private LocalDateTime hour;

    public EnergyMessage() {
    }

    public EnergyMessage(String source, double amount, LocalDateTime hour) {
        this.source = source;
        this.amount = amount;
        this.hour   = hour;
    }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public LocalDateTime getHour() { return hour; }
    public void setHour(LocalDateTime hour) { this.hour = hour; }
}
