# API 명세서

### weather API 목록 및 사용법을 기재함

- url: http://localhost:8080
- info:
  - title: weather API
  - contact:
    - github: skyni25
- license:
  - name: Apache 2.0
  - url: 'http://www.apache.org/licenses/LICENSE-2.0.html'

### API 목록

- 요청 경로:

| 경로(요청 메소드)    | 설명                                                                      | 요청인자                                    | 응답값                                                                  |
| -------------------- | ------------------------------------------------------------------------- | ------------------------------------------- | ----------------------------------------------------------------------- |
| /curr-weather (POST) | 위도, 경도를 HTTP post method로 전달하면 해당 위치의 날씨 정보를 반환해줌 | { latitude(string), longitude(string) }     | 성공: JSON 데이터 반환,<br/>실패: Bad request 문자열 반환               |
| /list-weather (POST) | 위도, 경도를 배열로 전달하면 각 위치들의 날씨 정보를 배열로 반환해줌      | [ { latitude(string), longitude(string) } ] | 성공: JSON 데이터를 포함한 배열 반환,<br/>실패: Bad request 문자열 반환 |
