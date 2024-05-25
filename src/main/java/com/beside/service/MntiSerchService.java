package com.beside.service;

import com.beside.DAO.MntiDao;
import com.beside.Entity.MntiEntity;
import com.beside.model.MntiListOutput;
import com.beside.model.MntiSearchInput;
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

    private final MntiDao mntiDao;
    private final MntiListService mntiListService;

    public List<MntiListOutput> mntiList(MntiSearchInput mntiSearchInput) throws URISyntaxException {
        List<MntiListOutput> mntiListOutput = new ArrayList<>();
        List<MntiEntity> mntiShufList = mntiDao.findByMntiSerch(mntiSearchInput.getMnti_name());
        List<MntiEntity> mntiShufList7 =mntiShufList.stream().limit(7).collect(Collectors.toList());
        for (int i = 0; i < mntiShufList7.size(); i++)
        {
            String potoUrl = null;
            MntiListOutput mntiOutput = new MntiListOutput();
            mntiOutput.setMnti_name(mntiShufList7.get(i).getMntiName());
            mntiOutput.setMnti_list_no(mntiShufList7.get(i).getMntilistNo());
            mntiOutput.setMnti_reb(mntiShufList7.get(i).getMntiReb());
            potoUrl = mntiListService.callExternalApi(mntiOutput.getMnti_list_no());
            mntiOutput.setMnti_add(mntiShufList7.get(i).getMntiAdd());

            mntiOutput.setPoto_url(potoUrl);
            mntiListOutput.add(mntiOutput);
        }

        return mntiListOutput;
    }
}
