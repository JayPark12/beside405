package com.beside.weather.api;

import com.beside.define.Define;
import com.beside.weather.dto.Weather;
import com.beside.weather.dto.WeatherResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
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

    private String encodingServiceKey = "ceksjLBrysIZ1BMFMtgb8drfzaeqJ0tMJUZ2PbtgtrhaB3sjghNnAdbkRKSUyHUSreI%2BQhI5lxoQUY7yxcEd3A%3D%3D";
    private String decodingServiceKey = "ceksjLBrysIZ1BMFMtgb8drfzaeqJ0tMJUZ2PbtgtrhaB3sjghNnAdbkRKSUyHUSreI+QhI5lxoQUY7yxcEd3A==";

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
                    case "POP": //강수확률
                        weather.setRain_persent(fcstValue);
                        break;
                    case "PTY": //강수형태
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
                    case "SKY": //하늘상태
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

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public WeatherResponse getTodayWeather() throws MalformedURLException, URISyntaxException {
        String localDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
//        String apiUrl = Define.url +"?serviceKey=" + serviceKey + "&base_date=" + localDate + "&base_time=0500"+ "&nx=60" + "&ny=127" + "&dataType=JSON";

        String apiUrl = UriComponentsBuilder.fromHttpUrl(Define.url)
                .queryParam("serviceKey", decodingServiceKey)
                .queryParam("base_date", localDate)
                .queryParam("base_time", "0500")
                .queryParam("nx", "60")
                .queryParam("ny", "127")
                .queryParam("dataType", "JSON")
                .toUriString();

        URI uri = new URI(apiUrl);
        System.out.println(apiUrl);

        // HTTP GET 요청을 보내고, 응답을 ResponseEntity<String>로 받음
        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
//        String jsonString = restTemplate.getForObject(uri, String.class);

        // 응답 본문을 가져옴
        String responseBody = response.getBody();

        // JSON 응답을 Java 객체로 변환하기 위해 ObjectMapper 사용
        ObjectMapper objectMapper = new ObjectMapper();

        WeatherResponse weatherResponse = new WeatherResponse();

        try {
            JsonNode rootNode = objectMapper.readTree(response.getBody());
            JsonNode itemsNode = rootNode.path("response").path("body").path("items").path("item");

            for (JsonNode item : itemsNode) {
                String category = item.path("category").asText();
                String fcstValue = item.path("fcstValue").asText();

                switch (category) {
                    case "POP": //강수확률
                        weatherResponse.setRainPersent(fcstValue);
                        break;
//                    case "PTY": //강수형태
//                        weatherCode = fcstValue;
//                        weatherName = switch (weatherCode) {
//                            case "0" -> "없음";
//                            case "1" -> "비";
//                            case "2" -> "비/눈";
//                            case "3" -> "눈";
//                            case "4" -> "소나기";
//                            default -> weatherName;
//                        };
//                        weather.setRain_type(weatherName);
//                        break;
                    case "TMP": // 기온
                        weatherResponse.setTemperature(fcstValue);
                        break;
                    case "SKY": //하늘상태
                        weatherResponse.setSkyState(fcstValue);
                        break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return weatherResponse;
    }



























}
