package com.dziem.popapi.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
@Data
@Entity
public class Driver implements Comparable<Driver>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Float score;
    private String imageUrl;
    private Integer tier;
    private String imageSource;
    private String imageSourceShort;

    @Override
    public int compareTo(Driver o) {
        return this.getScore().compareTo(o.getScore());
    }
}
