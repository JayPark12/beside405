package com.beside.mountain.service;

import com.beside.common.util.CommomUtil;
import com.beside.mountain.domain.MntiEntity;
import com.beside.define.GsonParserSvc;
import com.beside.mountain.dto.MntiListOutput;
import com.beside.mountain.repository.MntiRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class MntiListService {

    private final MntiRepository mntiRepository;
    private final CommomUtil commomUtil;

    public Page<MntiListOutput> mntiList(Pageable pageable) throws URISyntaxException {
        List<MntiListOutput> mntiListOutput = new ArrayList<>();
        List<MntiEntity> mntiShufList = mntiRepository.findByMnti();
        List<MntiEntity> mntiShufListPaged = mntiShufList.stream()
                .skip(pageable.getOffset())
                .limit(pageable.getPageSize())
                .collect(Collectors.toList());
        for (MntiEntity mntiEntity : mntiShufListPaged) {
            List<String> potoFileSelect = commomUtil.potoFile(mntiEntity.getMntiListNo(), mntiEntity.getMntiName());
            MntiListOutput mntiOutput = new MntiListOutput();
            mntiOutput.setMntiName(mntiEntity.getMntiName());
            mntiOutput.setMntiListNo(mntiEntity.getMntiListNo());
            mntiOutput.setMntiLevel(mntiEntity.getMntiLevel());
            mntiOutput.setMntiAdd(mntiEntity.getMntiAdd());

            if (!potoFileSelect.isEmpty()) {
                mntiOutput.setPotoFile(potoFileSelect.get(0));
            }

            mntiListOutput.add(mntiOutput);
        }
        return new PageImpl<>(mntiListOutput, pageable, mntiShufList.size());
    }
}
