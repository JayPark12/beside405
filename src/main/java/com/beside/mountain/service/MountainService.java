package com.beside.mountain.service;

import com.beside.mountain.domain.MntiEntity;
import com.beside.mountain.dto.Course;
import com.beside.mountain.dto.MntiDetailOutput;
import com.beside.mountain.dto.MntiListOutput;
import com.beside.mountain.repository.MntiRepository;
import com.beside.util.CommonUtil;
import com.beside.util.Coordinate;
import com.beside.weather.api.WeatherApi;
import com.beside.weather.dto.Weather;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MountainService {

    private final MntiRepository mntiRepository;
    private final WeatherApi weatherApi;
    private final ObjectMapper objectMapper;

    public Page<MntiListOutput> mntiList(Pageable pageable) throws URISyntaxException {
        List<MntiListOutput> mntiListOutput = new ArrayList<>();
        List<MntiEntity> mntiShufList = mntiRepository.findByMnti();
        List<MntiEntity> mntiShufListPaged = mntiShufList.stream()
                .skip(pageable.getOffset())
                .limit(pageable.getPageSize())
                .toList();
        for (MntiEntity mntiEntity : mntiShufListPaged) {
            List<String> potoFileSelect = CommonUtil.potoFile(mntiEntity.getMntiListNo(), mntiEntity.getMntiName());
            MntiListOutput mntiOutput = new MntiListOutput();
            mntiOutput.setMntiName(mntiEntity.getMntiName());
            mntiOutput.setMntiListNo(mntiEntity.getMntiListNo());
            mntiOutput.setMntiLevel(mntiEntity.getMntiLevel());
            mntiOutput.setMntiAdd(mntiEntity.getMntiAdd());

            if (!potoFileSelect.isEmpty()) {
                mntiOutput.setPotoFile(potoFileSelect.get(0));
            }

            mntiListOutput.add(mntiOutput);
        }
        return new PageImpl<>(mntiListOutput, pageable, mntiShufList.size());
    }


    public Page<MntiListOutput> getList(Pageable pageable, String keyword) {
        List<MntiListOutput> mntiListOutput = new ArrayList<>();
        List<MntiEntity> list;

        if(StringUtils.hasText(keyword)) {
            list = mntiRepository.findByMntiNameContaining(keyword);
        } else {
            list = mntiRepository.findAll();
        }

        for(MntiEntity mntiEntity : list){
            MntiListOutput dto = new MntiListOutput();
            dto.setMntiName(mntiEntity.getMntiName());
            dto.setMntiListNo(mntiEntity.getMntiListNo());
            dto.setMntiLevel(mntiEntity.getMntiLevel());
            dto.setMntiAdd(mntiEntity.getMntiAdd());
            mntiListOutput.add(dto);
        }

        mntiListOutput.sort(Comparator.comparing(MntiListOutput::getMntiName));

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), list.size());
        return new PageImpl<>(mntiListOutput.subList(start, end), pageable, mntiListOutput.size());
    }


    public MntiDetailOutput getMountainDetail(String id) throws Exception {
        MntiDetailOutput mntiDetailOutput = new MntiDetailOutput();
        MntiEntity mntiEntity = mntiRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("산이 존재하지 않습니다."));
        mntiDetailOutput.setMntiName(mntiEntity.getMntiName());
        mntiDetailOutput.setMntiAddress(mntiEntity.getMntiAdd());
        mntiDetailOutput.setPhotoFile(CommonUtil.potoFile(mntiEntity.getMntiListNo(), mntiEntity.getMntiName()));


        List<Course> courses = new ArrayList<>();
        ClassPathResource resource = new ClassPathResource("/mntiCourseData/PMNTN_"+mntiEntity.getMntiName()+"_"+mntiEntity.getMntiListNo()+".json");

        JsonNode rootNode = objectMapper.readTree(resource.getContentAsByteArray());
        JsonNode itemsNode = rootNode.path("features");

        if (itemsNode.isArray()) {
            for (JsonNode item : itemsNode) {
                Course course = new Course();
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
        }

        mntiDetailOutput.setContent(rootNode.path("content").asText());
        mntiDetailOutput.setMntiLevel(mntiEntity.getMntiLevel());
        mntiDetailOutput.setCourse(courses);
        mntiDetailOutput.setMntiHigh(mntiEntity.getMntihigh());

        //날씨 정보 api 오류나서 잠시 제거
        //watherInfo
//        List<Weather> weatherList = new ArrayList<>();
//        weatherList.add(weatherApi.watherListToday());// 오늘 날씨 데이터
//
//        weatherApi.watherListOrtherDay(weatherList);
//
//        mntiDetailOutput.setWeatherList(weatherList);

        return mntiDetailOutput;
    }
}
