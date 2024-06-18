package com.beside.schedule.controller;

import com.beside.schedule.dto.CreateScheduleRequest;
import com.beside.schedule.dto.ModifyScheduleRequest;
import com.beside.schedule.dto.ScheduleResponse;
import com.beside.schedule.service.ScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/schedule")
@Tag(name = "3.일정", description = "일정 관련 API")
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleService scheduleService;

    //내 일정 리스트 보기
    @GetMapping("/mySchedule")
    @Operation(summary = "내 일정 보기", description = "등록된 일정 리스트를 볼 수 있습니다.")
    public ResponseEntity<?> mySchedule() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        List<ScheduleResponse> response = scheduleService.mySchedule(userId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create")
    @Operation(summary = "일정 등록", description = "등산 일정을 등록할 수 있습니다.")
    public ResponseEntity<?> createSchedule(@RequestBody CreateScheduleRequest request) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        String response = scheduleService.createSchedule(userId, request);
        return ResponseEntity.ok("일정이 등록되었습니다. id : " + response);
    }

    //일정 수정 -> 수정 가능 컬럼 : 산이름, 인원수, 날짜(시간)
    @PatchMapping("/update")
    public ResponseEntity<?> modifySchedule(@RequestBody ModifyScheduleRequest request) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        String response = scheduleService.modifySchedule(userId, request);
        return ResponseEntity.ok("일정이 수정되었습니다. id : " + response);
    }

    //일정 삭제
    @PatchMapping("/delete/{scheduleId}")
    public ResponseEntity<?> deleteSchedule(@PathVariable String scheduleId) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        String response = scheduleService.deleteSchedule(userId, scheduleId);
        return ResponseEntity.ok("일정이 삭제되었습니다. id : " + response);
    }

    //일정 상세보기
    @GetMapping("/mySchedule/{scheduleId}")
    public ResponseEntity<?> detailSchedule(@PathVariable String scheduleId) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        return null;
    }


}
