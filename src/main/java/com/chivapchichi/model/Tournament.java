package com.chivapchichi.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Calendar;

@Data
public class Tournament {

    private int id;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private Calendar date;

    private int ratingRange;

    private String address;

    private String phone;

    private String organizerName;

    private double cost;

    private int max;

    private String name;

    private String division;

    public Tournament() {
    }

    public Tournament(Calendar date, int ratingRange, String address, String phone, String organizerName, double cost, int max, String name, String division) {
        this.date = date;
        this.ratingRange = ratingRange;
        this.address = address;
        this.phone = phone;
        this.organizerName = organizerName;
        this.cost = cost;
        this.max = max;
        this.name = name;
        this.division = division;
    }
}
