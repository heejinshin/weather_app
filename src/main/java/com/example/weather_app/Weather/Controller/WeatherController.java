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
import java.util.HashMap;
import java.util.Map;

@RestController
public class WeatherController {

    private String latitude = "60"; // 위도
    private String longitude = "125"; // 경도

    @Value("${weather-API-Key}")
    private String serviceKey;

    @Value("${papago-client-ID}")
    private String clientId;

    @Value("${papago-client-SECRET}")
    private String clientSecret;

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

        JSONObject jo = new JSONObject(strBuffer.toString());

        // 필요한 값만 추출
        JSONArray weaArr = (JSONArray) jo.get("weather");
        JSONObject weaObj = weaArr.getJSONObject(0);
        JSONObject mainObj = (JSONObject) jo.get("main");
        JSONObject windObj = (JSONObject) jo.get("wind");
        JSONObject response = new JSONObject();

        String description = (String) weaObj.get("description");
        Integer rowId = (Integer) weaObj.get("id");
        double id = rowId.doubleValue();
        id = Math.floor(id * 0.01) * 100; // 100 자리로 보여주기 위해 컨버팅 처리

        String regionName = (String) jo.get("name");

        // 지역명 영어에서 한글로 번역
        try {
            regionName = URLEncoder.encode(regionName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("인코딩 실패", e);
        }

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("X-Naver-Client-Id", clientId);
        requestHeaders.put("X-Naver-Client-Secret", clientSecret);

        JSONObject papagoResult = new JSONObject(post("https://openapi.naver.com/v1/papago/n2mt", requestHeaders, regionName));
        String name = (String)((JSONObject)((JSONObject) papagoResult.get("message")).get("result")).get("translatedText");

        BigDecimal temp = (BigDecimal) mainObj.get("temp");
        BigDecimal feels_like = (BigDecimal) mainObj.get("feels_like");
        BigDecimal temp_min = (BigDecimal) mainObj.get("temp_min");
        BigDecimal temp_max = (BigDecimal) mainObj.get("temp_max");
        BigDecimal speed = (BigDecimal) windObj.get("speed");

        BigDecimal rain_1h = new BigDecimal(0);
        BigDecimal snow_1h = new BigDecimal(0);
        {
            if(!jo.isNull("rain")) {
                rain_1h = (BigDecimal) ((JSONObject) jo.get("rain")).get("1h");
            }
            if(!jo.isNull("snow")) {
                snow_1h = (BigDecimal) ((JSONObject) jo.get("rain")).get("1h");
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

        return response.toString();
    }

    private String post(String apiUrl, Map<String, String> requestHeaders, String text){
        HttpURLConnection con = connect(apiUrl);
        String postParams = "source=en&target=ko&text=" + text; //원본언어: 한국어 (ko) -> 목적언어: 영어 (en)
        try {
            con.setRequestMethod("POST");
            for(Map.Entry<String, String> header :requestHeaders.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }

            con.setDoOutput(true);
            try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
                wr.write(postParams.getBytes());
                wr.flush();
            }

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 응답
                return readBody(con.getInputStream());
            } else {  // 에러 응답
                return readBody(con.getErrorStream());
            }
        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } finally {
            con.disconnect();
        }
    }

    private HttpURLConnection connect(String apiUrl){
        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection)url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
        }
    }

    private String readBody(InputStream body){
        InputStreamReader streamReader = new InputStreamReader(body);

        try (BufferedReader lineReader = new BufferedReader(streamReader)) {
            StringBuilder responseBody = new StringBuilder();

            String line;
            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }

            return responseBody.toString();
        } catch (IOException e) {
            throw new RuntimeException("API 응답을 읽는데 실패했습니다.", e);
        }
    }
}