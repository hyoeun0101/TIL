## 🔴 HTTP 헤더
 
- HTTP 전송에 필요한 모든 부가 정보를 담고 있다.
- ex) 메시지 바다의 내용, 메시지 바다의 크기, 압축, 인증, 요청 클라이언트, 서버 정보, 캐시 관리 정보 등
- 필요하다면 임의로 헤더를 추가할 수 있다.

### RFC723X
- 2014년 등장.
- 표현(실제 데이터) = 표현 메타데이터 + 표현 데이터

- 메시지 본문(=payload)를 통해 표현 데이터를 전달한다.
- 표현 헤더는 표현 데이터를 해석할 수 있는 정보보를 제공한다.

<br><br><br>


## 🔴 표현 헤더

- message body에 대한 표현 정보를 제공한다.

- 응답, 요청에 둘다 사용.

### 🟡 Content-Type: 표현 데이터의 타입
```
Context-Type: text/html; charset=utf-8

Context-Type: application/json

Context-Type: image/png
```

### 🟡 Content-Encoding: 표현 데이터의 압축 방식

  - 데이터를 보내는 쪽에서 데이터를 압축한 다음 Content-Encoding 헤더 추가
  - 데이터를 읽는 쪽에서 해당 헤더의 참고하여 압축을 해제한다.

```
Content-Encoding: gzip

Content-Encoding: deflate

Content-Encoding: identity

```
### 🟡 Content-Language: 표현 데이터의 자연 언어

```
Content-Language: ko

Content-Language: en

Content-Language: en-US
```

### 🟡 Content-Length: 표현 데이터의 길이

- 바이트 단위

- `Transfer-Encoding`(전송 코딩)을 사용할 땐 Content-Length를 사용하면 안된다.

```
Content-Length: 5
```



<br><br><br>

## 🔴 콘텐츠 협상 (협상 네고시에이션) 헤더
- 요청에만 사용.

- 클라이언트가 선호하는 표현을 나타낸다.

- 우선순위를 부여할 수 있다.
  1. 0~1 사이의 값 사용. 클수록 높은 우선순위. 생략하면 1.
  2. 구체적인 것이 우선된다.

### 🟡 Accept-Language: 클라이언트가 선호하는 자연 언어

```
// Accept-Language 사용하지 않는 요청 헤더
GET /event

// 다중 언어 지원 서버(기본 언어: en)의 응답 헤더
Content-Language: en
```

```
// 요청 헤더
GET /event
Accept-Language: ko

// 다중 언어 지원 서버의 응답 헤더
Content-Language: ko
```
- 만약 Accept-Language가 설정한 언어가 없으면 서버의 기본 언어로 응답한다.

- 자연언어에 우선순위를 부여할 수 있다.

```
// 여기서 ko-KR;q=1 (q 생략)
Accept-Language: ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7
```
- 우선순위 큰 순서
  - ko-KR > ko > en-US > en

### 🟡 Accept:  클라이언트가 선호하는 미디어 타입

- 구체적인 것이 우선한다

```
// 요청 헤더
Accept: text/*,text/plain,text/plain;format=flowed,*/*

```
- 우선순위 큰 순서
  - `text/plain;format=flowed` > `text/plain` > `text/*` > `*/*`

- 예) `Accept: text/*;q=0.3, text/html;q=0.7, text/html;level=1, text/html;level=2;q=0.4, */*;q=0.5`
  - q값을 이용하여 0~1 사이의 우선순위를 부여한다. 클수록 우선순위가 높다.
  - 미디어 타입이 text/plain이면 우선순위는 0.3이다.

### 🟡 Accept-Charset: 클라이언트가 선호하는 문자 인코딩


### 🟡 Accept-Encoding: 클라이언트가 선호하는 압축 인코딩


<br><br><br>


## 🔴 전송 방식에 따른 헤더

### 🟡 단순 전송  
- 데이터 한번에 단순 전송하기
  - 응답헤더에 `Content-Length` 명시해서 데이터 한번에 전송

```
--- 응답헤더----

HTTP/1.1 200 OK
Content-Type: text/html;charset=UTF-8
Content-Length: 3423

<html>
  <body>...</body>
<html>
```

### 🟡 압축 전송
- 데이터를 압축해서 전송하기
  - 응답헤더에 `Content-Encoding` 명시.

```
--- 응답헤더----

HTTP/1.1 200 OK
Content-Type: text/html;charset=UTF-8
Content-Encoding: gzip
Content-Length: 521

lkj123kljoiasudlkjaweioluywlnfdo912u34ljko98udjkl
```


### 🟡 분할 전송

- 데이터를 본할해서 전송
  - `Transfer-Encoding: chunked` 명시. 데이터를 쪼개서 전송한다.

```
--- 응답헤더----

HTTP/1.1 200 OK
Content-Type: text/plain
Transfer-Encoding: chuncked

5
Hello
5
World
0
\r\n
```

- 분할된 데이터의 바이트가 명시되어 있다.

- 데이터를 분할 전송함으로써 빠른 전송이 가능하다.

- 데이터를 분할 전송해서 데이터의 전체 크기를 예상할 수 없다. 따라서 **Content-Length를 같이 사용할 수 없다.**

### 🟡 범위 전송

- 특정 범위의 데이터를 전송한다.

- **요청헤더에서 Range를 명시**하여 응답받을 데이터의 범위를 나타낸다.
```
--- 요청헤더----

GET /event
Range: bytes=1001-2000
```
- 요청 헤더에서 Range로 데이터 범위를 명시했을 때, **응답 헤더에서 Content-Range로** 응답하는 데이터의 범위를 나타낸다.
```
--- 응답헤더----

HTTP/1.1 200 OK
Content-Type: text/plain
Content-Range: bytes 1001-2000 / 2000

qweqwe1l2iu3019u2oehj1987askjh3q98y
```

<br><br><br>


## 🔴 일반 정보 헤더

### 🟡 From: 유저 에이전트의 이메일 정보

- 유저 에이전트의 이메일 정보를 담을 때 사용.

- 요청에서 사용

- 일반적으로 잘 사용 X.

- 검색 엔진 같은 곳에서 주로 사용한다.

### 🟡 Referer: 이전 웹 페이지 주소
- 요청에서 사용

- 현재 요청된 페이지의 이전 웹 페이지 주소

- A -> B로 이동하는 경우, B를 요청할 때 `Referer:A`를 포함.

- 유입 경로 분석하는데 활용.

### 🟡 User-Agent: 유저 에이전트 애플리케이션 정보

- 요청에서 사용

- 클라이언트 애플리케이션 정보

- 어떤 브라우저에서 장애가 발생하는지 파악 가능.


### 🟡 Server: 오리진 서버의 소프트웨어 정보

- 응답에서 사용

- origin 서버의 정보를 담을 때 사용.
  - origin 서버란, 실제로 데이터를 처리하는 서버를 말한다.

- 예) `Server: Apache/2.2.22`, `Server:nginx`

### 🟡 Date: 메시지가 생선된 날짜짜

### Date
- 응답에서 사용

- 메시지가 발생한 날짜와 시간을 나타낼 때 사용

- 예) `Date: Tuem 15 Nov 1994 00:00:00 GMT`

## 🔴 특별한 정보 헤더

### 🟡 Host: 요청한 호스트 정보(도메인)

- 요청 헤더의 필수값. 

- 클라이언트에서 어느 서버에게 요청하는지를 알기 위해 요청할 서버의 도메인을 명시한다.


- 하나의 서버가 여러 도메인을 처리할 때 또는 하나의 IP 주소에 여러 도메인이 적용되어 있을 때를 감안하여 host를 꼭 명시해야한다.

### 🟡 Location: 페이지 리다이렉션

- 3XX 응답 헤더의 Location: Location 위치로 자동 리다이렉트.

- 201(Created) 응답 헤더의 Location: 요청에 의해 생성된 리소스 URI를 의미

### 🟡 Allow: 허용 가능한 HTTP 메서드

- 405(Method Not Allowed)에서 응답에 포함.
- 허용 가능한 HTTP 메서드를 나타낸다.
- 예) `Allow: GET, HEAD, PUT`

### 🟡 Retry-After

- `503(Service Unavailable)`응답 에서 서비스가 언제까지 안되는지 알려준다.

```
// 날짜 표기. 해당 날짜 이후에 동작 가능.
Retry-After: Fri 31 Dec 2000 23:59:59 GMT

// 초단위 표기. 120초 이후에 동작 가능.
Retry-After:120
```

## 🔴 인증 관련 헤더

### 🟡 Authorization

- 클라이언트 인증 정보를 서버에 전달.

- 요청에서 사용.

```
Authorization: Basic XXXXXXXXXX
```

### 🟡 WWW-Authenticate

- `401 (Uauthorized) `응답에서 접근 시 필요한 인증 방법을 정의한다.

```
WWW-Authenticate: Newauth realm="apps", type=1, title="Login to \"apps\"", Basic realm="simple"
```

## 🔴 쿠키 관련 헤더

### 🟡 Set-Cookie
- 응답에 사용

- 서버에서 클라이언트로 쿠키 전달할 때 사용

- `expires=`
  - 만료일이 되면 자동으로 쿠키 삭제.

  - 만료 날짜 생략하면 브라우저 종료시 삭제.

  - 만료 날짜 입력하면 해당 날짜까지 유지.

- `max-age=`
  - 지정 시간 초과하면 쿠키 삭제

  - 0 또는 음수를 지정하면 즉시 쿠키 삭제.

```java
// 해당 날짜까지 쿠키 유지
Set-Cookie: expires=Sat, 26-Dec-2020 00:00:00 GMT

// 3600초 지나면 쿠키 삭제
Set-Cookie: max-age=3600

// 즉시 쿠키 삭제
Set-Cookie: max-age=0

```

- `domain=`
  - 명시된 도메인에는 쿠키 접근 가능.

  - 생략하면 현재 기준 도메인만 쿠키 접근 가능.

```java
// example.org 뿐만 아니라 dev.example.org도 쿠키에 접근 가능.
Set-Cookie: domain=example.org
```

- `path=`
  - 이 경로를 포함한 하위 경로 페이지만 쿠키 접근 가능.
  
  - 일반적으로 path=/ 루트로 지정.

```java
//   /home/level1 접근 가능. /home/level1/level2 접근 가능.
Set-Cookie:path=/home
```

- `Secure`
  - 쿠키는 http, https를 구분하지 않고 전송하지만 Secure를 적용하면 https인 경우에만 전송한다.

- `HttpOnly`
  - XSS 공격 방지를 위함.

  - 자바스크립트에서 쿠키에 접근 불가 (javascript.cookie)
  
  - HTTP 전송에만 쿠키 사용 가능.
- `SameSite=`
  - XSRF 공격 방지를 위함.
  - 요청 도메인과 쿠키에 설정된 도메인이 같은 경우에만 쿠키 전송.




```
Set-Cookie: data=1234; Secure; HttpOnly
```

### 🟡 Cookie
- 요청에서 사용

- 클라이언트가 서버로부터 받은 쿠키를 저장한 후, 서버에게 HTTP 요청할 때 사용.

- 모든 요청에 쿠키 정보가 자동으로 포함된다.

```
Cookie:SESSIONID=1234
```
