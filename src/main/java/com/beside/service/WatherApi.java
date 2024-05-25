package com.beside.service;

import com.beside.define.Define;
import com.beside.model.Wather;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class WatherApi {

    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;


    public Wather watherListToday() throws URISyntaxException {
        String localDate ;
        if(LocalTime.now().isBefore(LocalTime.of(5, 0))){
            localDate = LocalDateTime.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        }else {
            localDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        }
        String url = Define.toDayWather + "base_date=" + localDate + "&base_time=0500"+ "&nx=60" + "&ny=127" +"&dataType=JSON"; // 외부 API URL
        Wather wather = new Wather();
        String weatherName = null;
        String weatherCode = null;
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        try {
            JsonNode rootNode = objectMapper.readTree(response.getBody());

            JsonNode itemsNode = rootNode.path("response").path("body").path("items").path("item");

            for (int i =0; i < itemsNode.size(); i++  ) {
                if (itemsNode.get(i).path("category").asText().equals("POP")) {
                    wather.setRain_persent(String.valueOf(itemsNode.get(i).path("fcstValue").asText()));
                }
                if(itemsNode.get(i).path("category").asText().equals("PTY"))  {
                    weatherCode= String.valueOf(itemsNode.get(i).path("fcstValue").asText());
                    switch (weatherCode) {
                        case "0":
                            weatherName = "없음";
                            break;
                        case "1":
                            weatherName = "비";
                            break;
                        case "2":
                            weatherName = "비/눈";
                            break;
                        case "3":
                            weatherName = "눈";
                            break;
                        case "4":
                            weatherName = "소나기";
                            break;
                    }
                    wather.setRain_type(weatherName);
                }
                if (itemsNode.get(i).path("category").asText().equals("SKY")) {
                    weatherCode= String.valueOf(itemsNode.get(i).path("fcstValue").asText());
                    switch (weatherCode) {
                        case "0":
                            weatherName = "없음";
                            break;
                        case "1":
                            weatherName = "맑음";
                            break;
                        case "3":
                            weatherName = "구름많음";
                            break;
                        case "4":
                            weatherName = "흐림";
                            break;
                    }
                    wather.setSky_state(weatherName);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return wather;
    }

    public List<Wather>  watherListOrtherDay(List<Wather> watherList) throws Exception {
        String localDate =null;
        if(LocalTime.now().isBefore(LocalTime.of(18, 0))){
            localDate = LocalDateTime.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        }else {
            localDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        }
        String url = Define.afterWather + "tmFc=" + localDate + "1800"+ "&regId=11B00000"+ "&dataType=JSON"; // 외부 API URL
        Wather wather = new Wather();

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        try {
            JsonNode rootNode = objectMapper.readTree(response.getBody());
            JsonNode itemsNode = rootNode.path("response").path("body").path("items").path("item").get(0);

            wather.setRain_persent(itemsNode.path("rnSt3Am").asText());
            wather.setSky_state(itemsNode.path("wf3Am").asText());
            watherList.add(wather);
            wather.setRain_persent(itemsNode.path("rnSt4Am").asText());
            wather.setSky_state(itemsNode.path("wf4Am").asText());
            watherList.add(wather);
            wather.setRain_persent(itemsNode.path("rnSt5Am").asText());
            wather.setSky_state(itemsNode.path("wf5Am").asText());
            watherList.add(wather);
            wather.setRain_persent(itemsNode.path("rnSt6Am").asText());
            wather.setSky_state(itemsNode.path("wf6Am").asText());
            watherList.add(wather);
            wather.setRain_persent(itemsNode.path("rnSt7Am").asText());
            wather.setSky_state(itemsNode.path("wf7Am").asText());
            watherList.add(wather);
            wather.setRain_persent(itemsNode.path("rnSt8").asText());
            wather.setSky_state(itemsNode.path("wf8").asText());
            watherList.add(wather);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return watherList;
    }
}
