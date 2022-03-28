# Axios?
 브라우저, Node.js 를 위한 Pormise API 활용하는 HTTP 비동기 통신 라이브러리.
 
 javascript에는 fetch API가 있긴 하지만 프레임워크에서 ajax를 구현할 때 axios를 사용한다.
 
# Ajax?
서버와 통신하는데 비동기로 XMLHttpRequest 객체를 사용한다.
Json, XML, HTML, 일반 텍스트 등 다양한 포맷을 주고 받는다.

비동기란? 페이지 새로고침없이 서버에게 데이터를 요청. 서버에게 요청하고 응답을 기다리는 동안 실행이 멈춰있는게 아니라 계속 실행하는 것. 
동기란? 서버에게 요청하고 응답이 올 때까지 기다리는 것. 응답 올 때까지 대기해야함.

# Axios 특징
- 써드파티 라이브러리로 설치가 필요
- XSRF 보호를 해준다.
- data 속성을 사용
- 자동으로 JSON 데이터 형식으로 변환한다.
- HTTP 요청을 가로챌 수 있다.요청을 취소할 수 있고 타임아웃 걸 수도 있음.
- 좀 더 많은 브라우저 지원.
=> 간단하게 사용할 때는 fetch 쓰고, 이외의 확장성을 염두하면 axios 사용.

# Axios 사용법
```
# npm 사용하기
npm install axios
# yarn 사용하기
yarn add axios
# bower 사용하기
bower install axios
#jsDeliver CDN 사용하기
<script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
#unpkg CDN 사용하기
<script src="https://unpkg.com/axios/dist/axios.min.js"></script>
```
###문법
```javascript
axios({
 url: '통신할 주소',
 method: 'get',
 data: { foo : 'diary'}
});
```
<https://inpa.tistory.com/entry/AXIOS-%F0%9F%93%9A-%EC%84%A4%EC%B9%98-%EC%82%AC%EC%9A%A9>
