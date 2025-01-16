package com.beside.mountain.controller;

import com.beside.mountain.dto.CourseResponse;
import com.beside.mountain.dto.MntiDetailOutput;
import com.beside.mountain.service.MountainService;
import com.beside.util.CommonUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;


@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin
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

        return ResponseEntity.ok(mountainService.getWeatherList());
    }


    @Operation(summary = "이미지 반환 테스트 (바이트 배열을 String 형식으로 변환 후 반환)", description = "바이트 배열을 String 형식으로 변환 후 반환")
    @GetMapping("/imgTest")
    public ResponseEntity<?> imgTest() throws Exception {
        return ResponseEntity.ok(CommonUtil.getImageByMountain("112300301"));
    }

    @Operation(summary = "이미지 반환 테스트 (바이트 배열)", description = "바이트 배열로 반환")
    @GetMapping("/imgTest2")
    public ResponseEntity<?> imgTest2() throws Exception {
        return ResponseEntity.ok(CommonUtil.getImageByMountain2("112300301"));
    }



    //https://over-the-mountain.site/api/main/111100101 형식으로 호출 시 사진 리소스로 응답 받음
    @GetMapping("/{filename}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) throws IOException {
        final Path imageDirectory = Paths.get("/root/JavaProject/beside405/img/mountain/"); //이미지폴더 경로


        // 확장자를 지정하지 않고 파일 이름만으로 검색
        Path imagePath = null;
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(imageDirectory, filename + ".*")) {
            // 검색 결과 중 첫 번째 파일을 선택
            for (Path entry : stream) {
                imagePath = entry.normalize();
                break;
            }
        }

        if (imagePath == null) {
            return ResponseEntity.notFound().build();
        }

        Resource resource = new UrlResource(Objects.requireNonNull(imagePath).toUri());
        String contentType = Files.probeContentType(imagePath);


        return ResponseEntity.ok()
                //.contentType(MediaType.IMAGE_JPEG) // MIME 타입을 직접 설정
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

}
