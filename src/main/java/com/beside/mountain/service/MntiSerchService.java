package com.beside.mountain.service;

import com.beside.mountain.domain.MntiEntity;
import com.beside.mountain.dto.MntiListOutput;
import com.beside.mountain.dto.MntiSearchInput;
import com.beside.mountain.repository.MntiRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final MntiListService mntiListService;

    public List<MntiListOutput> mntiList(MntiSearchInput mntiSearchInput) throws URISyntaxException {
        List<MntiListOutput> mntiListOutput = new ArrayList<>();
        List<MntiEntity> mntiShufList = mntiRepository.findByMntiSerch(mntiSearchInput.getMntiName());
        List<MntiEntity> mntiShufList7 =mntiShufList.stream().limit(7).collect(Collectors.toList());
        for (int i = 0; i < mntiShufList7.size(); i++)
        {
            List<String> potoFileSelect = mntiListService.potoFile(mntiShufList7.get(i).getMntiListNo() ,mntiShufList7.get(i).getMntiName());
            MntiListOutput mntiOutput = new MntiListOutput();
            mntiOutput.setMntiName(mntiShufList7.get(i).getMntiName());
            mntiOutput.setMntiListNo(mntiShufList7.get(i).getMntiListNo());
            mntiOutput.setMntiLevel(mntiShufList7.get(i).getMntiLevel());
            mntiOutput.setMntiAdd(mntiShufList7.get(i).getMntiAdd());

            if(potoFileSelect.size() != 0) {
                mntiOutput.setPotoFile(potoFileSelect.get(0));
            }

            mntiListOutput.add(mntiOutput);
        }

        return mntiListOutput;
    }
}
