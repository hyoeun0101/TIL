
## 🔴 2XX
### 🟡 200 OK
- GET 요청인 경우 : 조회한 리소스 반환
- HEAD 요청인 경우 : 메세지 본문 없이 표현 헤더가 응답에 포함되어 있음을 의미.
- POST 요청인 경우 : 성공적으로 전송됨.


### 🟡 201 Created

- create 성공
- 응답 메세지 본문에 새로 만들어진 리소스, 리소스에 대한 설명과 링크를 반환.

### 🟡 204

- delete 성공
- response body가 없음.



## 🔴 3XX


### 🟡 302 Found

- 임시 리다이렉트. 클라이언트에게 다른 URI로 이동하라고 지시하지만, 원본 URI는 유효함.

1. 서버가 HTTP 302와 Loation 헤더를 반환

```
GET /old-path HTTP/1.1
Host: example.com

HTTP/1.1 302 Found
Location: https://example.com/new-path
Content-Length: 0
```
2. 브라우저(클라이언트)는 자동으로 새 URL에 재요청



### 🟡 303 See Other

- 서버가 클라이언트에게 "다른 URI로 가서 결과를 보라"고 지시하는 리다이렉트 응답 코드.

- 주로 POST 요청의 리다이렉트 상황에서 사용. 

- 예시
    1. SNS 로그인 페이지  POST 요청 -> 응답이 `HTTP 303 + Location`
    2. 브라우저에서 GET 요청으로 카카오 로그인 페이지 전환



## 🔴 4XX
### 400 Bad Request

- 잘못된 요청 구문. 유효하지 않은 요청 메시지

### 404 Not found
- 요청받은 리소스를 찾을 수 없음.
- get하는데 존재하지 않을 때 발생
### 413 Payload Too Large
- 요청의 크기가 서버에서 허용하는 최대 크기를 초과했음.
### 405 Method Not Allowed

- 서버까지 도달했으나 사용 불가능..?
### 415 Unsupported Media Type
- 지원하지 않느 형식
- 요청의 Content-Type, Content-Encoding으로 인해 발생하거나 데이터를 직접 검사한 경우 발생.
## 🔴 5XX
### 500 Internal Server Error

- 요청을 처리하는 과정에서 서버에 예상치 못한 에러가 발생.
