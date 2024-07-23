package com.beside.mountain.service;

import com.beside.mountain.domain.MntiEntity;
import com.beside.mountain.dto.Course;
import com.beside.mountain.dto.CourseResponse;
import com.beside.mountain.dto.MntiDetailOutput;
import com.beside.mountain.dto.MntiListOutput;
import com.beside.mountain.repository.MntiRepository;
import com.beside.util.CommonUtil;
import com.beside.util.Coordinate;
import com.beside.weather.api.WeatherApi;
import com.beside.weather.dto.Weather;
import com.beside.weather.dto.WeatherResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MountainService {

    private final MntiRepository mntiRepository;
    private final WeatherApi weatherApi;
    private final ObjectMapper objectMapper;
    private final Map<String, String> courseMap = new HashMap<>();


    @PostConstruct
    public void init() {
        try {
            initializeCourseMap();
        } catch (IOException e) {
            // 로깅 및 예외 처리
            e.printStackTrace();
        }
    }


    private void initializeCourseMap() throws IOException {
        // 모든 산 목록을 가져와서 courseMap을 초기화
        List<MntiEntity> mountains = mntiRepository.findAll();
        for (MntiEntity mntiEntity : mountains) {
            ClassPathResource resource = new ClassPathResource("/mntiCourseData/PMNTN_" + mntiEntity.getMntiName() + "_" + mntiEntity.getMntiListNo() + ".json");
            JsonNode rootNode = objectMapper.readTree(resource.getContentAsByteArray());
            JsonNode itemsNode = rootNode.path("features");

            if (itemsNode.isArray()) {
                for (JsonNode item : itemsNode) {
                    String courseName = item.path("attributes").path("PMNTN_NM").asText();
                    String courseNo = item.path("attributes").path("PMNTN_SN").asText();
                    if (courseName != null && !courseName.isEmpty() && !courseName.equals(" ")) {
                        courseMap.put(courseNo, courseName);
                    }
                }
            }
        }
    }



    public List<MntiListOutput> getList(String keyword) throws IOException {
        return fetchAndConvert(keyword);
    }

    public Page<MntiListOutput> getPageList(Pageable pageable, String keyword) throws IOException {
        List<MntiListOutput> mntiListOutput = fetchAndConvert(keyword);

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), mntiListOutput.size());
        return new PageImpl<>(mntiListOutput.subList(start, end), pageable, mntiListOutput.size());
    }

    private List<MntiListOutput> fetchAndConvert(String keyword) throws IOException {
        List<MntiEntity> list;

        if (StringUtils.hasText(keyword)) {
            list = mntiRepository.findByMntiNameContaining(keyword);
        } else {
            list = mntiRepository.findAll();
        }

        List<MntiListOutput> mntiListOutput = new ArrayList<>();
        for (MntiEntity mntiEntity : list) {
            MntiListOutput dto = new MntiListOutput();
            dto.setMntiName(mntiEntity.getMntiName());
            dto.setMntiListNo(mntiEntity.getMntiListNo());
            dto.setMntiLevel(mntiEntity.getMntiLevel());
            dto.setMntiAdd(mntiEntity.getMntiAdd());
            dto.setHeight(mntiEntity.getMntihigh());
            dto.setPotoFile(CommonUtil.getImageByMountain(mntiEntity.getMntiListNo()));
            mntiListOutput.add(dto);
        }

        mntiListOutput.sort(Comparator.comparing(MntiListOutput::getMntiName));
        return mntiListOutput;
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
            Set<String> courseNames = new HashSet<>();
            for (JsonNode item : itemsNode) {
                String courseName = item.path("attributes").path("PMNTN_NM").asText();
                if(courseName != null && !courseName.isEmpty() && !courseNames.contains(courseName) && !courseName.equals(" ")) {
                    Course course = new Course();
                    course.setCourseNo(item.path("attributes").path("PMNTN_SN").asText());
                    course.setCourseName(courseName);
                    courseNames.add(courseName);
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
        }

        mntiDetailOutput.setContent(rootNode.path("content").asText());
        mntiDetailOutput.setMntiLevel(mntiEntity.getMntiLevel());
        mntiDetailOutput.setCourse(courses);
        mntiDetailOutput.setMntiHigh(mntiEntity.getMntihigh());


        //날씨 리스트 가져오기
        List<WeatherResponse> weatherList = new ArrayList<>();
        weatherList = weatherList();
        mntiDetailOutput.setWeatherList(weatherList);
        return mntiDetailOutput;
    }



    public WeatherResponse getWeather(String localDate) throws IOException, URISyntaxException {
        return weatherApi.getTodayWeather(localDate);
    }


    public List<WeatherResponse> weatherList() throws IOException, URISyntaxException {
        List<WeatherResponse> weatherList = new ArrayList<>();

        // 현재 날짜 가져오기
        LocalDate today = LocalDate.now();

        // DateTimeFormatter 설정 (yyyymmdd 형식)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        for(int i = 0; i < 7; i++) {
            LocalDate targetDate = today.plusDays(i);

            // LocalDate를 yyyyMMdd 형식의 문자열로 변환
            String dateString = targetDate.format(formatter);

            // yyyyMMdd 형식의 문자열을 숫자형으로 변환
            int dateNumber = Integer.parseInt(dateString);
            weatherList.add(getWeather(String.valueOf(dateNumber)));
        }

        weatherList.sort(Comparator.comparing(WeatherResponse::getDate));
        return weatherList;
    }


    public List<CourseResponse> getCourseList(String mountainId) throws IOException {
        List<CourseResponse> courseList = new ArrayList<>();
        MntiEntity mntiEntity = mntiRepository.findById(mountainId).orElseThrow(() -> new EntityNotFoundException("산이 존재하지 않습니다."));

        ClassPathResource resource = new ClassPathResource("/mntiCourseData/PMNTN_"+mntiEntity.getMntiName()+"_"+mntiEntity.getMntiListNo()+".json");
        JsonNode rootNode = objectMapper.readTree(resource.getContentAsByteArray());
        JsonNode itemsNode = rootNode.path("features");

        if (itemsNode.isArray()) {
            Set<String> courseNames = new HashSet<>();
            for (JsonNode item : itemsNode) {
                String courseName = item.path("attributes").path("PMNTN_NM").asText();
                String courseNo = item.path("attributes").path("PMNTN_SN").asText();
                if(courseName != null && !courseName.isEmpty() && !courseNames.contains(courseName) && !courseName.equals(" ")) {
                    CourseResponse courseResponse = new CourseResponse();
                    courseResponse.setCourseNo(courseNo);
                    courseResponse.setCourseName(courseName);
                    courseList.add(courseResponse);
                    courseNames.add(courseName);
                    courseMap.put(courseNo, courseName);
                }
            }
        }
        return courseList;
    }


    public String getCourseNameByNo(String courseNo) {
        return courseMap.get(courseNo);
    }
}
