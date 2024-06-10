package com.beside.mountain.controller;

import com.beside.mountain.dto.MntiDetailInput;
import com.beside.mountain.dto.MntiDetailOutput;
import com.beside.mountain.service.MntiDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/detail")
public class MtDetailPgController {

    private final MntiDetailService mntiDetailService;

    @PostMapping("/mtDetail")

    public MntiDetailOutput mntiList(@RequestBody MntiDetailInput mntiDetailInput) throws Exception {
       MntiDetailOutput mntiList = mntiDetailService.readJsonFile(mntiDetailInput);

       return mntiList ;
    }
}
