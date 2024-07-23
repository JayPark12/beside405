package com.beside.weather.api;

import com.beside.define.Define;
import com.beside.weather.dto.Weather;
import com.beside.weather.dto.WeatherResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
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

    private String encodingServiceKey = "QwBnXKXGpEVCOs%2FqPD4gm8IHUTeypn4Css4kxLn%2FmxFhO1PA%2Bkf69ydEVVkuItSaTVzMYkWJUy%2FPTIqMSG%2Fg9A%3D%3D";
    private String decodingServiceKey = "QwBnXKXGpEVCOs/qPD4gm8IHUTeypn4Css4kxLn/mxFhO1PA+kf69ydEVVkuItSaTVzMYkWJUy/PTIqMSG/g9A==";

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

    public WeatherResponse get3DayWeather(String localDate, int number) throws IOException, URISyntaxException {
        WeatherResponse weatherResponse = new WeatherResponse();

        String url2 = "https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst?serviceKey=QwBnXKXGpEVCOs%2FqPD4gm8IHUTeypn4Css4kxLn%2FmxFhO1PA%2Bkf69ydEVVkuItSaTVzMYkWJUy%2FPTIqMSG%2Fg9A%3D%3D&base_date=" + localDate + "&base_time=0500&nx=60&ny=127&dataType=JSON&pageNo="+ number +"&numOfRows=100";

        // URL 객체 생성
        URL url = new URL(url2);

        // HttpURLConnection 객체 생성 및 설정
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");


        // 응답 코드 확인
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 응답
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // JSON 데이터 출력 (혹은 저장)
            String jsonResponse = response.toString();
            System.out.println("Response JSON: " + jsonResponse);

//             JSON 파싱
            weatherResponse = parseJson(jsonResponse, localDate);

        } else {
            System.out.println("GET request not worked");
        }
        connection.disconnect();
        return weatherResponse;
    }


    public WeatherResponse parseJson(String jsonResponse, String localDate) {
        WeatherResponse weatherResponse = new WeatherResponse();
        weatherResponse.setDate(localDate);
        try {
            // 문자열을 JSON 객체로 변환
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            JsonNode itemsNode = rootNode.path("response").path("body").path("items").path("item");

            for(JsonNode itemNode : itemsNode) {
                String category = itemNode.path("category").asText();
                String fcstTime = itemNode.path("fcstTime").asText();
                if(category.equals("TMP") && fcstTime.equals("0900")) {
                    String tmp = itemNode.path("fcstValue").asText();
                    weatherResponse.setTemperature(tmp);
                } else if(category.equals("SKY") && fcstTime.equals("0900")) {
                    String sky = itemNode.path("fcstValue").asText();
                    weatherResponse.setSkyState(sky);
                } else if(category.equals("POP") && fcstTime.equals("0900")) {
                    String pop = itemNode.path("fcstValue").asText();
                    weatherResponse.setRainPersent(pop);
                }
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return weatherResponse;
    }




}
