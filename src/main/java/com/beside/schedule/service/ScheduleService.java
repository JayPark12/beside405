package com.beside.schedule.service;

import com.beside.mountain.domain.MntiEntity;
import com.beside.mountain.repository.MntiRepository;
import com.beside.schedule.domain.HikeSchedule;
import com.beside.schedule.dto.CreateScheduleRequest;
import com.beside.schedule.dto.ModifyScheduleRequest;
import com.beside.schedule.dto.ScheduleResponse;
import com.beside.schedule.repository.HikeScheduleRepository;
import com.beside.user.domain.UserEntity;
import com.beside.user.exception.UserErrorInfo;
import com.beside.user.exception.UserException;
import com.beside.user.repository.UserRepository;
import com.beside.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.converters.ResponseSupportConverter;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ScheduleService {
    private final UserRepository userRepository;
    private final HikeScheduleRepository hikeScheduleRepository;
    private final MntiRepository mntiRepository;


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
        response.setCourse("코스");
        return response;
    }


    public String createSchedule(String userId, CreateScheduleRequest request) {
        HikeSchedule hikeSchedule = HikeSchedule.builder()
                .scheduleId(CommonUtil.getCurrentTime())
                .userId(userId)
                .scheduleName("일정 등록 테스트")
                .scheduleDate(CommonUtil.getDateTime(request.getScheduleDate()))
                .mountainId(request.getMountainId())
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


    public String modifySchedule(String userId, ModifyScheduleRequest request) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new UserException(UserErrorInfo.NOT_FOUND_USER));
        HikeSchedule schedule = hikeScheduleRepository.findById(request.getScheduleId()).orElseThrow();
        schedule.updateSchedule(request);
        hikeScheduleRepository.save(schedule);
        return schedule.getScheduleId();
    }


    public String deleteSchedule(String userId, String scheduleId) {
        HikeSchedule hikeSchedule = hikeScheduleRepository.findByUserIdAndScheduleId(userId, scheduleId).orElseThrow();
        hikeSchedule.deleteSchedule();
        hikeScheduleRepository.save(hikeSchedule);
        return hikeSchedule.getScheduleId();
    }
}
