package com.beside.schedule.service;

import com.beside.mountain.domain.MntiEntity;
import com.beside.mountain.repository.MntiRepository;
import com.beside.mountain.service.MountainService;
import com.beside.schedule.domain.HikeSchedule;
import com.beside.schedule.domain.ScheduleMemo;
import com.beside.schedule.dto.*;
import com.beside.schedule.repository.HikeScheduleRepository;
import com.beside.schedule.repository.ScheduleMemoRepository;
import com.beside.user.domain.UserEntity;
import com.beside.user.exception.UserErrorInfo;
import com.beside.user.exception.UserException;
import com.beside.user.repository.UserRepository;
import com.beside.util.CommonUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.converters.ResponseSupportConverter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ScheduleService {
    private final HikeScheduleRepository hikeScheduleRepository;
    private final MntiRepository mntiRepository;
    private final MountainService mountainService;
    private final ScheduleMemoRepository scheduleMemoRepository;


    public List<ScheduleResponse> mySchedule(String userId) {
        return hikeScheduleRepository.findByUserIdAndDelYn(userId, "N").stream()
                .map(this::convertToScheduleResponse)
                .collect(Collectors.toList());
    }

    private ScheduleResponse convertToScheduleResponse(HikeSchedule entity) {
        ScheduleResponse response = new ScheduleResponse();
        response.setScheduleId(entity.getScheduleId());
        response.setMountain(getMountainName(entity.getMountainId()));
        response.setMemberCount(entity.getMemberCount());
        response.setScheduleDate(entity.getScheduleDate());
        response.setCourse(mountainService.getCourseNameByNo(entity.getCourseNo()));
        return response;
    }


    public String createSchedule(String userId, CreateScheduleRequest request) {
        HikeSchedule hikeSchedule = HikeSchedule.builder()
                .scheduleId(CommonUtil.getCurrentTime())
                .userId(userId)
                .scheduleDate(CommonUtil.getDateTime(request.getScheduleDate()))
                .mountainId(request.getMountainId())
                .courseNo(request.getCourseNo())
                .memberCount(request.getMemberCount())
                .createDate(LocalDateTime.now())
                .delYn("N").build();

        hikeScheduleRepository.save(hikeSchedule);
        return hikeSchedule.getScheduleId();
    }


    public String getMountainName(String mountainId) {
        MntiEntity mountain = mntiRepository.findByMntiListNo(mountainId).orElseThrow();
        return mountain.getMntiName();
    }


    @Transactional
    public String modifySchedule(String userId, ModifyScheduleRequest request) {
        HikeSchedule schedule = hikeScheduleRepository.findByUserIdAndScheduleId(userId, request.getScheduleId()).orElseThrow();
        schedule.updateSchedule(request);
        hikeScheduleRepository.save(schedule);
        return schedule.getScheduleId();
    }


    @Transactional
    public String deleteSchedule(String userId, String scheduleId) {
        HikeSchedule hikeSchedule = hikeScheduleRepository.findByUserIdAndScheduleId(userId, scheduleId).orElseThrow();
        hikeSchedule.deleteSchedule();
        hikeScheduleRepository.save(hikeSchedule);
        return hikeSchedule.getScheduleId();
    }

    public DetailScheduleResponse detailSchedule(String userId, String scheduleId) {
        HikeSchedule hikeSchedule = hikeScheduleRepository.findByUserIdAndScheduleId(userId, scheduleId).orElseThrow();
        MntiEntity mountain = mntiRepository.findByMntiInfo(hikeSchedule.getMountainId());
        return DetailScheduleResponse.builder()
                .scheduleId(scheduleId)
                .mountainName(getMountainName(hikeSchedule.getMountainId()))
                .courseName(mountainService.getCourseNameByNo(hikeSchedule.getCourseNo()))
                .scheduleDate(hikeSchedule.getScheduleDate())
                .mountainImg(null) // TODO
                .mountainHigh(mountain.getMntihigh())
                .mountainLevel(mountain.getMntiLevel()).build();
    }


    public List<MemoListResponse> getMemoList(String userId, String scheduleId) {
        List<MemoListResponse> list = new ArrayList<>();
        List<ScheduleMemo> entityList = scheduleMemoRepository.findByScheduleId(scheduleId);
        for(ScheduleMemo memo : entityList){
            MemoListResponse memoListResponse = new MemoListResponse();
            memoListResponse.setMemoId(memo.getMemoId());
            memoListResponse.setScheduleId(scheduleId);
            memoListResponse.setContent(memo.getContent());
            memoListResponse.setCheckStatus(memo.isCheckStatus());
            list.add(memoListResponse);
        }
        return list;
    }

    public String createMemo(CreateMemoRequest request, String userId) {
        try {
            ScheduleMemo memo = ScheduleMemo.builder()
                    .scheduleId(request.getScheduleId())
                    .memoId(CommonUtil.getMsgId())
                    .content(request.getMemoContent())
                    .createUser(userId)
                    .checkStatus(false)
                    .build();
            scheduleMemoRepository.save(memo);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
        return "메모 등록 완료";
    }


    @Transactional
    public String modifyMemo(UpdateMemoRequest request) {
        //메모 수정 권한 있는 지 체크
        try {
            ScheduleMemo memo = scheduleMemoRepository.findById(request.getMemoId()).orElseThrow();
            memo.updateMemo(request.getMemoContent());
            scheduleMemoRepository.save(memo);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
        return request.getMemoId();
    }

    @Transactional
    public String deleteMemo(String userId, String memoId) {
        //메모 삭제 권한 있는 지 체크
        try {
            ScheduleMemo memo = scheduleMemoRepository.findById(memoId).orElseThrow();
            scheduleMemoRepository.deleteById(memoId);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
        return memoId;
    }

    @Transactional
    public String checkMemo(String userId, String memoId) {
        //메모 수정 권한 있는 지 체크
        try {
            ScheduleMemo memo = scheduleMemoRepository.findById(memoId).orElseThrow();
            memo.updateCheckStatus(!memo.isCheckStatus());
            scheduleMemoRepository.save(memo);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
        return memoId;
    }


}
