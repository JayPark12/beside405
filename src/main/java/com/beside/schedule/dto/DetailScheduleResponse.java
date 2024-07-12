package com.beside.schedule.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springdoc.webmvc.core.fn.SpringdocRouteBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class DetailScheduleResponse {
    private String scheduleId;
    private String mountainName;
    private String courseName;
    private LocalDateTime scheduleDate;
    private BigDecimal mountainHigh;
    private String mountainLevel;
    private String mountainImg;
}
