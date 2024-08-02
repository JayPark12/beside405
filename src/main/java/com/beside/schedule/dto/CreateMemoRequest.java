package com.beside.schedule.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class CreateMemoRequest {
    private String scheduleId;
    private List<MemoRequest> memoRequest;
}
