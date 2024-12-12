package com.dziem.popapi.model.webpage;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
public class DailyActiveUsers {
    @Id
    private LocalDate day;
    private Integer activeUsers;
}
