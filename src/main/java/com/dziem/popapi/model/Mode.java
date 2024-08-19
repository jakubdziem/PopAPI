package com.dziem.popapi.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Mode {
    @Id
    @Column(name="mode_name")
    private String modeName;
}
