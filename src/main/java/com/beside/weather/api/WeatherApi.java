package com.beside.weather.api;

import com.beside.define.Define;
import com.beside.weather.dto.Weather;
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
public class WeatherApi {

    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;


    public Weather watherListToday() throws URISyntaxException {
        String localDate ;
        if(LocalTime.now().isBefore(LocalTime.of(5, 0))){
            localDate = LocalDateTime.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        }else {
            localDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        }
        String url = Define.toDayWather + "base_date=" + localDate + "&base_time=0500"+ "&nx=60" + "&ny=127" +"&dataType=JSON"; // 외부 API URL
        Weather weather = new Weather();
        String weatherName = null;
        String weatherCode = null;
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        try {
            JsonNode rootNode = objectMapper.readTree(response.getBody());
            JsonNode itemsNode = rootNode.path("response").path("body").path("items").path("item");

            for (JsonNode item : itemsNode) {
                String category = item.path("category").asText();
                String fcstValue = item.path("fcstValue").asText();

                switch (category) {
                    case "POP":
                        weather.setRain_persent(fcstValue);
                        break;
                    case "PTY":
                        weatherCode = fcstValue;
                        weatherName = switch (weatherCode) {
                            case "0" -> "없음";
                            case "1" -> "비";
                            case "2" -> "비/눈";
                            case "3" -> "눈";
                            case "4" -> "소나기";
                            default -> weatherName;
                        };
                        weather.setRain_type(weatherName);
                        break;
                    case "SKY":
                        weatherCode = fcstValue;
                        weatherName = switch (weatherCode) {
                            case "0" -> "없음";
                            case "1" -> "맑음";
                            case "3" -> "구름많음";
                            case "4" -> "흐림";
                            default -> weatherName;
                        };
                        weather.setSky_state(weatherName);
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return weather;
    }

    public List<Weather>  watherListOrtherDay(List<Weather> weatherList) throws Exception {
        String localDate =null;
        if(LocalTime.now().isBefore(LocalTime.of(18, 0))){
            localDate = LocalDateTime.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        }else {
            localDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        }
        String url = Define.afterWather + "tmFc=" + localDate + "1800"+ "&regId=11B00000"+ "&dataType=JSON"; // 외부 API URL
        Weather weather = new Weather();

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        try {
            JsonNode rootNode = objectMapper.readTree(response.getBody());
            JsonNode itemsNode = rootNode.path("response").path("body").path("items").path("item").get(0);

            weather.setRain_persent(itemsNode.path("rnSt3Am").asText());
            weather.setSky_state(itemsNode.path("wf3Am").asText());
            weatherList.add(weather);
            weather.setRain_persent(itemsNode.path("rnSt4Am").asText());
            weather.setSky_state(itemsNode.path("wf4Am").asText());
            weatherList.add(weather);
            weather.setRain_persent(itemsNode.path("rnSt5Am").asText());
            weather.setSky_state(itemsNode.path("wf5Am").asText());
            weatherList.add(weather);
            weather.setRain_persent(itemsNode.path("rnSt6Am").asText());
            weather.setSky_state(itemsNode.path("wf6Am").asText());
            weatherList.add(weather);
            weather.setRain_persent(itemsNode.path("rnSt7Am").asText());
            weather.setSky_state(itemsNode.path("wf7Am").asText());
            weatherList.add(weather);
            weather.setRain_persent(itemsNode.path("rnSt8").asText());
            weather.setSky_state(itemsNode.path("wf8").asText());
            weatherList.add(weather);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return weatherList;
    }
}
