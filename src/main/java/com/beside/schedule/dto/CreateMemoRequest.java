package com.beside.schedule.dto;

import lombok.Data;

@Data
public class CreateMemoRequest {
    private String scheduleId;
    private String memoContent;
}
