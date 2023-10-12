package com.example.weather_app.Weather.Controller;

import com.example.weather_app.Weather.domain.Weather;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@RestController
public class WeatherController {

    private String latitude = "60"; // 위도
    private String longitude = "125"; // 경도

    @Value("${weather-API-Key}")
    private String serviceKey;

    @RequestMapping(value = "/curr-weather")
    @CrossOrigin(origins = "*")
    public String currWeather(@RequestBody Weather weather) throws IOException {
        String latitude = weather.getLatitude();
        String longitude = weather.getLongitude();
        if (weather.getLatitude().equals("") || weather.getLatitude() == null) {
            latitude = this.latitude;
        }
        if (weather.getLongitude().equals("") || weather.getLongitude() == null) {
            longitude = this.longitude;
        }
        URL url = new URL(String.format(
                "https://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&appid=%s&lang=kr&units=Metric", latitude,
                longitude, serviceKey));
        // HttpURLConnection 객체를 만들어 API를 호출합니다.
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        // 요청 방식을 GET으로 설정합니다.
        con.setRequestMethod("GET");
        // 요청 헤더를 설정합니다. 여기서는 Content-Type을 application/json으로 설정합니다.
        con.setRequestProperty("Content-Type", "application/json");

        // API의 응답을 읽기 위한 BufferedReader를 생성합니다.
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        // 응답을 한 줄씩 읽어들이면서 StringBuffer에 추가합니다.
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        // BufferedReader를 닫습니다.
        in.close();

        System.out.println(response);

        // 응답을 출력합니다.
        return response.toString();
    }
}