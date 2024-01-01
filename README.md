# Weathie - Backend

No icon

## Weathie v1.0

> **날씨 정보를 알려주는 모바일 웹 사이트** <br/> **개발기간: 2023.08 ~ 2023.12**

## 배포 주소

> **웹 사이트 주소: https://weathie.vercel.app/** <br /> **프론트엔드 주소: https://github.com/JeanYoungPark/weather-app**

## 팀 소개

> **권OO: 기획, 디자인**<br /> **skyni25 : 인프라, 백엔드**<br /> **JeanYoungPark: 프론트엔드**

## 프로젝트 소개

**날씨 정보를 알려주는 모바일 웹 사이트** <br />
급변하는 날씨 속에 유용한 정보를 알려주고자 시작하게된 프로젝트입니다. <br/>
화면 종류는 메인화면과 즐겨찾기 화면이 있습니다. <br />

- 메인화면: 현재 위치의 온도, 미세먼지, 강수량, 풍속 등의 정보를 제공
- 즐겨찾기 화면: 즐겨찾기에 추가한 위치의 날씨정보를 간략히 제공

## 시작 가이드

**Requirements** <br/>
For building and running the application you need:

- jdk >= 11
- springframework >= 2.7.14
- OpenWeatherMap API KEY
- KAKAO REST API KEY
- AWS App runner

**Requirements** <br/>

```bash
$ git clone https://github.com/SkynI25/weather_app.git
$ cd weather_app
```

## Stacks

### Environment

![Visual Studio Code](https://img.shields.io/badge/Visual%20Studio%20Code-007ACC?style=for-the-badge&logo=Visual%20Studio%20Code&logoColor=white)
![Git](https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=Git&logoColor=white)
![Github](https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=GitHub&logoColor=white)

### Config

![gradle](https://img.shields.io/badge/Gradle-gray)

### Development

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![SpringBoot](https://img.shields.io/badge/SpringBoot-6DB33F?style=flat-square&logo=Spring&logoColor=white)
![OpenWeatherMap](https://img.shields.io/badge/OpenWeatherMap-tomato)
![Kakao Badge](https://img.shields.io/badge/Kakao-FFCD00?logo=kakao&logoColor=000&style=flat-square)

### Communication

![Slack](https://img.shields.io/badge/Slack-4A154B?style=for-the-badge&logo=Slack&logoColor=white)
![Notion](https://img.shields.io/badge/Notion-000000?style=for-the-badge&logo=Notion&logoColor=white)
![GoogleMeet](https://img.shields.io/badge/GoogleMeet-00897B?style=for-the-badge&logo=Google%20Meet&logoColor=white)

## 화면 구성

| 메인페이지                                                                 | 즐겨찾기 페이지                                                            |
| -------------------------------------------------------------------------- | -------------------------------------------------------------------------- |
| ![Main](https://img001.prntscr.com/file/img001/Xvt25IO3T82nl_daN7rlhQ.png) | ![List](https://img001.prntscr.com/file/img001/xw5hLXdfSeW6eMMWBq3pcw.png) |

## 주요 기능

### ⭐️ 해당 위치의 날씨 정보 확인

- 실시간으로 사용자 위치에 해당하는 날씨 정보를 보여줌

### ⭐️ 여러 위치의 날씨 정보 확인

- 실시간으로 사용자가 저장한 위치에 해당하는 날씨 정보를 보여줌
