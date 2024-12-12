package com.dziem.popapi.model.webpage;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
public class WeeklyActiveUsers {
    @Id
    private LocalDate weekStartDate;
    private Integer activeUsers;
}
