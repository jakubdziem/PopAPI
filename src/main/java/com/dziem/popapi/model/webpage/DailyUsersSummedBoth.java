package com.dziem.popapi.model.webpage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
@Data
@Builder
@AllArgsConstructor
public class DailyUsersSummedBoth {
    private LocalDate day;
    private int numberOfUsers;
}
