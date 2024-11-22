package com.dziem.popapi.model.webpage;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyUsersSummed {
    @Id
    private LocalDate day;
    private int guestUsers;
    private int googleOrEmailUsers;
}
