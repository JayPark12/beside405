package com.beside.Controller;

import com.beside.model.MntiListOutput;
import com.beside.model.MntiSearchInput;
import com.beside.service.MntiListService;
import com.beside.service.MntiSerchService;
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
