package com.beside.schedule.service;

import com.beside.mountain.domain.MntiEntity;
import com.beside.mountain.dto.Course;
import com.beside.mountain.repository.MntiRepository;
import com.beside.mountain.service.MountainService;
import com.beside.schedule.domain.HikeSchedule;
import com.beside.schedule.domain.MemberId;
import com.beside.schedule.domain.ScheduleMember;
import com.beside.schedule.domain.ScheduleMemo;
import com.beside.schedule.dto.*;
import com.beside.schedule.repository.HikeScheduleRepository;
import com.beside.schedule.repository.ScheduleMemberRepository;
import com.beside.schedule.repository.ScheduleMemoRepository;
import com.beside.util.CommonUtil;
import com.beside.util.Coordinate;
import com.beside.weather.dto.WeatherResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ScheduleService {
    private final HikeScheduleRepository hikeScheduleRepository;
    private final MntiRepository mntiRepository;
    private final MountainService mountainService;
    private final ScheduleMemoRepository scheduleMemoRepository;
    private final ScheduleMemberRepository scheduleMemberRepository;
    private final ObjectMapper objectMapper;


    public List<ScheduleResponse> mySchedule(String userId) {
        List<ScheduleResponse> scheduleResponseList = new ArrayList<>();
        List<ScheduleMember> scheduleMemberList = scheduleMemberRepository.findByIdMemberId(userId);

        for(ScheduleMember scheduleMember : scheduleMemberList) {
            HikeSchedule hikeSchedule = hikeScheduleRepository.findByScheduleId(scheduleMember.getId().getScheduleId()).orElseThrow();
            scheduleResponseList.add(convertToScheduleResponse(hikeSchedule));
        }
        return scheduleResponseList;
//        return hikeScheduleRepository.findByUserIdAndDelYn(userId, "N").stream()
//                .map(this::convertToScheduleResponse)
//                .collect(Collectors.toList());
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


    public ScheduleIdResponse createSchedule(String userId, CreateScheduleRequest request) {
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

        //일정 멤버로 추가
        joinSchedule(userId, hikeSchedule.getScheduleId());
        return ScheduleIdResponse.builder().id(hikeSchedule.getScheduleId()).build();
    }


    public String getMountainName(String mountainId) {
        MntiEntity mountain = mntiRepository.findByMntiListNo(mountainId).orElseThrow();
        return mountain.getMntiName();
    }


    @Transactional
    public ScheduleIdResponse modifySchedule(String userId, ModifyScheduleRequest request) {
        HikeSchedule hikeSchedule = hikeScheduleRepository.findByUserIdAndScheduleId(userId, request.getScheduleId()).orElseThrow();
        hikeSchedule.updateSchedule(request);
        hikeScheduleRepository.save(hikeSchedule);
        return ScheduleIdResponse.builder().id(hikeSchedule.getScheduleId()).build();
    }


    @Transactional
    public ScheduleIdResponse deleteSchedule(String userId, String scheduleId) {
        HikeSchedule hikeSchedule = hikeScheduleRepository.findByUserIdAndScheduleId(userId, scheduleId).orElseThrow();
        hikeSchedule.deleteSchedule();
        hikeScheduleRepository.save(hikeSchedule);
        return ScheduleIdResponse.builder().id(hikeSchedule.getScheduleId()).build();
    }

    public DetailScheduleResponse detailSchedule(String userId, String scheduleId) throws IOException, URISyntaxException {
        MemberId memberId = new MemberId(scheduleId, userId);
        Optional<ScheduleMember> memberCheck = scheduleMemberRepository.findById(memberId);
        if(memberCheck.isEmpty()) {
            throw new EntityNotFoundException("등산 일정의 멤버 아님");
        }

        List<WeatherResponse> weatherList = mountainService.getWeatherList();


        HikeSchedule hikeSchedule = hikeScheduleRepository.findByScheduleId(scheduleId).orElseThrow();
        MntiEntity mountain = mntiRepository.findByMntiInfo(hikeSchedule.getMountainId());
        return DetailScheduleResponse.builder()
                .scheduleId(scheduleId)
                .mountainName(getMountainName(hikeSchedule.getMountainId()))
                .courseName(mountainService.getCourseNameByNo(hikeSchedule.getCourseNo()))
                .scheduleDate(hikeSchedule.getScheduleDate())
                .mountainImg(CommonUtil.getImageByMountain(hikeSchedule.getMountainId()))
                .mountainHigh(mountain.getMntihigh())
                .mountainLevel(mountain.getMntiLevel())
                .mountainAddress(mountain.getMntiAdd())
                .course(getCourse(hikeSchedule.getMountainId(), hikeSchedule.getCourseNo()))
                .weatherList(weatherList)
                .famous100(false)
                .build();
    }

    public Course getCourse(String mountainId, String courseId) throws IOException {
        Course course = new Course();

        MntiEntity mountain = mntiRepository.findById(mountainId).orElseThrow();
        String mountainName = mountain.getMntiName();

        ClassPathResource resource = new ClassPathResource("/mntiCourseData/PMNTN_"+mountainName+"_"+mountainId+".json");
        JsonNode rootNode = objectMapper.readTree(resource.getContentAsByteArray());
        JsonNode itemsNode = rootNode.path("features");

        if (itemsNode.isArray()) {
            for (JsonNode item : itemsNode) {
                String originCourseId = item.path("attributes").path("PMNTN_SN").asText();
                if(Objects.equals(courseId, originCourseId)) {
                    course.setCourseNo(originCourseId);
                    course.setCourseName(item.path("attributes").path("PMNTN_NM").asText());
                    course.setMntiTime(item.path("attributes").path("PMNTN_UPPL").asLong() + item.path("attributes").path("PMNTN_GODN").asLong());
                    course.setMntiDist(item.path("attributes").path("PMNTN_LT").asText());
                    course.setMntiLevel(item.path("attributes").path("PMNTN_DFFL").asText());

                    JsonNode pathsNode = item.path("geometry").path("paths");
                    if (pathsNode.isArray()) {
                        List<List<Coordinate>> paths = new ArrayList<>();
                        for (JsonNode pathNode : pathsNode) {
                            List<Coordinate> path = new ArrayList<>();
                            for (JsonNode coordNode : pathNode) {
                                double[] coordinates = new double[]{coordNode.get(0).asDouble(), coordNode.get(1).asDouble()};
                                path.add(new Coordinate(coordinates));
                            }
                            paths.add(path);
                        }
                        course.setPaths(paths);
                    }
                }
            }
        }
        return course;
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
        ScheduleMemo memo = null;
        try {
            memo = ScheduleMemo.builder()
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
        return Objects.requireNonNull(memo).getMemoId();
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


    public String joinSchedule(String userId, String scheduleId) {
        MemberId memberId = new MemberId(scheduleId, userId);
        ScheduleMember scheduleMember = ScheduleMember.builder()
                .id(memberId).build();
        scheduleMemberRepository.save(scheduleMember);
        return scheduleId;
    }

    public String leaveSchedule(String userId, String scheduleId) {
        MemberId id = new MemberId(scheduleId, userId);
        scheduleMemberRepository.deleteById(id);
        return scheduleId;
    }
}
