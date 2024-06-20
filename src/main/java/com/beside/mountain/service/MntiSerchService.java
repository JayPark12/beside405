package com.beside.mountain.service;

import com.beside.mountain.domain.MntiEntity;
import com.beside.mountain.dto.MntiListOutput;
import com.beside.mountain.dto.MntiSearchInput;
import com.beside.mountain.repository.MntiRepository;
import com.beside.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MntiSerchService {

    private final MntiRepository mntiRepository;
    private final CommonUtil commonUtil;

    public Page<MntiListOutput> mntiList(MntiSearchInput mntiSearchInput, Pageable pageable) throws URISyntaxException {
        List<MntiListOutput> mntiListOutput = new ArrayList<>();
        List<MntiEntity> mntiShufList = mntiRepository.findByMntiSerch(mntiSearchInput.getMntiName());
        List<MntiEntity> mntiShufListPaged = mntiShufList.stream()
                .skip(pageable.getOffset())
                .limit(pageable.getPageSize())
                .collect(Collectors.toList());
        for (MntiEntity mntiEntity : mntiShufListPaged) {
            List<String> potoFileSelect = commonUtil.potoFile(mntiEntity.getMntiListNo(), mntiEntity.getMntiName());
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
