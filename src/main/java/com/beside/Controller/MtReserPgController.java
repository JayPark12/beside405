package com.beside.Controller;

import com.beside.model.MntiDetailInput;
import com.beside.model.MntiDetailOutput;
import com.beside.service.MntiDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/reser")
public class MtReserPgController {

    private final MntiDetailService mntiDetailService;

    @PostMapping("/registration")

    public MntiDetailOutput reservation(@RequestBody MntiDetailInput mntiDetailInput) throws Exception {
       MntiDetailOutput mntiList = mntiDetailService.readJsonFile(mntiDetailInput);

       return mntiList ;
    }
}
