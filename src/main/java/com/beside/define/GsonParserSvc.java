package com.beside.define;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GsonParserSvc {
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    public String PotoGsonParser(String response){
        String imgFilename = null;
        List<String> imgFilenameList = new ArrayList<>();
        try {
            JsonNode rootNode = objectMapper.readTree(response);

            JsonNode itemsNode = rootNode.path("response").path("body").path("items").path("item");

            if (itemsNode.isArray()) {
                for (JsonNode item : itemsNode) {
                    imgFilename = item.path("imgfilename").asText();
                    imgFilenameList.add(imgFilename);
                }
            }
            if (imgFilenameList.size() > 1) {   // 한개 이상이면 첫번째 사진 나중에 예약하기 들어갈때 다건으로 들어가야함
                imgFilename = imgFilenameList.get(0);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return imgFilename;
    }

    public List<String> GsonParserPotolList(String  mntilistNo) throws URISyntaxException {
        String url = Define.potoUrl + "mntiListNo="+mntilistNo; // 외부 API URL
        URI uri = new URI(url);
        String imgFilename = null;
        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
        List<String> imgFilenameList = new ArrayList<>();
        try {
            JsonNode rootNode = objectMapper.readTree(response.getBody());

            JsonNode itemsNode = rootNode.path("response").path("body").path("items").path("item");

            if (itemsNode.isArray()) {
                for (JsonNode item : itemsNode) {
                    imgFilename = item.path("imgfilename").asText();
                    imgFilenameList.add(imgFilename);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return imgFilenameList;
    }
    //산 높이
    public List<String> MntiInfo(String mntiName) throws Exception {
        String url = Define.mntiApiUrl + "searchWrd=" + mntiName; // 외부 API URL

        String mntiHight = null;
        String mntiAdd = null;
        List<String> mntiInfoList = new ArrayList<>();
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        try {
            JsonNode rootNode = objectMapper.readTree(response.getBody());
            JsonNode itemsNode = rootNode.path("response").path("body").path("items").path("item");

            mntiHight = itemsNode.path("mntihigh").asText();
            mntiAdd   = itemsNode.path("mntiadd").asText();


            mntiInfoList.add(mntiHight);
            mntiInfoList.add(mntiAdd);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return mntiInfoList;
    }
}
