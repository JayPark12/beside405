package com.beside.Controller;

import com.beside.model.MntiDetailInput;
import com.beside.model.MntiDetailOutput;
import com.beside.model.MntiReserInput;
import com.beside.model.MntiReserOutput;
import com.beside.service.JwtProvider;
import com.beside.service.MntiDetailService;
import com.beside.service.MntiResrService;
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
       jwtProvider.validToken(mntiDetailInput.getJwtToken()); // 토큰값 확인

       MntiDetailOutput mntiList = mntiDetailService.readJsonFile(mntiDetailInput);

       return mntiList ;
    }

    @PostMapping("/registration")
    public MntiReserOutput registration(@RequestBody MntiReserInput mntiReserInput) throws Exception {
        jwtProvider.validToken(mntiReserInput.getJwtToken()); // 토큰값 확인

        MntiReserOutput mntiList = mntiResrService.reserJsonFile(mntiReserInput);

        return mntiList;

    }


}
