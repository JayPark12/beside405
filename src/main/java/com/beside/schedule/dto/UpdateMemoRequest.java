package com.beside.schedule.dto;

import lombok.Data;

@Data
public class UpdateMemoRequest {
    private String memoId;
    private String memoContent;
}
