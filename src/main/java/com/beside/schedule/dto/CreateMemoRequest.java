package com.beside.schedule.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class CreateMemoRequest {
    private String scheduleId;
    private String text;
    private boolean checked;
}
