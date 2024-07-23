package com.beside.mountain.controller;

import com.beside.mountain.dto.*;
import com.beside.mountain.service.MntiDetailService;
import com.beside.mountain.service.MountainService;
import com.beside.mountain.service.MntiSerchService;
import com.beside.util.CommonUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Set;


@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "3.산", description = "산 관련 API")
@RequestMapping("/main")
public class MountainController {

    private final MountainService mountainService;

    @Operation(summary = "전체 산 리스트", description = "전체 산 리스트를 표출합니다. keyword를 넣어서 조회하는 경우 keyword를 포함하는 산 리스트를 표출합니다.")
    @GetMapping("/list")
    public ResponseEntity<?> getMntiList(@RequestParam(required = false) Integer page,
                                            @RequestParam(required = false) Integer size,
                                            @RequestParam(name = "keyword", required = false) String keyword) throws IOException {
        if(size == null) {
            return ResponseEntity.ok(mountainService.getList(keyword));
        } else {
            Pageable pageable = PageRequest.of(page, size);
            return ResponseEntity.ok(mountainService.getPageList(pageable, keyword));
        }
    }

    @Operation(summary = "산 상세 정보", description = "산의 고유 id를 조회하여 상세 정보를 조회할 수 있습니다.")
    @PostMapping("/detail/{id}")
    public ResponseEntity<?> detail(@PathVariable String id) throws Exception {
        MntiDetailOutput response = mountainService.getMountainDetail(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "산 코스 리스트", description = "산의 고유 id를 조회하여 코스 리스트를 조회할 수 있습니다.")
    @PostMapping("/courseList/{mountainId}")
    public ResponseEntity<?> course(@PathVariable String mountainId) throws IOException {
        List<CourseResponse> courseList = mountainService.getCourseList(mountainId);
        return ResponseEntity.ok(courseList);
    }

    @Operation(summary = "날씨 정보", description = "날씨 정보 가져오기 테스트 api")
    @PostMapping("/weather")
    public ResponseEntity<?> weather() throws Exception {
//        return ResponseEntity.ok(mountainService.getWeather());
        return null;
    }


    @GetMapping("/imgTest")
    public String imgTest() throws Exception {
        return CommonUtil.getImageByMountain("112300301");
    }

}
