package com.beside.complete.controller;

import com.beside.complete.service.MntiCompleteService;
import com.beside.mountain.dto.MntiDetailOutput;
import com.beside.reservation.dto.MntiReserDetailInput;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "4.산등산완료", description = "등산완료 관련 API")
@RequestMapping("/complete")
public class MntiCompleteController {

    private final MntiCompleteService mntiCompleteService;



    @Operation(summary = "등산 완료", description = "등산완료 STS 변경")
    @PostMapping("/update")
    public ResponseEntity<?> detail(@RequestBody MntiReserDetailInput mntiReserDetailInput)  throws Exception {
        mntiCompleteService.execute(mntiReserDetailInput);
        return ResponseEntity.ok("등산 완료");
    }
}
