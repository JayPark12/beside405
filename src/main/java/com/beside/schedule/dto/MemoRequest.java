package com.beside.schedule.dto;

import lombok.Data;

@Data
public class MemoRequest {
    private String text;
    private boolean checked;
}
