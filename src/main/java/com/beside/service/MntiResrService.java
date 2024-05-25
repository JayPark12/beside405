package com.beside.service;

import com.beside.define.GsonParserSvc;
import com.beside.model.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MntiResrService {

    private final ObjectMapper objectMapper;
    private final GsonParserSvc gsonParserSvc;
    private final WatherApi watherApi;

    public MntiReserOutput reserJsonFile(MntiReserInput mntiReserInput) throws Exception {
        MntiReserOutput mntiReserOutput = new MntiReserOutput();
        Course course = new Course();
        List<Course> courses = new ArrayList<>();
        ClassPathResource resource = new ClassPathResource("/mntiCourseData/"+mntiReserInput.getMnti_list_no()+".json");

        JsonNode rootNode = objectMapper.readTree(resource.getContentAsByteArray());

        JsonNode itemsNode = rootNode.path("features");
        //고정된 정보
        mntiReserOutput.setMnti_name(mntiReserInput.getMnti_name());
        mntiReserOutput.setMnti_add(mntiReserInput.getMnti_add());
        mntiReserOutput.setPoto_url(gsonParserSvc.GsonParserPotolList(mntiReserInput.getMnti_list_no()));

        if (itemsNode.isArray()) {
            for (JsonNode item : itemsNode) {
                if (item.path("attributes").path("PMNTN_SN").asText().equals(mntiReserInput.getCourse_no())) {
                    course.setCourse_no(item.path("attributes").path("PMNTN_SN").asText());
                    course.setCourse_name(item.path("attributes").path("PMNTN_NM").asText());
                    course.setMnti_time(item.path("attributes").path("PMNTN_UPPL").asLong() + item.path("attributes").path("PMNTN_GODN").asLong());
                    course.setMnti_reb(item.path("attributes").path("PMNTN_DFFL").asText());
                    course.setMnti_dist(item.path("attributes").path("PMNTN_LT").asLong());
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
                mntiReserOutput.setMnti_high(gsonParserSvc.MntiInfo(mntiReserInput.getMnti_name()).get(0));
            }
        }

        //watherInfo   여기 등록된날짜들어가게 날씨처리하는것은 다시해봐야함
        List<Wather> watherList = new ArrayList<>();
        watherList.add(watherApi.watherListToday());// 오늘 날씨 데이터

        watherApi.watherListOrtherDay(watherList);

        mntiReserOutput.setWather_list(watherList);;


        return mntiReserOutput;
    }
}
