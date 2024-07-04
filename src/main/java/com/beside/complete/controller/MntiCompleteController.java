package com.beside.complete.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "3.산등산완료", description = "등산완료 관련 API")
@RequestMapping("/complete")
public class MntiCompleteController {

}
