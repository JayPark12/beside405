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

    public MntiDetailOutput readJsonFile(MntiDetailInput mntiDetailInput) throws IOException, URISyntaxException {
        MntiDetailOutput mntiDetailOutput = new MntiDetailOutput();
        Course course = new Course();
        List<Course> courses = new ArrayList<>();
        ClassPathResource resource = new ClassPathResource("/mntiCourseData/"+mntiDetailInput.getMntilistNo()+".json");

        JsonNode rootNode = objectMapper.readTree(resource.getContentAsByteArray());

        JsonNode itemsNode = rootNode.path("features");
        //고정된 정보
        mntiDetailOutput.setMntiName(mntiDetailInput.getMntiName());
        mntiDetailOutput.setMntiAdd(mntiDetailInput.getMntiAdd());
        mntiDetailOutput.setPotoUrl(gsonParserSvc.GsonParserPotolList(mntiDetailInput.getMntilistNo()));


        if (itemsNode.isArray()) {
            for (JsonNode item : itemsNode) {
                course.setCourseNo(item.path("attributes").path("PMNTN_SN").asText());
                course.setCourseName(item.path("attributes").path("PMNTN_NM").asText());
                course.setMntiTime(item.path("attributes").path("PMNTN_UPPL").asLong() + item.path("attributes").path("PMNTN_GODN").asLong());
                course.setMntiReb(item.path("attributes").path("PMNTN_DFFL").asText());
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
            mntiDetailOutput.setMntiHigh(gsonParserSvc.Mntihigh(mntiDetailInput.getMntiName()));
        }


        return mntiDetailOutput;
    }
}
