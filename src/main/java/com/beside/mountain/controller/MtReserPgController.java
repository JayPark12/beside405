package com.beside.mountain.controller;

import com.beside.user.domain.UserEntity;
import com.beside.mountain.dto.MntiDetailInput;
import com.beside.mountain.dto.MntiDetailOutput;
import com.beside.mountain.dto.MntiReserInput;
import com.beside.mountain.dto.MntiReserOutput;
import com.beside.jwt.JwtProvider;
import com.beside.mountain.service.MntiDetailService;
import com.beside.mountain.service.MntiResrService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/reser")
public class MtReserPgController {

    private final MntiResrService mntiResrService;

    @PostMapping("/registration")
    public MntiReserOutput registration(@RequestBody MntiReserInput mntiReserInput) throws Exception {

        log.debug("[START]  /registration, INPUT = [{}]", mntiReserInput.toString());

        MntiReserOutput output = mntiResrService.execute(mntiReserInput);

        return output;
    }

    @PostMapping("/registrationList")
    public MntiReserOutput registrationList(@RequestBody MntiReserInput mntiReserInput) throws Exception {

        log.debug("[START]  /registrationList, INPUT = [{}]", mntiReserInput.toString());

        MntiReserOutput output = mntiResrService.execute(mntiReserInput);

        return output;
    }

}
