# js 어디서 불러올 것인가?
## 1. head 안
```
<head>
  <script src="main.js"></script>
</head>
```
parsing html -> js 다운, 실행 -> parsing html
- js파일이 크면 다운, 실행하는 시간소요가 길어질 수 있음. -> 멈춰있는 시간 발생

## 2. body 안
```
<body>
  <script src="main.js"></script>
</body>
```
parsing html -> js 다운, 실행
- html 다 읽고 js 실행을 함.   
- js 다운되기 전에 html이 먼저 실행되니까 사용자는 화면을 빨리 볼 수 있음
- 하지만, js에 의존적인 사이트라면 js 다운, 실행 시간 소요가 많음
## 3. head 안 async
```
<head>
  <script async src="main.js"></script>
</head>
```
parsing hmtl    -> parsing html
    js 다운, 실행    
- html이 파싱되는 동안 병렬적으로 js 다운,실행함-> JS 다운로드 시간 단축
- HTML 실행되기 전에 JS가 실행될 수도 있음. JS에서 HTML 요소를 받아야하는 경우가 있다면 문제가 됨.
## 4. head 안 defer
```
<head>
  <script defer src="main.js"></script>
</head>
```
parsing html  -> executing js    
  fetching js
- html 파싱되는 동안 js 다운만 받고 파싱 끝나면 실행
- 제일 효율적
