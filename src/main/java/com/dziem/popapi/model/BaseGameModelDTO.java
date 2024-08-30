package com.dziem.popapi.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BaseGameModelDTO {
    private String name;
    private String comparableValue;
    private String comparableValueLabel;
    private String imageUrl;
}
