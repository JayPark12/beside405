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
        List<MntiEntity> mntiShufList = mntiRepository.findByMntiSerch(mntiSearchInput.getMnti_name());
        List<MntiEntity> mntiShufList7 =mntiShufList.stream().limit(7).collect(Collectors.toList());
        for (int i = 0; i < mntiShufList7.size(); i++)
        {
            String potoUrl = null;
            MntiListOutput mntiOutput = new MntiListOutput();
            mntiOutput.setMnti_name(mntiShufList7.get(i).getMntiName());
            mntiOutput.setMnti_list_no(mntiShufList7.get(i).getMntilistNo());
            mntiOutput.setMnti_leb(mntiShufList7.get(i).getMntiLeb());
            potoUrl = mntiListService.callExternalApi(mntiOutput.getMnti_list_no());
            mntiOutput.setMnti_add(mntiShufList7.get(i).getMntiAdd());

            mntiOutput.setPoto_url(potoUrl);
            mntiListOutput.add(mntiOutput);
        }

        return mntiListOutput;
    }
}
