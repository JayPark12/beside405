package com.beside.schedule.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemoListResponse {
    private String memoId;
    private String scheduleId;
    private String content;
    private boolean checkStatus;
}
