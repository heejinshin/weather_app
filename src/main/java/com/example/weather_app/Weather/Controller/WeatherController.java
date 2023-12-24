package com.example.weather_app.Weather.Controller;

import com.example.weather_app.Weather.Domain.Weather;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@RestController
public class WeatherController {

    private String latitude = "60"; // 위도
    private String longitude = "125"; // 경도

    @Value("${weather-API-Key}")
    private String serviceKey;

    @Value("${KAKAO-REST-API-KEY}")
    private String kakaoAPIKey;

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
        StringBuffer strBuffer = new StringBuffer();

        // 응답을 한 줄씩 읽어들이면서 StringBuffer에 추가합니다.
        while ((inputLine = in.readLine()) != null) {
            strBuffer.append(inputLine);
        }
        // BufferedReader를 닫습니다.
        in.close();

        StringBuffer airPmBuffer = new StringBuffer();

        url = new URL(String.format(
                "https://api.openweathermap.org/data/2.5/air_pollution?lat=%s&lon=%s&appid=%s", latitude,
                longitude, serviceKey));

        con = (HttpURLConnection) url.openConnection();

        con.setRequestMethod("GET");
        con.setRequestProperty("Content-Type", "application/json");

        in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));

        while ((inputLine = in.readLine()) != null) {
            airPmBuffer.append(inputLine);
        }

        in.close();

        // 미세먼지 값 추출
        JSONObject airPmJO = new JSONObject(airPmBuffer.toString());
        JSONArray airPmList = (JSONArray) airPmJO.get("list");
        JSONObject airPmObj = airPmList.getJSONObject(0);
        JSONObject airPmComp = (JSONObject) airPmObj.get("components");
        BigDecimal air = (BigDecimal) airPmComp.get("pm10");

        JSONObject jo = new JSONObject(strBuffer.toString());

        // 날씨 값 추출
        JSONArray weaArr = (JSONArray) jo.get("weather");
        JSONObject weaObj = weaArr.getJSONObject(0);
        JSONObject mainObj = (JSONObject) jo.get("main");
        JSONObject windObj = (JSONObject) jo.get("wind");
        JSONObject response = new JSONObject();

        String description = (String) weaObj.get("description");
        Integer rowId = (Integer) weaObj.get("id");
        double id = rowId.doubleValue();
        id = Math.floor(id * 0.01) * 100; // 100 자리로 보여주기 위해 컨버팅 처리

        // 받아온 좌표로 정확한 주소 보내주기
        StringBuffer nameBuffer = new StringBuffer();
        url = new URL(String.format(
                "https://dapi.kakao.com/v2/local/geo/coord2regioncode.json?x=%s&y=%s",
                longitude, latitude));

        con = (HttpURLConnection) url.openConnection();

        con.setRequestMethod("GET");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Authorization", "KakaoAK " + kakaoAPIKey);

        in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));

        while ((inputLine = in.readLine()) != null) {
            nameBuffer.append(inputLine);
        }

        in.close();

        JSONObject areaJO = new JSONObject(nameBuffer.toString());
        String areaName = (String)((JSONArray) areaJO.get("documents")).getJSONObject(0).get("region_3depth_name");

        Object name = areaName;

        Object temp = mainObj.get("temp");

        Object feels_like = new BigDecimal(0);
        {
            if (!mainObj.isNull("feels_like")) {
                feels_like = mainObj.get("feels_like");
            }
        }
        Object temp_min = mainObj.get("temp_min");
        Object temp_max = mainObj.get("temp_max");
        Object speed = windObj.get("speed");

        Object rain_1h = 0;
        Object snow_1h = 0;
        {
            if(!jo.isNull("rain")) {
                rain_1h = ((JSONObject) jo.get("rain")).get("1h");
            }
            if(!jo.isNull("snow")) {
                snow_1h = ((JSONObject) jo.get("rain")).get("1h");
            }
        }

        response.put("name", name);
        response.put("id", id);
        response.put("description", description);
        response.put("temp", temp);
        response.put("feels_like", feels_like);
        response.put("temp_min", temp_min);
        response.put("temp_max", temp_max);
        response.put("speed", speed);
        response.put("rain_1h", rain_1h);
        response.put("snow_1h", snow_1h);
        response.put("air", air);

        return response.toString();
    }

    @RequestMapping(value = "/list-weather")
    @CrossOrigin(origins = "*")
    public String listWeather(@RequestBody ArrayList<Weather> listWeather) throws IOException {

        ArrayList<String> result = new ArrayList<>();
        Weather weather = new Weather();

        for(int i = 0; i < listWeather.size(); i++) {
            weather.setLatitude(listWeather.get(i).getLatitude());
            weather.setLongitude(listWeather.get(i).getLongitude());
            String currWeather = currWeather(weather);
            result.add(currWeather);
        }

        return result.toString();
    }
}