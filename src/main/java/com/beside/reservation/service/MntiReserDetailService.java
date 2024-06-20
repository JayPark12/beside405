package com.beside.reservation.service;

import com.beside.common.util.CommomUtil;
import com.beside.mountain.dto.Course;
import com.beside.reservation.domain.MntiReserEntity;
import com.beside.reservation.dto.MntiReserDetailInput;
import com.beside.reservation.dto.MntiReserOutput;
import com.beside.reservation.repository.ReserRepository;
import com.beside.util.Coordinate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MntiReserDetailService {

    private final CommomUtil commomUtil ;
    private final ReserRepository reserRepository;
    private final ObjectMapper objectMapper;

    public MntiReserOutput execute(MntiReserDetailInput mntiReserDetailInput) throws Exception {
        String id = SecurityContextHolder.getContext().getAuthentication().getName();
        MntiReserOutput mntiReserOutput = reserJsonFile(id, mntiReserDetailInput); //json file read


        return mntiReserOutput;
    }

    private MntiReserOutput reserJsonFile(String id , MntiReserDetailInput mntiReserDetailInput) throws Exception {
        MntiReserOutput mntiReserOutput = new MntiReserOutput();
        MntiReserEntity mntiReserInfo = reserRepository.findByIdAndMntiListNoAndMntiStrDt(id, mntiReserDetailInput.getMntiListNo(), mntiReserDetailInput.getMntiStrDate(), mntiReserDetailInput.getMntiCourse(), "0");
        Course course = new Course();
        List<Course> courses = new ArrayList<>();
        ClassPathResource resource = new ClassPathResource("/mntiCourseData/PMNTN_"+mntiReserInfo.getMntiName()+"_"+mntiReserInfo.getMntiListNo()+".json");

        JsonNode rootNode = objectMapper.readTree(resource.getContentAsByteArray());

        JsonNode itemsNode = rootNode.path("features");
        //고정된 정보
        mntiReserOutput.setMntiName(mntiReserInfo.getMntiName());
        mntiReserOutput.setMntiListNo(mntiReserInfo.getMntiListNo());
        mntiReserOutput.setPotoFiles(commomUtil.potoFile(mntiReserInfo.getMntiListNo(), mntiReserInfo.getMntiName()));
        mntiReserOutput.setMntiLevel(mntiReserInfo.getMntiLevel());

        if (itemsNode.isArray()) {
            for (JsonNode item : itemsNode) {//코스 이름만 맞는 것으로 setting
                if (StringUtils.equals(item.path("attributes").path("PMNTN_SN").asText(), mntiReserDetailInput.getMntiCourse())) {
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
            mntiReserOutput.setMntiHigh(mntiReserInfo.getMntihigh());
        }
        return mntiReserOutput;
    }
}
