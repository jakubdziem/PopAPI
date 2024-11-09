package com.dziem.popapi.model.webpage;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
@Data
@Entity
@Builder
@RequiredArgsConstructor
public class WeeklyUsersSummed {
    @Id
    private LocalDate weekStartDate;
    private int guestUsers;
    private int googleOrEmailUsers;

}
