package com.beside.service;

import com.beside.DAO.MntiDao;
import com.beside.Entity.MntiEntity;
import com.beside.define.Define;
import com.beside.define.GsonParserSvc;
import com.beside.model.MntiListOutput;
import com.beside.model.MntiSearchInput;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MntiListService {

    private final MntiDao mntiDao;
    //private final RestTemplateConfig restTemplateConfig;
    private final RestTemplate restTemplate;
    private final GsonParserSvc gsonParserSvc;

    public List<MntiListOutput> mntiList() throws URISyntaxException {
        List<MntiListOutput> mntiListOutput = new ArrayList<>();
        List<MntiEntity> mntiShufList = mntiDao.findByMnti();
        Collections.shuffle(mntiShufList);
        List<MntiEntity> mntiShufList7 =mntiShufList.stream().limit(7).collect(Collectors.toList());
        for (int i = 0; i < mntiShufList7.size(); i++)
        {
            String potoUrl = null;
            MntiListOutput mntiOutput = new MntiListOutput();
            mntiOutput.setMntiName(mntiShufList7.get(i).getMntiName());
            mntiOutput.setMntilistNo(mntiShufList7.get(i).getMntilistNo());
            mntiOutput.setMntiReb(mntiShufList7.get(i).getMntiReb());
            potoUrl = callExternalApi(mntiOutput.getMntilistNo());

            mntiOutput.setPotoUrl(potoUrl);
            mntiListOutput.add(mntiOutput);
        }

        return mntiListOutput;
    }

    public String callExternalApi(String mntilistNo) throws URISyntaxException {
        String url = Define.apiUrl + "mntiListNo="+mntilistNo; // 외부 API URL
        String potoInfo = null;
        URI uri = new URI(url);
        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);

        String jpgFileName =  gsonParserSvc.GsonParser(response.getBody());//api에서 데이터 가져온 처리

        if (jpgFileName != null) {
            potoInfo = Define.potoCallUrl + jpgFileName;
        }

        return potoInfo;//이미지 불러오는 api call
    }
}
