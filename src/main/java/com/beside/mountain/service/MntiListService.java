package com.beside.mountain.service;

import com.beside.mountain.domain.MntiEntity;
import com.beside.mountain.dto.MntiListOutput;
import com.beside.mountain.repository.MntiRepository;
import com.beside.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MntiListService {

    private final MntiRepository mntiRepository;

    public Page<MntiListOutput> mntiList(Pageable pageable) throws URISyntaxException {
        List<MntiListOutput> mntiListOutput = new ArrayList<>();
        List<MntiEntity> mntiShufList = mntiRepository.findByMnti();
        List<MntiEntity> mntiShufListPaged = mntiShufList.stream()
                .skip(pageable.getOffset())
                .limit(pageable.getPageSize())
                .collect(Collectors.toList());
        for (MntiEntity mntiEntity : mntiShufListPaged) {
            List<String> potoFileSelect = CommonUtil.potoFile(mntiEntity.getMntiListNo(), mntiEntity.getMntiName());
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


    public Page<MntiListOutput> getList(Pageable pageable, String keyword) {
        List<MntiListOutput> mntiListOutput = new ArrayList<>();
        List<MntiEntity> list;

        if(StringUtils.hasText(keyword)) {
            list = mntiRepository.findByMntiNameContaining(keyword);
        } else {
            list = mntiRepository.findAll();
        }

        for(MntiEntity mntiEntity : list){
            MntiListOutput dto = new MntiListOutput();
            dto.setMntiName(mntiEntity.getMntiName());
            dto.setMntiListNo(mntiEntity.getMntiListNo());
            dto.setMntiLevel(mntiEntity.getMntiLevel());
            dto.setMntiAdd(mntiEntity.getMntiAdd());
            mntiListOutput.add(dto);
        }

        mntiListOutput.sort(Comparator.comparing(MntiListOutput::getMntiName));

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), list.size());
        return new PageImpl<>(mntiListOutput.subList(start, end), pageable, mntiListOutput.size());
    }


}
