package com.beside.reservation.controller;

import com.beside.reservation.dto.MntiReserDetailInput;
import com.beside.reservation.dto.MntiReserListOutput;
import com.beside.reservation.service.MntiReserDeleteService;
import com.beside.reservation.service.MntiReserDetailService;
import com.beside.reservation.service.MntiReserListService;
import com.beside.reservation.dto.MntiReserInput;
import com.beside.reservation.dto.MntiReserOutput;
import com.beside.reservation.service.MntiReserInsertService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
//@Tag(name = "4.등산 일정", description = "등산 일정 관련 API")
@RequestMapping("/reser")
public class MtReserPgController {

    private final MntiReserInsertService mntiReserInsertService;
    private final MntiReserListService mntiReserListService;
    private final MntiReserDetailService mntiReserDetailService;
    private final MntiReserDeleteService mntiReserDeleteService;


//    @Operation(summary = "일정 등록", description = "일정을 등록할 수 있습니다.")
//    @PostMapping("/registration")
    public ResponseEntity<?> registration(@RequestBody MntiReserInput mntiReserInput) throws Exception {
        String id = SecurityContextHolder.getContext().getAuthentication().getName();
        log.debug("[START]  /registration, INPUT = [{}]", mntiReserInput.toString());

        MntiReserOutput output = mntiReserInsertService.execute(id, mntiReserInput);

        return ResponseEntity.ok(output);
    }

//    @Operation(summary = "일정 리스트 조회", description = "나의 전체 일정을 조회할 수 있습니다.")
//    @GetMapping("/registrationList")
    public Page<MntiReserListOutput> registrationList(@RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "10") int size) throws Exception {

        log.debug("[START]  /registrationList");
        Pageable pageable = PageRequest.of(page, size);

        Page<MntiReserListOutput> output = mntiReserListService.execute(pageable);

        return output;
    }

//    @Operation(summary = "일정 상세보기", description = "일정의 상세내역을 볼 수 있습니다.")
//    @PostMapping("/registrationDitail")
    public MntiReserOutput registrationDitail (@RequestBody MntiReserDetailInput mntiReserDetailInput) throws Exception {

        log.debug("[START]  /registrationDitail");
        MntiReserOutput output  = mntiReserDetailService.execute(mntiReserDetailInput);

        return output;
    }

//    @Operation(summary = "일정 삭제", description = "일정을 삭제합니다.")
//    @PostMapping("/registrationDelete")
    public ResponseEntity<?> registrationDelete (@RequestBody MntiReserDetailInput mntiReserDetailInput) throws  Exception {

        log.debug("[START]  /registrationDelete");
        mntiReserDeleteService.execute(mntiReserDetailInput);

        return ResponseEntity.ok("취소 완료");
    }
}
