package com.beside.mountain.controller;

import com.beside.mountain.dto.MntiListOutput;
import com.beside.mountain.dto.MntiSearchInput;
import com.beside.mountain.service.MntiListService;
import com.beside.mountain.service.MntiSerchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/main")
public class MainPgController {

    private final MntiListService mntiListService;
    private final MntiSerchService mntiSerchService;

    @GetMapping("/mntiList")
    public List<MntiListOutput> mntiList() throws URISyntaxException {
       List<MntiListOutput> mntiList = mntiListService.mntiList();

       return mntiList ;
    }

    @GetMapping("/mntiSerch")
    public List<MntiListOutput> mntiSerch(@RequestBody MntiSearchInput mntiSearchInput) throws Exception {
        List<MntiListOutput> mntiList = mntiSerchService.mntiList(mntiSearchInput);

        return mntiList;
    }
}
