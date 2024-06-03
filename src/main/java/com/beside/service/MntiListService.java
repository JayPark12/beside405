package com.beside.service;

import com.beside.Entity.MntiEntity;
import com.beside.define.Define;
import com.beside.define.GsonParserSvc;
import com.beside.model.MntiListOutput;
import com.beside.repository.MntiRepository;
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

    private final MntiRepository mntiRepository;
    //private final RestTemplateConfig restTemplateConfig;
    private final RestTemplate restTemplate;
    private final GsonParserSvc gsonParserSvc;

    public List<MntiListOutput> mntiList() throws URISyntaxException {
        List<MntiListOutput> mntiListOutput = new ArrayList<>();
        List<MntiEntity> mntiShufList = mntiRepository.findByMnti();
        Collections.shuffle(mntiShufList);
        List<MntiEntity> mntiShufList7 =mntiShufList.stream().limit(7).collect(Collectors.toList());
        for (int i = 0; i < mntiShufList7.size(); i++)
        {
            String potoUrl = null;
            MntiListOutput mntiOutput = new MntiListOutput();
            mntiOutput.setMnti_name(mntiShufList7.get(i).getMntiName());
            mntiOutput.setMnti_list_no(mntiShufList7.get(i).getMntilistNo());
            mntiOutput.setMnti_leb(mntiShufList7.get(i).getMntiLeb());
            mntiOutput.setMnti_add(mntiShufList7.get(i).getMntiAdd());
            potoUrl = callExternalApi(mntiOutput.getMnti_list_no());   //사진정보

            mntiOutput.setPoto_url(potoUrl);
            mntiListOutput.add(mntiOutput);
        }

        return mntiListOutput;
    }

    public String callExternalApi(String mntilistNo) throws URISyntaxException {
        String url = Define.potoUrl + "mntiListNo="+mntilistNo; // 외부 API URL
        String potoInfo = null;
        URI uri = new URI(url);
        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);

        String jpgFileName =  gsonParserSvc.PotoGsonParser(response.getBody());//api에서 데이터 가져온 처리

        if (jpgFileName != null) {
            potoInfo = Define.potoCallUrl + jpgFileName;
        }

        return potoInfo;//이미지 불러오는 api call
    }
}
