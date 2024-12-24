package com.teamsantos.easybarber.utils;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.teamsantos.easybarber.services.EstablishmentService;

@Component
public class Price {
    @Autowired
    private EstablishmentService establishmentService;
    private double value;

    public Price(double price, long establishmentServiceId, LocalDateTime dateTime) {
        this.value = establishmentService.getActualPrice(price, establishmentServiceId, dateTime);
    }

    @JsonCreator
    public Price(double price) {
        this.value = price;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.format("%.2f", value);
    }
}
