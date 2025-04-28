package com.dziem.popapi.dto.webpage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
@Data
@Builder
@AllArgsConstructor
public class DailyUsersSummedBothDTO {
    private LocalDate day;
    private int numberOfUsers;
}
