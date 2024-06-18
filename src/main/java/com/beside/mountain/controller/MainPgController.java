package com.beside.mountain.controller;

import com.beside.mountain.dto.MntiListOutput;
import com.beside.mountain.dto.MntiSearchInput;
import com.beside.mountain.service.MntiListService;
import com.beside.mountain.service.MntiSerchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public Page<MntiListOutput> getMntiList(@RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size) throws URISyntaxException {
        Pageable pageable = PageRequest.of(page, size);
        return mntiListService.mntiList(pageable);
    }

    @PostMapping("/mntiSerch")
    public Page<MntiListOutput> mntiSerch(@RequestBody MntiSearchInput mntiSearchInput,
                                          @RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "10")int size) throws Exception {
        Pageable pageable = PageRequest.of(page, size);

        return mntiSerchService.mntiList(mntiSearchInput,pageable);
    }
}
