package com.beside.schedule.service;

import com.beside.mountain.domain.MntiEntity;
import com.beside.mountain.dto.Course;
import com.beside.mountain.repository.MntiRepository;
import com.beside.mountain.service.MountainService;
import com.beside.schedule.domain.*;
import com.beside.schedule.dto.*;
import com.beside.schedule.repository.HikeScheduleRepository;
import com.beside.schedule.repository.ScheduleInvitationRepository;
import com.beside.schedule.repository.ScheduleMemberRepository;
import com.beside.schedule.repository.ScheduleMemoRepository;
import com.beside.user.dto.UserInfoResponse;
import com.beside.user.service.UserService;
import com.beside.util.CommonUtil;
import com.beside.util.Coordinate;
import com.beside.weather.dto.WeatherResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.IntBinaryOperator;

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
    private final ScheduleInvitationRepository scheduleInvitationRepository;
    private final UserService userService;

    private final Map<Integer, String> imageUrl = new HashMap<>();

    @PostConstruct
    public void init() {
        try {
            setImageUrl();
        } catch (IOException e) {
            // 로깅 및 예외 처리
            e.printStackTrace();
        }
    }

    private void setImageUrl() throws IOException {
        imageUrl.put(1, "https://i.ibb.co/ymzWkhX/1.png");
        imageUrl.put(2, "https://i.ibb.co/FHNdPg2/2.png");
        imageUrl.put(3, "https://i.ibb.co/BGk7qkn/3.png");
        imageUrl.put(4, "https://i.ibb.co/Yf8P3v3/4.png");
        imageUrl.put(5, "https://i.ibb.co/4WgPf1m/5.png");
        imageUrl.put(6, "https://i.ibb.co/XSRQcLc/6.png");

    }


    public List<ScheduleResponse> mySchedule(String userId) throws IOException, URISyntaxException {
        log.info("user id : {}", userId);
        List<ScheduleResponse> scheduleResponseList = new ArrayList<>();
        List<ScheduleMember> scheduleMemberList = scheduleMemberRepository.findByIdMemberId(userId);

        for(ScheduleMember scheduleMember : scheduleMemberList) {
            Optional<HikeSchedule> scheduleCheck = hikeScheduleRepository.findByScheduleIdAndDelYn(scheduleMember.getId().getScheduleId(), "N");
            if(scheduleCheck.isPresent()) {
                HikeSchedule hikeSchedule = scheduleCheck.get();
                scheduleResponseList.add(convertToScheduleResponse(hikeSchedule));
            }
        }
        scheduleResponseList.sort(Comparator.comparing(ScheduleResponse::getScheduleDate).reversed());
        return scheduleResponseList;
    }

    private ScheduleResponse convertToScheduleResponse(HikeSchedule entity) throws IOException, URISyntaxException {
        List<WeatherResponse> weatherList = mountainService.getWeatherList();

        ScheduleResponse response = new ScheduleResponse();
        response.setScheduleId(entity.getScheduleId());
        response.setMountain(getMountainName(entity.getMountainId()));
        response.setMemberCount(entity.getMemberCount());
        response.setScheduleDate(entity.getScheduleDate());
        response.setCourse(mountainService.getCourseNameByNo(entity.getCourseNo()));
        response.setWeatherList(weatherList);
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
        MntiEntity mountain = mntiRepository.findByMntiListNo(mountainId).orElseThrow(() -> new RuntimeException("해당 산이 존재하지 않습니다. mountain id : " + mountainId));
        return mountain.getMntiName();
    }


    @Transactional
    public ScheduleIdResponse modifySchedule(String userId, ModifyScheduleRequest request) {
        MemberId memberId = new MemberId(request.getScheduleId(), userId);
        Optional<ScheduleMember> memberCheck = scheduleMemberRepository.findById(memberId);
        if(memberCheck.isEmpty()) {
            throw new EntityNotFoundException("등산 일정의 멤버 아님");
        }

        HikeSchedule hikeSchedule = hikeScheduleRepository.findByScheduleId(request.getScheduleId()).orElseThrow();
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
                .mountainId(hikeSchedule.getMountainId())
                .mountainName(getMountainName(hikeSchedule.getMountainId()))
                .courseName(mountainService.getCourseNameByNo(hikeSchedule.getCourseNo()))
                .scheduleDate(hikeSchedule.getScheduleDate())
                .memberCount(hikeSchedule.getMemberCount())
                .mountainImg(CommonUtil.getImageByMountain(hikeSchedule.getMountainId()))
                .mountainHigh(mountain.getMntihigh())
                .mountainLevel(mountain.getMntiLevel())
                .mountainAddress(mountain.getMntiAdd())
                .course(getCourse(hikeSchedule.getMountainId(), hikeSchedule.getCourseNo()))
                .weatherList(weatherList)
                .famous100(mountain.isFamous100())
                .build();
    }

    public Course getCourse(String mountainId, String courseId) throws IOException {
        MntiEntity mountain = mntiRepository.findById(mountainId).orElseThrow();
        String mountainName = mountain.getMntiName();

        if(Objects.equals(courseId, "free")) {
            Course course = new Course();
            course.setCourseNo("free");
            course.setCourseName("자유코스");
            return course;
        }

        ClassPathResource resource = new ClassPathResource("/mntiCourseData/PMNTN_"+mountainName+"_"+mountainId+".json");
        JsonNode rootNode = objectMapper.readTree(resource.getContentAsByteArray());
        JsonNode itemsNode = rootNode.path("features");

        if (itemsNode.isArray()) {
            Course course = new Course();
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
            return course;
        }

        return null;
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
        list.sort(Comparator.comparing(MemoListResponse::getMemoId));
        return list;
    }

    public CreateMemoResponse createMemo(CreateMemoRequest request, String userId) {
        List<MemoResponse> memoResponses = new ArrayList<>();
        String scheduleId = request.getScheduleId();

        List<MemoRequest> memoRequestList = request.getMemoRequest();

        for(MemoRequest memoRequest : memoRequestList) {
            ScheduleMemo memo = ScheduleMemo.builder()
                    .scheduleId(scheduleId)
                    .memoId(CommonUtil.getMsgId())
                    .content(memoRequest.getText())
                    .createUser(userId)
                    .checkStatus(memoRequest.isChecked())
                    .createDate(LocalDateTime.now())
                    .build();
            scheduleMemoRepository.save(memo);
            MemoResponse memoResponse = MemoResponse.builder().memoId(memo.getMemoId()).text(memo.getContent()).checked(memo.isCheckStatus()).build();
            memoResponses.add(memoResponse);
        }
        return CreateMemoResponse.builder().scheduleId(scheduleId).memoResponse(memoResponses).build();
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

    public InvitationResponse viewInvitation(String invitationId) throws IOException {
        ScheduleInvitation scheduleInvitation = scheduleInvitationRepository.findByInvitationId(invitationId).orElseThrow(() -> new RuntimeException("해당 초대장이 존재하지 않습니다. schedule id : " + invitationId));
        HikeSchedule hikeSchedule = hikeScheduleRepository.findByScheduleId(scheduleInvitation.getScheduleId()).orElseThrow(() -> new RuntimeException("해당 일정이 존재하지 않습니다. schedule id : " + scheduleInvitation.getScheduleId()));

        UserInfoResponse userInfoResponse = userService.userInfo(scheduleInvitation.getCreateUser());

        return InvitationResponse.builder()
                .invitationId(scheduleInvitation.getInvitationId())
                .scheduleId(scheduleInvitation.getScheduleId())
                .imgNumber(scheduleInvitation.getImgNumber())
                .imgUrl(imageUrl.get(scheduleInvitation.getImgNumber()))
                .img(CommonUtil.getInvitationImg(scheduleInvitation.getImgNumber()))
                .createUser(scheduleInvitation.getCreateUser())
                .nickname(userInfoResponse.getNickname())
                .scheduleDate(hikeSchedule.getScheduleDate())
                .mountainName(getMountainName(hikeSchedule.getMountainId()))
                .courseName(mountainService.getCourseNameByNo(hikeSchedule.getCourseNo()))
                .text(scheduleInvitation.getContent())
                .build();
    }




    public InvitationResponse createInvitation(String userId, InvitationRequest request) {
        ScheduleInvitation scheduleInvitation = ScheduleInvitation.builder()
                .invitationId(CommonUtil.getMsgId())
                .scheduleId(request.getScheduleId())
                .imgNumber(request.getImgNumber())
                .createUser(userId)
                .content(request.getText()).build();
        scheduleInvitationRepository.save(scheduleInvitation);
        HikeSchedule hikeSchedule = hikeScheduleRepository.findByScheduleId(request.getScheduleId()).orElseThrow(() -> new RuntimeException("해당 일정이 존재하지 않습니다. schedule id : " + request.getScheduleId()));

        return InvitationResponse.builder()
                .invitationId(scheduleInvitation.getInvitationId())
                .scheduleId(scheduleInvitation.getScheduleId())
                .createUser(scheduleInvitation.getCreateUser())
                .scheduleDate(hikeSchedule.getScheduleDate())
                .mountainName(getMountainName(hikeSchedule.getMountainId()))
                .courseName(mountainService.getCourseNameByNo(hikeSchedule.getCourseNo()))
                .text(scheduleInvitation.getContent())
                .build();
    }

    public DetailScheduleResponse viewScheduleFromInvitation(String invitationId) throws IOException, URISyntaxException {
        ScheduleInvitation scheduleInvitation = scheduleInvitationRepository.findByInvitationId(invitationId).orElseThrow(() -> new RuntimeException("해당 초대장이 존재하지 않습니다. schedule id : " + invitationId));

        HikeSchedule hikeSchedule = hikeScheduleRepository.findByScheduleId(scheduleInvitation.getScheduleId()).orElseThrow(() -> new RuntimeException("해당 일정이 존재하지 않습니다. schedule id : " + scheduleInvitation.getScheduleId()));

        List<WeatherResponse> weatherList = mountainService.getWeatherList();

        MntiEntity mountain = mntiRepository.findByMntiInfo(hikeSchedule.getMountainId());
        return DetailScheduleResponse.builder()
                .scheduleId(scheduleInvitation.getScheduleId())
                .mountainId(hikeSchedule.getMountainId())
                .mountainName(getMountainName(hikeSchedule.getMountainId()))
                .courseName(mountainService.getCourseNameByNo(hikeSchedule.getCourseNo()))
                .scheduleDate(hikeSchedule.getScheduleDate())
                .memberCount(hikeSchedule.getMemberCount())
                .mountainImg(CommonUtil.getImageByMountain(hikeSchedule.getMountainId()))
                .mountainHigh(mountain.getMntihigh())
                .mountainLevel(mountain.getMntiLevel())
                .mountainAddress(mountain.getMntiAdd())
                .course(getCourse(hikeSchedule.getMountainId(), hikeSchedule.getCourseNo()))
                .weatherList(weatherList)
                .famous100(mountain.isFamous100())
                .build();
    }


    public List<InvitationImgResponse> getInvitationImg() throws IOException {
        List<InvitationImgResponse> imgList = new ArrayList<>();
        for(int i = 1; i<= 6; i++) {
            InvitationImgResponse response = new InvitationImgResponse();
            response.setImgNumber(i);
            response.setImg(CommonUtil.getInvitationImg(i));
            imgList.add(response);
        }
        return imgList;
    }




}
