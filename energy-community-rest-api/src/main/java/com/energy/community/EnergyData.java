package com.energy.community.restapi.model;

import java.time.LocalDateTime;

/**
 * Repräsentiert Energiedaten zu einem bestimmten Zeitpunkt.
 * Enthält Informationen über die produzierte und konsumierte Energie.
 */
public class EnergyData {

    /** Zeitpunkt der Messung */
    private LocalDateTime timestamp;

    /** Menge der produzierten Energie in kWh */
    private double produced;

    /** Menge der verbrauchten Energie in kWh */
    private double consumed;

    /**
     * Erstellt ein neues EnergyData-Objekt mit Zeitstempel, Produktion und Verbrauch.
     *
     * @param timestamp Zeitpunkt der Messung
     * @param produced  produzierte Energiemenge
     * @param consumed  verbrauchte Energiemenge
     */
    public EnergyData(LocalDateTime timestamp, double produced, double consumed) {
        this.timestamp = timestamp;
        this.produced = produced;
        this.consumed = consumed;
    }

    /**
     * Gibt den Zeitpunkt der Energiedaten zurück.
     *
     * @return Zeitstempel der Messung
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * Gibt die produzierte Energiemenge zurück.
     *
     * @return produzierte Energie in kWh
     */
    public double getProduced() {
        return produced;
    }

    /**
     * Gibt die verbrauchte Energiemenge zurück.
     *
     * @return verbrauchte Energie in kWh
     */
    public double getConsumed() {
        return consumed;
    }
}
