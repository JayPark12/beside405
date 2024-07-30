package com.beside.schedule.controller;

import com.beside.schedule.dto.*;
import com.beside.schedule.service.ScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;
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
        ScheduleIdResponse response = scheduleService.createSchedule(userId, request);
        return ResponseEntity.ok(response);
    }

    //일정 수정 -> 수정 가능 컬럼 : 산이름, 인원수, 날짜(시간)
    @PatchMapping("/update")
    @Operation(summary = "일정 수정", description = "등산 일정을 수정할 수 있습니다.")
    public ResponseEntity<?> modifySchedule(@RequestBody ModifyScheduleRequest request) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        ScheduleIdResponse response = scheduleService.modifySchedule(userId, request);
        return ResponseEntity.ok(response);
    }

    //일정 삭제
    @PatchMapping("/delete/{scheduleId}")
    @Operation(summary = "일정 삭제", description = "등록된 등산일정을 삭제할 수 있습니다.")
    public ResponseEntity<?> deleteSchedule(@PathVariable String scheduleId) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        ScheduleIdResponse response = scheduleService.deleteSchedule(userId, scheduleId);
        return ResponseEntity.ok(response);
    }

    //일정 상세보기
    @Operation(summary = "일정 상세 페이지", description = "등산 일정의 Id를 조회하여 상세페이지를 볼 수 있습니다.")
    @GetMapping("/mySchedule/{scheduleId}")
    public ResponseEntity<?> detailSchedule(@PathVariable String scheduleId) throws IOException, URISyntaxException {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        DetailScheduleResponse response = scheduleService.detailSchedule(userId, scheduleId);
        return ResponseEntity.ok(response);
    }


    //메모 리스트
    @Operation(summary = "메모 리스트", description = "등산 일정의 Id를 조회하여 해당 일정의 메모 리스트를 볼 수 있습니다.")
    @GetMapping("/memo/list/{scheduleId}")
    public ResponseEntity<?> scheduleMemoList(@PathVariable String scheduleId) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        List<MemoListResponse> response = scheduleService.getMemoList(userId, scheduleId);
        return ResponseEntity.ok(response);
    }

    //메모 등록
    @Operation(summary = "메모 등록", description = "일정 id에 매핑되는 메모를 생성합니다.")
    @PostMapping("/memo/create")
    public ResponseEntity<?> createMemo(@RequestBody List<CreateMemoRequest> request) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        List<CreateMemoResponse> response = scheduleService.createMemo(request, userId);
        return ResponseEntity.ok(response);
    }


    //메모 개별 수정
    @Operation(summary = "메모 개별 수정", description = "메모 Id를 조회하여 해당 메모를 수정할 수 있습니다.")
    @PatchMapping("/memo/update")
    public ResponseEntity<?> modifyMemo(@RequestBody UpdateMemoRequest request) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        String response = scheduleService.modifyMemo(request);
        return ResponseEntity.ok(response);
    }

    //메모 삭제
    @Operation(summary = "메모 개별 삭제", description = "메모 Id를 조회하여 해당 메모를 삭제할 수 있습니다.")
    @DeleteMapping("/memo/delete/{memoId}")
    public ResponseEntity<?> deleteMemo(@PathVariable String memoId) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        String response = scheduleService.deleteMemo(userId, memoId);
        return ResponseEntity.ok(response);
    }

    //메모 체크
    @Operation(summary = "메모 체크 수정", description = "메모 Id를 조회하여 해당 메모의 체크를 수정할 수 있습니다.")
    @PatchMapping("/memo/update/{memoId}")
    public ResponseEntity<?> checkMemo(@PathVariable String memoId) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        String response = scheduleService.checkMemo(userId, memoId);
        return ResponseEntity.ok(response);
    }


    //Todo : 초대장

    @Operation(summary = "초대장 수락", description = "초대장 수락 시 등산 일정의 멤버로 가입됩니다.")
    @PostMapping("/invite/join/{scheduleId}")
    public ResponseEntity<?> joinSchedule(@PathVariable String scheduleId) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        String response = scheduleService.joinSchedule(userId, scheduleId);
        return ResponseEntity.ok("일정에 가입되었습니다. id : " + response);
    }

    @Operation(summary = "초대받은 일정 나가기", description = "초대받은 일정에서 나갑니다.")
    @DeleteMapping("/invite/leave/{scheduleId}")
    public ResponseEntity<?> leaveSchedule(@PathVariable String scheduleId) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        String response = scheduleService.leaveSchedule(userId, scheduleId);
        return ResponseEntity.ok("일정이 삭제되었습니다. id : " + response);
    }



}
