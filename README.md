# NC 코딩 테스트

## 폴더 설명
- 01.front
  - dom 관련 jquery등 라이브러리 없이 구현한 버전
- 02.front+server
  - vuejs를 이용하여 SPA로 개발한 버전
  - 프로젝트 설명
    - nc-test-front
      - vue를 사용하여 구현한 프론트사이드 프로젝트
    - nc-test-server
      - springboot2를 사용하여 구현한 서버 사이드 프로젝트

## 사용라이브러리
- 01.front
  - javascript
    - d3js
  - css
    - bootstrap4
- 02.front+server
  - javascript
    - vue
    - vuex
    - bootstrapvue
    - d3js
  - css
    - bootstrav4

## 실행방법
- 01.front
  1. index.html 파일을 웹브라우저로 OPEN   
- 02.front-server
  - server
    ```shell
    cd nc-test-server
    mvn spring-boot:run #8080 port로 서버 실행
    ```
  - front
    ```shell
    cd nc-test-front
    # 1. npm 사용시
    npm install
    npm run serve

    # 2. yarn 사용시
    yarn install
    yarn run serve #8001 port로 webpack-dev 서버 실행
    ```
  - webbrowser
    - http://localhost:8001   
