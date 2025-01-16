package com.beside.mountain.service;

import com.beside.mountain.domain.MntiEntity;
import com.beside.mountain.dto.Course;
import com.beside.mountain.dto.MntiDetailInput;
import com.beside.mountain.dto.MntiDetailOutput;
import com.beside.mountain.repository.MntiRepository;
import com.beside.util.CommonUtil;
import com.beside.util.Coordinate;
import com.beside.weather.api.WeatherApi;
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
public class MntiDetailService {

    private final ObjectMapper objectMapper;
    private final WeatherApi weatherApi;
    private final MntiRepository mntiRepository;

    public MntiDetailOutput readJsonFile(MntiDetailInput mntiDetailInput) throws Exception {
        MntiDetailOutput mntiDetailOutput = new MntiDetailOutput();
        MntiEntity mntiInfo = mntiRepository.findByMntiInfo(mntiDetailInput.getMntiListNo());

        Course course = new Course();
        List<Course> courses = new ArrayList<>();
        ClassPathResource resource = new ClassPathResource("/mntiCourseData/PMNTN_"+mntiInfo.getMntiName()+"_"+mntiInfo.getMntiListNo()+".json");

        JsonNode rootNode = objectMapper.readTree(resource.getContentAsByteArray());

        JsonNode itemsNode = rootNode.path("features");
        //고정된 정보
        mntiDetailOutput.setMntiName(mntiInfo.getMntiName());
        mntiDetailOutput.setMntiAddress(mntiInfo.getMntiAdd());
        mntiDetailOutput.setPhotoFile(CommonUtil.getImageByMountain(mntiInfo.getMntiListNo()));

        if (itemsNode.isArray()) {
            for (JsonNode item : itemsNode) {
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
            mntiDetailOutput.setContent(rootNode.path("content").asText());
            mntiDetailOutput.setMntiLevel(mntiInfo.getMntiLevel());
            mntiDetailOutput.setCourse(courses);
            mntiDetailOutput.setMntiHigh(mntiInfo.getMntihigh());
        }

        return mntiDetailOutput;
    }

}
