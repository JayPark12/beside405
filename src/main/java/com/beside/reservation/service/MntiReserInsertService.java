package com.beside.reservation.service;

import com.beside.mountain.domain.MntiEntity;
import com.beside.mountain.dto.Course;
import com.beside.mountain.repository.MntiRepository;
import com.beside.reservation.domain.MntiReserEntity;
import com.beside.reservation.dto.MntiReserInput;
import com.beside.reservation.dto.MntiReserOutput;
import com.beside.reservation.repository.ReserRepository;
import com.beside.util.CommonUtil;
import com.beside.util.Coordinate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MntiReserInsertService {

    private final ObjectMapper objectMapper;
    private final MntiRepository mntiRepository;
    private final ReserRepository reserRepository;

    public MntiReserOutput execute(String id, MntiReserInput mntiReserInput) throws Exception {
        reserInputCheck(id ,mntiReserInput); // input check 나중에 더추가하거나 확인필요
        MntiReserOutput mntiReserOutput = reserJsonFile(mntiReserInput); //json file read
        reserInsert(id ,mntiReserOutput, mntiReserInput); // 데이터 적재

        return mntiReserOutput;
    }

    private MntiReserOutput reserJsonFile(MntiReserInput mntiReserInput) throws Exception {
        MntiReserOutput mntiReserOutput = new MntiReserOutput();
        MntiEntity mntiInfo = mntiRepository.findByMntiInfo(mntiReserInput.getMntiListNo());
        Course course = new Course();
        List<Course> courses = new ArrayList<>();
        ClassPathResource resource = new ClassPathResource("/mntiCourseData/PMNTN_"+mntiInfo.getMntiName()+"_"+mntiInfo.getMntiListNo()+".json");

        JsonNode rootNode = objectMapper.readTree(resource.getContentAsByteArray());

        JsonNode itemsNode = rootNode.path("features");
        //고정된 정보
        mntiReserOutput.setMntiName(mntiInfo.getMntiName());
        mntiReserOutput.setMntiAdd(mntiInfo.getMntiAdd());
        mntiReserOutput.setMntiListNo(mntiInfo.getMntiListNo());
        mntiReserOutput.setPotoFiles(CommonUtil.potoFile(mntiInfo.getMntiListNo(), mntiInfo.getMntiName()));
        mntiReserOutput.setMntiLevel(mntiInfo.getMntiLevel());

        if (itemsNode.isArray()) {
            for (JsonNode item : itemsNode) {//코스 이름만 맞는 것으로 setting
                if (StringUtils.equals(item.path("attributes").path("PMNTN_SN").asText(), mntiReserInput.getMntiCourse())) {
                    course.setCourseNo(item.path("attributes").path("PMNTN_SN").asText());
                    course.setCourseName(item.path("attributes").path("PMNTN_NM").asText());
                    course.setMntiTime(item.path("attributes").path("PMNTN_UPPL").asLong() + item.path("attributes").path("PMNTN_GODN").asLong());
                    course.setMntiLevel(item.path("attributes").path("PMNTN_DFFL").asText());
                    course.setMntiDist(item.path("attributes").path("PMNTN_LT").asText());
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
                    courses.add(course);
                }
                mntiReserOutput.setCourse(courses);
            }
            mntiReserOutput.setMntiHigh(mntiInfo.getMntihigh());
        }
        return mntiReserOutput;
    }

    private void reserInsert (String id , MntiReserOutput mntiReserOutput, MntiReserInput mntiReserInput) throws InterruptedException {
        MntiReserEntity mntiReserEntity = new MntiReserEntity(); //새로운 정보 입력
        reserInputCheck(id , mntiReserInput);
        Integer mntiCnt = reserRepository.findByMntiReserSearch(id , mntiReserOutput.getMntiListNo()); //등산 횟수 체크 (같은 산)
        if(mntiCnt == null) {
            mntiReserEntity.setMntiCnt(1);
        }else {
            mntiReserEntity.setMntiCnt(mntiCnt + 1);
        }
        mntiReserEntity.setMntihigh(mntiReserOutput.getMntiHigh());
        mntiReserEntity.setId(id);
        mntiReserEntity.setMntiName(mntiReserOutput.getMntiName());
        mntiReserEntity.setMntiListNo(mntiReserOutput.getMntiListNo());
        mntiReserEntity.setMntiCourse(mntiReserInput.getMntiCourse());
        mntiReserEntity.setMntiCourseName(mntiReserOutput.getCourse().get(0).getCourseName());
        //mntiReserEntity.setMntimt() 일단 어떤식으로 들어올지 몰라
        //mntiReserEntity.setmntiCaution
        mntiReserEntity.setMntiSts("0"); //0 : 등산 계획,  1 :등산 중 , 2 : 등산완료 ,3 : 등산실패
        mntiReserEntity.setMntiStrDate(mntiReserInput.getMntiStrDate());
        mntiReserEntity.setMntiLevel(mntiReserOutput.getCourse().get(0).getMntiLevel());
        mntiReserEntity.setMntiClimTm(mntiReserOutput.getCourse().get(0).getMntiTime());
        mntiReserEntity.setMntiDistance(mntiReserOutput.getCourse().get(0).getMntiDist());
        mntiReserEntity.setMntiReserDate(LocalDate.now());
        mntiReserEntity.setMntiPeople(mntiReserInput.getMntiPeople());

        reserRepository.save(mntiReserEntity);
    }

    private void reserInputCheck (String id, MntiReserInput mntiReserInput) throws InterruptedException {
        String mntiResrToday = reserRepository.findByMntiReserSerchForInputCheck(id, mntiReserInput.getMntiListNo(), mntiReserInput.getMntiCourse(), mntiReserInput.getMntiStrDate());

        // 날짜 체크 오늘보다 이른날 설정은 못함
        if(mntiReserInput.getMntiStrDate().isBefore(LocalDate.now())) {
            throw new InterruptedException("날짜 확인 해주세요 ~");
        }
        // 같은날 같은산 같은 코스는 같은날짜 등록 안됨
        if(StringUtils.equals(mntiResrToday, "0")) { //TODO sts값임 추후 추가로 넣을 예정
            throw new InterruptedException("같은 날에 같은산 같은 코스는 등산 완료 아니면 등록 못함");
        }
    }
}
