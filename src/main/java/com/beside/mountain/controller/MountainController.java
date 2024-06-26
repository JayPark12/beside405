package com.beside.mountain.controller;

import com.beside.mountain.dto.MntiDetailInput;
import com.beside.mountain.dto.MntiDetailOutput;
import com.beside.mountain.dto.MntiListOutput;
import com.beside.mountain.dto.MntiSearchInput;
import com.beside.mountain.service.MntiDetailService;
import com.beside.mountain.service.MountainService;
import com.beside.mountain.service.MntiSerchService;
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


@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "3.산", description = "산 관련 API")
@RequestMapping("/main")
public class MountainController {

    private final MountainService mountainService;
    private final MntiSerchService mntiSerchService;
    private final MntiDetailService mntiDetailService;

    @Operation(summary = "산 리스트", description = "전체 산 리스트")
    @GetMapping("/list")
    public Page<MntiListOutput> getMntiList(@RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size,
                                            @RequestParam(name = "keyword", required = false) String keyword) {
        Pageable pageable = PageRequest.of(page, size);
        return mountainService.getList(pageable, keyword);
    }

    @Operation(summary = "산 상세 정보", description = "산의 고유 id를 조회하여 상세 정보를 조회할 수 있습니다.")
    @PostMapping("/detail/{id}")
    public ResponseEntity<?> detail(@PathVariable String id) throws Exception {
        MntiDetailOutput response = mountainService.getMountainDetail(id);
        return ResponseEntity.ok(response);
    }



    public Page<MntiListOutput> mntiSerch(@RequestBody MntiSearchInput mntiSearchInput,
                                          @RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "10")int size) throws Exception {
        Pageable pageable = PageRequest.of(page, size);
        return mntiSerchService.mntiList(mntiSearchInput,pageable);
    }

    public MntiDetailOutput mntiList(@RequestBody MntiDetailInput mntiDetailInput) throws Exception {
        MntiDetailOutput mntiList = mntiDetailService.readJsonFile(mntiDetailInput);

        return mntiList ;
    }
}
