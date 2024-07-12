package com.beside.schedule.service;

import com.beside.mountain.domain.MntiEntity;
import com.beside.mountain.repository.MntiRepository;
import com.beside.mountain.service.MountainService;
import com.beside.schedule.domain.HikeSchedule;
import com.beside.schedule.dto.CreateScheduleRequest;
import com.beside.schedule.dto.DetailScheduleResponse;
import com.beside.schedule.dto.ModifyScheduleRequest;
import com.beside.schedule.dto.ScheduleResponse;
import com.beside.schedule.repository.HikeScheduleRepository;
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


}
