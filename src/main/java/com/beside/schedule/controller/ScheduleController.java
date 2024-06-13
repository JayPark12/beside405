package com.beside.schedule.controller;

import com.beside.schedule.dto.CreateScheduleRequest;
import com.beside.schedule.service.ScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/schedule")
@Tag(name = "3.일정", description = "일정 관련 API")
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleService scheduleService;

    @PostMapping("/create")
    @Operation(summary = "일정 등록", description = "등산 일정을 등록할 수 있습니다.")
    public ResponseEntity<?> createSchedule(@RequestBody CreateScheduleRequest request) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        int response = scheduleService.createSchedule(userId, request);
        return ResponseEntity.ok("일정이 등록되었습니다. id : " + response);
    }
}
