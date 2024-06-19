package com.beside.reservation.controller;

import com.beside.reservation.dto.MntiReserListOutput;
import com.beside.reservation.service.MntiReserDetailService;
import com.beside.reservation.service.MntiReserListService;
import com.beside.reservation.dto.MntiReserInput;
import com.beside.reservation.dto.MntiReserOutput;
import com.beside.reservation.service.MntiReserInsertService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/reser")
public class MtReserPgController {

    private final MntiReserInsertService mntiReserInsertService;
    private final MntiReserListService mntiReserListService;
    private final MntiReserDetailService mntiReserDetailService;

    @PostMapping("/registration")
    public MntiReserOutput registration(@RequestBody MntiReserInput mntiReserInput) throws Exception {

        log.debug("[START]  /registration, INPUT = [{}]", mntiReserInput.toString());

        MntiReserOutput output = mntiReserInsertService.execute(mntiReserInput);

        return output;
    }

    @GetMapping("/registrationList")
    public Page<MntiReserListOutput> registrationList(@RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "10") int size) throws Exception {

        log.debug("[START]  /registrationList");
        Pageable pageable = PageRequest.of(page, size);

        Page<MntiReserListOutput> output = mntiReserListService.execute(pageable);

        return output;
    }

    @PostMapping("/registrationDitail")
    public MntiReserOutput registrationDitail (@RequestBody MntiReserInput mntiReserInput) throws Exception {

        MntiReserOutput output  = mntiReserDetailService.execute(mntiReserInput);

        return output;
    }
}
