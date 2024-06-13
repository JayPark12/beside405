package com.beside.schedule.service;

import com.beside.schedule.domain.HikeSchedule;
import com.beside.schedule.dto.CreateScheduleRequest;
import com.beside.schedule.repository.HikeScheduleRepository;
import com.beside.user.domain.UserEntity;
import com.beside.user.exception.UserErrorInfo;
import com.beside.user.exception.UserException;
import com.beside.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final UserRepository userRepository;
    private final HikeScheduleRepository hikeScheduleRepository;

    public int createSchedule(String userId, CreateScheduleRequest request) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new UserException(UserErrorInfo.NOT_FOUND_USER));

        HikeSchedule hikeSchedule = HikeSchedule.builder().userId(user.getId())
                .scheduleName("일정 등록 테스트")
                .scheduleDate(getDateTime(request.getScheduleDate()))
                .mountain(request.getMountainId())
                .memberCount(request.getMemberCount())
                .createDate(LocalDateTime.now())
                .delYn("N").build();
        hikeScheduleRepository.save(hikeSchedule);
        return hikeSchedule.getScheduleId();
    }

    public LocalDateTime getDateTime(String inputDate) {
        int year = Integer.parseInt(inputDate.substring(0, 4));
        int month = Integer.parseInt(inputDate.substring(4, 6));
        int day = Integer.parseInt(inputDate.substring(6, 8));
        int hour = Integer.parseInt(inputDate.substring(8, 10));
        int minute = Integer.parseInt(inputDate.substring(10, 12));

        return LocalDateTime.of(year, month, day, hour, minute);
    }

}
