package com.beside.service;

import com.beside.Entity.MntiReserEntity;
import com.beside.Entity.UserEntity;
import com.beside.define.GsonParserSvc;
import com.beside.model.*;
import com.beside.repository.MntiRepository;
import com.beside.repository.ReserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MntiResrService {

    private final ObjectMapper objectMapper;
    private final GsonParserSvc gsonParserSvc;
    private final WatherApi watherApi;
    private final MntiRepository mntiRepository;
    private final ReserRepository reserRepository;

    public MntiReserOutput reserJsonFile(MntiReserInput mntiReserInput) throws Exception {
        MntiReserOutput mntiReserOutput = new MntiReserOutput();
        Course course = new Course();
        List<Course> courses = new ArrayList<>();
        ClassPathResource resource = new ClassPathResource("/mntiCourseData/"+mntiReserInput.getMntilistno()+".json");

        JsonNode rootNode = objectMapper.readTree(resource.getContentAsByteArray());

        JsonNode itemsNode = rootNode.path("features");
        //고정된 정보
        mntiReserOutput.setMnti_name(mntiReserInput.getMntiname());
        mntiReserOutput.setMnti_add(mntiReserInput.getMntiadd());
        mntiReserOutput.setMntilist_no(mntiReserInput.getMntilistno());
        mntiReserOutput.setPoto_url(gsonParserSvc.GsonParserPotolList(mntiReserInput.getMntilistno()));

        if (itemsNode.isArray()) {
            for (JsonNode item : itemsNode) {
                if (StringUtils.equals(item.path("attributes").path("PMNTN_SN").asText(), mntiReserInput.getCoursno())) {
                    course.setCourse_no(item.path("attributes").path("PMNTN_SN").asText());
                    course.setCourse_name(item.path("attributes").path("PMNTN_NM").asText());
                    course.setMnti_time(item.path("attributes").path("PMNTN_UPPL").asLong() + item.path("attributes").path("PMNTN_GODN").asLong());
                    course.setMnti_reb(item.path("attributes").path("PMNTN_DFFL").asText());
                    course.setMnti_dist(item.path("attributes").path("PMNTN_LT").asText());
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
            mntiReserOutput.setMnti_high(gsonParserSvc.MntiInfo(mntiReserInput.getMntiname()).get(0));
        }

        //watherInfo   여기 등록된날짜들어가게 날씨처리하는것은 다시해봐야함
        List<Wather> watherList = new ArrayList<>();
        watherList.add(watherApi.watherListToday());// 오늘 날씨 데이터

        watherApi.watherListOrtherDay(watherList);

        mntiReserOutput.setWather_list(watherList);


        return mntiReserOutput;
    }

    public void reserInsert (MntiReserOutput mntiReserOutput, UserEntity userEntity, MntiReserInput mntiReserInput){
        MntiReserEntity mntiReserEntity = new MntiReserEntity(); //새로운 정보 입력
        Integer mntiCnt = reserRepository.findByMntiReserSerch(userEntity.getId() , mntiReserOutput.getMntilist_no()); //등산 횟수 체크 (같은 산)
        if(mntiCnt == null) {
            mntiReserEntity.setMntiCnt(1);
        }else {
            mntiReserEntity.setMntiCnt(mntiCnt + 1);
        }
        mntiReserEntity.setId(userEntity.getId());
        mntiReserEntity.setMntilistNo(mntiReserOutput.getMntilist_no());
        mntiReserEntity.setMntiCs(mntiReserOutput.getCourse().get(0).getCourse_no());
        mntiReserEntity.setMntiCsName(mntiReserOutput.getCourse().get(0).getCourse_name());
        //mntiReserEntity.setMntimt() 일단 어떤식으로 들어올지 몰라
        //mntiReserEntity.setmntiCaution
        mntiReserEntity.setMntiSts("4"); //등산예약 4 등산 중 0 등산 완료 1 등산 실패 2
        mntiReserEntity.setMntiStrDt(mntiReserInput.getMntiStrDt());
        mntiReserEntity.setMntiReb(mntiReserOutput.getCourse().get(0).getMnti_reb());
        mntiReserEntity.setMntiClimTm(mntiReserOutput.getCourse().get(0).getMnti_dist());
        mntiReserEntity.setMntiReser(LocalDate.now());

        reserRepository.save(mntiReserEntity);

    }
}
