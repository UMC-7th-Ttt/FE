## 📚 프로젝트 개요
꾸준히 독서를 이어가기 어려운 사람들을 위해 책과 장소 큐레이션, 단기 북클럽, 다양한 위젯 기능을 제공하는 앱 서비스입니다.    
가벼운 마음으로 독서를 시작할 수 있도록 도와주며, 나만의 독서라이프를 찾아갈 수 있는 경험을 제공합니다. 

<br/>

## 💡 주요 기능

### 북레터
특정 주제나 상황에 맞춰 에디터가 선정한 책 리스트와 큐레이션 콘텐츠 제공   
(e.g. "겨울에 읽기 좋은 책", "새로운 도전을 위한 책")

### 책 큐레이션
관심사 기반 맞춤형 추천   
사용자 독서 성향 분석 후 개인화된 책 리스트 제공

### 장소 큐레이션
독서하기 좋은 북카페와 독립서점 추천   
위치 기반 및 추천 순으로 장소 리스트 제공

### 단기 북클럽 운영
1~4주 단위로 부담 없이 참여할 수 있는 그룹 독서   
북클럽 멤버들의 인증 사진과 서평을 확인하고, 댓글과 좋아요로 의견 나누기   
완독 권장률을 통한 독서 목표 설정

### 다양한 위젯 기능
블라인드북 위젯: 책 구절을 랜덤으로 띄워 관심 있는 책을 빠르게 발견   
위젯 연동: 북클럽 멤버들의 독서 인증 사진을 실시간으로 확인

<br/>

## 🚀 팀원 소개
|김은진|이승연|전다인|조은향|
|:-:|:-:|:-:|:-:|
|<img width="100px" alt="은진" src="https://avatars.githubusercontent.com/u/80269953?v=4">|<img width="100px" alt="승연" src="https://avatars.githubusercontent.com/u/88431909?v=4">|<img width="100px" alt="다인" src="https://avatars.githubusercontent.com/u/120189161?v=4">|<img width="100px" alt="은향" src="https://avatars.githubusercontent.com/u/146572390?v=4">
|[@kejjin0](https://github.com/kejjin0)|[@yslle](https://github.com/yslle)|[@jeondain](https://github.com/jeondain)|[@JoEunHyang](https://github.com/JoEunHyang)|

<br/>

## 🛠️ 기술 스택 및 개발 환경

<div style="display: flex; flex-wrap: wrap; gap: 10px;">
  <img src="https://img.shields.io/badge/SpringBoot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white" style="border-radius: 8px;">
  <img src="https://img.shields.io/badge/jpa-D22128?style=for-the-badge&logo=jpa&logoColor=white">
  <img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=MySQL&logoColor=white">
</div>

<div style="display: flex; flex-wrap: wrap; gap: 10px;">
  <img src="https://img.shields.io/badge/AWS-232F3E?style=for-the-badge&logo=amazonwebservices&logoColor=white">
  <img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white">
  <img src="https://img.shields.io/badge/Nginx-009639?style=for-the-badge&logo=Nginx&logoColor=white">
</div>

<div>
  <img src="https://img.shields.io/badge/Selenium-43B02A?style=for-the-badge&logo=selenium&logoColor=white">
  <img src="https://img.shields.io/badge/SMTP-6B8E23?style=for-the-badge&logo=gmail&logoColor=white">
  <img src="https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=Swagger&logoColor=white">
  <img src="https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=postman&logoColor=white">
</div>

<br/>

<table style="width: 100%; text-align: left; border-collapse: collapse;">
  <tr>
    <th style="text-align: left;">통합 개발 환경</th>
    <td>IntelliJ</td>
  </tr>
  <tr>
    <th style="text-align: left;">Spring 버전</th>
    <td>3.4.1</td>
  </tr>
  <tr>
    <th style="text-align: left;">데이터베이스</th>
    <td>AWS RDS(MySQL), Redis</td>
  </tr>
  <tr>
    <th style="text-align: left;">배포</th>
    <td>Docker, Github Actions, EC2</td>
  </tr>
  <tr>
    <th style="text-align: left;">Project 빌드 관리 도구</th>
    <td>Gradle</td>
  </tr>
  <tr>
    <th style="text-align: left;">Java version</th>
    <td>java 17</td>
  </tr>
  <tr>
    <th style="text-align: left;">API 테스트</th>
    <td>Swagger, Postman</td>
  </tr>
  <tr>
    <th style="text-align: left;">보안</th>
    <td>OAuth 2.0, JWT, Spring Security</td>
  </tr>
</table>

<br/>

## 📊 ERD
<img src="https://github.com/user-attachments/assets/86a9587d-0ab5-4744-a781-080827162e74"/>
<p/>

<br/>

## 🌐 Open API
- [알라딘 OpenAPI](https://blog.aladin.co.kr/openapi) 
  → 책 정보 제공  
- [문화공공데이터광장](https://www.culture.go.kr/data/main/main.do) 
  → 북카페, 독립서점 정보 제공
- [Kakao Geocoding API](https://developers.kakao.com/) 
  → 현재 위도와 경도를 기반으로 주소 변환
- [구글 로그인 & 이메일 인증](https://developers.google.com/?hl=ko) 
  → 사용자 인증 및 이메일 확인

<br/>

## 🔧 기술 고도화
### 개발 중 진행한 기술 고도화 작업
- CI/CD 파이프라인 최적화   
- N+1 문제 해결   
- SQL 쿼리 최적화   

자세한 내용은 [Notion 링크](https://www.notion.so/196a5f8ffcb080d9a62de1cb66f6997f)에서 확인하실 수 있습니다. 

<br/>


## 🔎 프로젝트 오버뷰 및 기능 상세
<img src="https://github.com/user-attachments/assets/9720bb0d-11c8-4252-ad6e-ee5fe8f73b71">
<img src="https://github.com/user-attachments/assets/be83a39d-0600-4118-bcb6-a43bfd92ea95">
<img src="https://github.com/user-attachments/assets/9b7eeaf7-88cf-4507-9027-149a3d6583c7">
<img src="https://github.com/user-attachments/assets/372118de-2038-44d8-9599-8480bc6605aa">
<img src="https://github.com/user-attachments/assets/65d3b026-6c9d-4773-b8f2-2671d4abf947">
<img src="https://github.com/user-attachments/assets/2651cbee-6cef-4fda-9565-1ce7d962c285">
<img src="https://github.com/user-attachments/assets/a13432ba-b4e4-4adb-b723-2e4dea905249">
<img src="https://github.com/user-attachments/assets/cf895559-3b49-404f-bf80-2975ceddedee">


<br/>
