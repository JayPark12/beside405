package com.beside.service;

import com.beside.define.GsonParserSvc;
import com.beside.model.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MntiDetailService {

    private final ObjectMapper objectMapper;
    private final GsonParserSvc gsonParserSvc;
    private final WatherApi watherApi;

    public MntiDetailOutput readJsonFile(MntiDetailInput mntiDetailInput) throws Exception {
        MntiDetailOutput mntiDetailOutput = new MntiDetailOutput();
        Course course = new Course();
        List<Course> courses = new ArrayList<>();
        ClassPathResource resource = new ClassPathResource("/mntiCourseData/"+mntiDetailInput.getMnti_list_no()+".json");

        JsonNode rootNode = objectMapper.readTree(resource.getContentAsByteArray());

        JsonNode itemsNode = rootNode.path("features");
        //고정된 정보
        mntiDetailOutput.setMnti_name(mntiDetailInput.getMnti_name());
        mntiDetailOutput.setMnti_add(mntiDetailInput.getMnti_add());
        mntiDetailOutput.setPoto_url(gsonParserSvc.GsonParserPotolList(mntiDetailInput.getMnti_list_no()));


        if (itemsNode.isArray()) {
            for (JsonNode item : itemsNode) {
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
            mntiDetailOutput.setCourse(courses);
            mntiDetailOutput.setMnti_high(gsonParserSvc.MntiInfo(mntiDetailInput.getMnti_name()).get(0));
        }

        //watherInfo
        List<Wather> watherList = new ArrayList<>();
        watherList.add(watherApi.watherListToday());// 오늘 날씨 데이터

        watherApi.watherListOrtherDay(watherList);

        mntiDetailOutput.setWather_list(watherList);;


        return mntiDetailOutput;
    }
}
