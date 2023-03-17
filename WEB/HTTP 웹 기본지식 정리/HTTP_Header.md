## 🍎 HTTP 헤더

- HTTP 전송에 필요한 모든 부가 정보를 담고 있다.
- ex) 메시지 바다의 내용, 메시지 바다의 크기, 압축, 인증, 요청 클라이언트, 서버 정보, 캐시 관리 정보 등

## 🍎 표현 헤더

### Content-Type

- 예) `text/html; charset=utf-8` , `application/json`, `image/png`

### Content-Encoding

- 표현 데이터를 압축하기 위해 사용한다.
- 데이터를 전달하는 곳에서 압축 후 인코딩 헤더 추가
- 데이터를 읽는 곳에서 인코딩 헤더의 정보로 압축 해제
- 예) gzip, deflate, identity

### Content-Language

- 표현 데이터의 자연 언어를 나타낸다.
- 예) ko, en, en-US

### Content-Length

- 표현 데이터의 길이를 나타낸다.
- 바이트 단위
- `Transfer-Encoding`을 사용하면 Content-Length를 사용하면 안된다.

## 🍎 콘텐츠 협상 (협상 네고시에이션) 헤더

- 협상 헤더는 요청 시에만 사용한다.
- 클라이언트가 선호하는 것을 나타낸다.

### Accept

- 예) `Accept: text/*, text/plain, text/plain;format=flowed, */*`
  - 구체적인 것이 우선한다. 서버에서 Content-Type을 제일 구체적인 text/plain;format=flowed로 설정한다.
- 예) `Accept: text/*;q=0.3, text/html;q=0.7, text/html;level=1, text/html;level=2;q=0.4, */*;q=0.5`
  - q값을 이용하여 0~1 사이의 우선순위를 부여한다. 클수록 우선순위가 높다.
  - 미디어 타입이 text/plain이면 우선순위는 0.3이다.

### Accept-Language

- 클라이언트가 선호하는 자연 언어를 나타낸다.
- 예) `Accept-Language : ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7`
- 클라이언트에서 위와 같이 요청을 하면 서버에서 Content-Language의 값을 ko-KR로 하고, 없으면 ko, en-US, en 순서로 넣는다.

### 이외

- Accept-Charset
- Accept-Encoding

## 🍎 전송 방식에 따른 헤더

### 단순 전송 - Content-Length

- 응답헤더에 Content-Length 명시해서 데이터 한번에 쭉 받기

### 압축 전송 - Content-Encoding

- 응답헤더에 Content-Encoding 명시.
- 데이터를 압축해서 전송

### 분할 전송 - Transfer-Encoding

- `Transfer-Encoding: chunked` 명시. 데이터를 쪼개서 전송한다.
- 분할해서 보낼 데이터의 바이트가 명시된다.
- 데이터를 쪼개서 전송하니 빠르게 전송할 수 있다.
- 단 Content-Length를 같이 사용할 수 없다. 데이터를 쪼개서 전송하기 때문에 데이터의 전체 크기를 예상할 수 없다.

### 범위 전송 - Range, Content-Range

- 예)` Range:bytes=1001-2000`
  - 요청헤더에서 Range를 명시하여 응답받을 데이터의 범위를 나타낸다.
- 예) `Content-Range:bytes 1001-2000/2000`
  - 요청 헤더에서 Range로 데이터 범위를 명시했을 때, 응답 헤더에서 Content-Range로 응답하는 데이터의 범위를 나타낸다.

## 🍎 일반 정보 헤더

### From

- 요청 헤더에서 유저 에이전트의 이메일 정보를 담을 때 사용
- 일반적으로 잘 사용되진 않는다.
- 검색 엔진 같은 곳에서 주로 사용한다.

### Referer

- 요청 헤더에서 이전 웹페이지 주소를 담을 때 사용
- 유입 경로 분석하는데 활용.
- A -> B로 이동하는 경우, B를 요청할 때 `Referer:A`를 포함한다.

### User-Agent

- 요청 헤더에서 클라이언트 애플리케이션 정보 담을 때 사용
- 어떤 브라우저에서 장애가 발생하는지 파악할 수 있다.

### Server

- 응답 헤더에서 ORIGIN 서버의 정보를 담을 때 사용.
- ORIGIN 서버란 실제로 데이터를 처리하는 서버를 말한다.
- 예) `Server: Apache/2.2.22`, `Server:nginx`

### Date

- 응답 헤더에서 메시지가 발생한 날짜와 시간을 나타낼 때 사용
- 예) `Date: Tuem 15 Nov 1994 00:00:00 GMT`

## 🍎특별한 정보 헤더

### Host

- 요청 헤더의 필수값. 요청할 서버의 도메인을 명시해야한다.
- 하나의 서버가 여러 도메인을 처리할 때, 하나의 IP 주소에 여러 도메인이 적용되어 있을 때를 감안하여 host를 꼭 명시해야한다.

### Location

- 3XX 응답 결과에 Location 헤더가 있으면 Location 위치로 리다이렉트한다.
- 201(Created) 응답에서 Location은 요청에 의해 생성된 리소스 URI를 의미한다.

### Allow

- 405(Method Not Allowed)응답에서 응답에 포함해야한다. 허용 가능한 HTTP 메서드를 나타낸다.
- 예) Allow: GET, HEAD, PUT

### Retry-After

- 503(Service Unavailable)응답 에서 서비스가 언제까지 안되는지 알려준다.

```
Retry-After: Fri 31 Dec 2000 23:59:59 GMT (날짜 표기)

Retry-After:120 (초단위 표기)
```

## 🍎 인증 관련 헤더

### Authorization

- 클라이언트 인증 정보를 서버에 전달한다.

### WWW-Authenticate

- 401 (Uauthorized) 응답에서 접근 시 필요한 인증 방법을 정의한다.

```
WWW-Authenticate: Newauth realm="apps", type=1, title="Login to \"apps\"", Basic realm="simple"
```

## 🍎 쿠키 관련 헤더

### Set-Cookie

- 서버에서 클라이언트로 쿠키를 생성하여 전달할 때 응답에서 사용한다.

```
** 셍명주기 **
Set-Cookie:expires=Sat, 26-Dec-2020 00:00:00 GMT
(만료일이 되면 쿠키 삭제)

Set-Cookie:max-age=3600
(3600초, 0이나 음수를 지정하면 쿠키 삭제)

(만료 날짜 생략하면 브라우저 종료 시 삭제)
```

```
** 도메인

Set-Cookie:domain=example.org
```

- 도메인을 지정해서 쿠키를 생성한다. `example.org` 뿐만 아니라 `dev.example.org`도 쿠키 접근이 가능하다.
- 도메인을 생략하면 example.org, 쿠키를 생성한 도메인에서만 쿠키에 접근 가능하다.
- 요청할 도메인과 일치하는 쿠키를 담아 요청한다.

```
** 경로
Set-Cookie:path=/home
```

- 이 경로를 포함한 하위 경로 페이지만 쿠키 접근이 가능하다.

### Cookie

- 클라이언트가 서버에서 받은 쿠키를 저장한 뒤, 클라이언트가 서버에 쿠키 전달할 때 사용한다.

```
Cookie:SESSIONID=1234
```

- 쿠키는 http, https를 구분하지 않고 전송하지만 Secure를 적용하면 https인 경우에만 전송한다.
- XSS 공격, XSRF 공격을 방지한다.

## 🍎 캐시 관련 헤더

- 캐시가 없다면 똑같은 데이터도 계속 네트워크를 통해 통신해야한다.
  - 네트워크를 통한 통신은 느리고 비싸다.
- 캐시를 적용하면 네트워크를 사용하지 않아 빠르게 데이터를 받을 수 있다.

### Cache-control

```
cache-control:max-age=60
(60초 동안 캐시에 담아둠)

cache-control:no-cache
(데이터는 캐시 가능하지만, 계속 Origin 서버에 검증하고 사용)

cache-Control:no-store
(캐시에 저장하면 안됨)
```

\*\* 캐시 동작 과정

1. 응답헤더에 `cache-control:max-age=60`이 있으면 60초동안 데이터를 캐시에 담아둔다.
2. 유효 시간 내에 같은 요청이 오면 캐시에서 데이터를 꺼내 사용한다.
3. 유효 시간이 지났다면 다시 서버로부터 데이터를 받고, 캐시를 갱신한다.

### Expires

- 캐시 만료일을 지정한다.
- 이것보단 Cache-Control:max-age가 권장된다. Cache-Control와 함께 사용하면 Expires는 무시된다.

### 서버의 데이터가 변경되었는지 확인

- 서버에서 기존 데이터를 변경하지 않았으면 서버로부터 데이터를 갱신할 필요가 없다.
- 서버의 데이터가 변경되지 않았으면 캐시에서 데이터를 받으면 된다.
- 데이터가 변경되었는지 확인하는 방법은 두 가지가 있다. ( `ETage`, `Last-Modified` )

### ETag, If-Non-Match

- 캐싱 데이터에 임의의 고유한 이름 ETag를 붙인다. 데이터가 변경되면 ETag를 변경하여 데이터가 변경되었는지 확인할 수 있다.

**\*\* 동작 과정**

1. 응답헤더에 `Cache-Control:max-age=60   ETag:"aaaaaa"`를 붙여 응답한다.
2. 캐시에 데이터를 저장한다.
3. 캐시의 유효 시간이 지나서 클라이언트가 서버한테 다시 요청할 때, 요청 헤더에 `If-Non-Match:"aaaaaa"`를 붙인다.
4. 서버에서는 ETag가 같으면, Body가 없는 `304 Not Modified`를 보내고 캐시의 유효시간을 갱신한다. 그럼 클라이언트는 다시 캐시에서 데이터를 받는다.
   - ETag가 같다는 것은 데이터의 변경이 없다는 의미다. Body가 없이 작은 용량의 네트워크 통신만 일어난다.
5. 서버에서 ETage가 다르면, 서버는 `200 OK`와 갱신된 데이터를 응답하고, 캐시의 데이터 역시 갱신한다.

- ETag는 같으면 캐시 유지, 다르면 갱신하며 단순하게 동작한다.
- ETag를 사용하면 캐시 제어 로직을 서버에서 완전히 관리할 수 있다.
- 서버는 배타 오픈 기간 3일동안 파일이 변경되어도 ETage를 동일하게 유지하고, 배포 주기에 맞춰 ETag를 모두 갱신한다.

### Last-Modified , if-modified-since

- Last-Modified는 데이터가 수정된 날짜와 시간을 나타낸다.

**\*\* 동작 과정**

1. 응답헤더에 `Cache-control:max-age=60   Last-Modified: Thu, 04 Jun 2020 07:19:24 GMT`를 붙혀 응답한다.
2. 캐시에 데이터를 저장한다.
3. 캐시의 유효 시간이 지나서 클라이언트가 서버에게 다시 요청할 때, 요청 헤더에 `if-modified-sicne:Thu, 04 Jun 2020 07:19:24 GMT`를 붙인다.
4. 서버에서 데이터의 변경 시간이 같으면, Body가 없는 `304 Not Modified`를 보내고 캐시의 유효시간을 갱신한다. 그럼 클라이언트는 다시 캐시에서 데이터르 받는다.
5. 서버에서 데이터의 변경 시간이 다르면, `200 OK`와 갱신된 데이터를 응답하고, 캐시의 데이터 역시 갱신한다.

- Last-Modified는 데이터가 의미없이 변경되어도 갱신된다. (주석이 변경돼도 갱신)서버에서 데이터의 갱신을 마음대로 조절할 수가 없다.

## 🍎 프록시 캐시 서버
