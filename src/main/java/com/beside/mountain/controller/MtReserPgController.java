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

    private final MntiDetailService mntiDetailService;
    private final MntiResrService mntiResrService;
    private final JwtProvider jwtProvider;

    @PostMapping("/registrationSet")
    public MntiDetailOutput registrationSet(@RequestBody MntiDetailInput mntiDetailInput) throws Exception {
       ; // 토큰값 확인
       MntiDetailOutput mntiList = mntiDetailService.readJsonFile(mntiDetailInput);

       return mntiList ;
    }

    @PostMapping("/registration")
    public MntiReserOutput registration(@RequestBody MntiReserInput mntiReserInput) throws Exception {

        MntiReserOutput mntiList = mntiResrService.reserJsonFile(mntiReserInput);
        //등산 예약 정보 insert

        mntiResrService.reserInsert(mntiList, mntiReserInput);

        return mntiList;

    }
}
