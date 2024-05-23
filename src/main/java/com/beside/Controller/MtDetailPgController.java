package com.beside.Controller;

import com.beside.model.MntiDetailInput;
import com.beside.model.MntiDetailOutput;
import com.beside.model.MntiListOutput;
import com.beside.model.MntiSearchInput;
import com.beside.service.MntiDetailService;
import com.beside.service.MntiListService;
import com.beside.service.MntiSerchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;
import java.util.List;


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
