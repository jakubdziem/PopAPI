package com.dziem.popapi.model.webpage;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDate;
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeeklyUsersSummed {
    @Id
    private LocalDate weekStartDate;
    private int guestUsers;
    private int googleOrEmailUsers;

}
