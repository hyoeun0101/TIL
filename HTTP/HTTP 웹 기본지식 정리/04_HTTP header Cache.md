
## 🔴 캐시 관련 헤더

- 캐시가 없다면 똑같은 데이터도 계속 네트워크를 통해 통신해야한다.
  - 네트워크를 통한 통신은 느리고 비싸다.

- 캐시를 적용하면 네트워크를 사용하지 않아 빠르게 데이터를 받을 수 있다.

### 🟡 Cache-control

- `max-age=`
  - 캐시의 유효 시간 설정. 설정 시간동안 브라우저 캐시의 데이터가 유효하다.


```java
/* 응답 헤더 */

HTTP/1.1 200 OK
Content-Type: image/jpeg
cache-control: max-age=60  // 60초 동안 유효
Content-Length: 34012

j123kljoiasudlkjaweioluywlnfdo912u34ljko98udjkla
 slkjdfl;qkawj9;o4ruawsldkal;skdjfa;ow9ejk

```

- `no-cache`
  - 데이터는 캐시해도 되지만, 항상 origin 서버에 검증하고 사용해야 한다.

- `no-store`
  - 데이터에 민감한 정보가 있어서 캐시 저장하면 안된다.
<br>

### 🟡 Pragma
- `Pragma: no-cache`

- HTTP 1.0 하위 호환

### 🟡 Expires

- 캐시 만료일 지정

- HTTP 1.0부터 사용

- 지금은 더 유연한 `Cache-Control:max-age` 권장
  - `Cache-Control:max-age`와 함께 사용하면 `Expires`는 무시된다.


## 🔴 검증 헤더

- 캐시 만료 후 서버에 재요청하면 두 가지 상황이 발생한다.
  
  1. 서버에서 기존 데이터를 변경하지 않은 경우 -> 데이터 재전송 대신에 캐시 재사용 가능

  2. 서버에서 기존 데이터를 변경한 경우

- 검증 헤더는 캐시 데이터와 서버 데이터가 같은지 검증하기 위해 사용한다.
  
<br>


### 🟡 Last-Modified 와 If-Modified-Since(또는 If-Unmodified-Since)

1.  첫 요청에 응답할 때 `Last-Modified`를 포함해서 응답한다.

2. 클라이언트에서는 브라우저 캐시에 캐시 유효 시간과 Last-modified 정보를 저장한다.

```java
/* 1. 첫 번째 요청의 응답 헤더 */

HTTP/1.1 200 OK
Content-Type: image/jpeg
cache-control: max-age=60  // 60초 동안 유효
Last-Modified: 2020년 11월 10일 10:00:00 //데이터 최종 수정일
Content-Length: 34012

j123kljoiasudlkjaweioluywlnfdo912u34ljko98udjkla
 slkjdfl;qkawj9;o4ruawsldkal;skdjfa;ow9ejk
```

3. 캐시가 만료된 후 서버에 재요청할 때 last-modified 정보를 함께 보낸다.

```java

/* 2. 캐시 만료 후 두 번째 요청 헤더 */
GET /star.jpg
if-modified-since: 2020년 11월 10일 10:00:00

```

4. 서버에서 클라이언트가 보낸 `if-modified-since` 정보를 통해 데이터의 변경을 확인한다.

5. 변경이 없으면 Http Body가 없이 304 응답을 보낸다. 
    - Body가 없어서 용량이 아주 작다. ex) 0.1M

6. 클라이언트는 캐시 정보를 재사용한다.


```java
/* 3-1. 두 번째 요청의 응답 헤더 (변경하지 않은 경우) */

HTTP/1.1 304 Not Modified
Content-Type: image/jpeg
cache-control: max-age=60  // 60초 동안 유효
Last-Modified: 2020년 11월 10일 10:00:00 //데이터 최종 수정일
Content-Length: 34012

/* Http Body가 없음 */
```

- 변경한 경우 서버는 데이터를 재전송하고, 클라이언트는 캐시 데이터를 갱신한다.

```java
/* 3-2. 두 번째 요청의 응답 헤더 (변경한 경우) */

HTTP/1.1 200 OK
Content-Type: image/jpeg
cache-control: max-age=60  // 60초 동안 유효
Last-Modified: 2020년 11월 11일 10:00:00 //데이터 최종 수정일
Content-Length: 34012


j123kljoiasudlkjaweioluywlnfdo912u34ljko98udjkla
 slkjdfl;qkawj9;o4ruawsldkal;skdjfa;ow9ejk
```


- **`Last-modified`, `if-modified-since`의 단점**

  - 날짜 기반이라서 1초 미만의 0.X초 단위로 설정이 불가능하다.

  - 실제 데이터의 변경을 확인하는 것이 아니라 날짜의 변경만 확인한다. (날짜만 변경되고 실제 데이터는 같을 수 있음.)

  - 서버에서 별도의 캐시 로직을 관리하지 못한다.

    - ex) 스페이스, 주석처럼 영향이 없는 변경은 캐시를 유지하고 싶다.

<br>

### 🟡 ETag 와 If-Non-Match(또는 If-Match)

1. 첫 번째 요청의 응답 헤더에 ETag를 보내고 캐시 유효시간과 ETag를 브라우저 캐시에 저장한다.

```java
/* 1. 첫 번째 요청의 응답 헤더 */

HTTP/1.1 200 OK
Content-Type: image/jpeg
cache-control: max-age=60  // 60초 동안 유효
ETag: "aaa"
Content-Length: 34012

j123kljoiasudlkjaweioluywlnfdo912u34ljko98udjkla
 slkjdfl;qkawj9;o4ruawsldkal;skdjfa;ow9ejk
```

2. 캐시가 만료된 후 서버에 재요청할 때 ETag 정보를 함께 보낸다.

```java

/* 2. 캐시 만료 후 두 번째 요청 헤더 */
GET /star.jpg
If-Non-Match: "aaa"

```

- 서버에서 클라이언트가 보낸 `If-Non-Match` 정보를 통해 데이터의 변경을 확인한다.
- 변경이 없으면 Http Body가 없이 304 응답을 보낸다. 
  - Body가 없어서 용량이 아주 작다. ex) 0.1M

- 그럼 클라이언트는 캐시 정보를 재사용한다.


```java
/* 3-1. 두 번째 요청의 응답 헤더 (변경하지 않은 경우) */

HTTP/1.1 304 Not Modified
Content-Type: image/jpeg
cache-control: max-age=60  // 60초 동안 유효
ETag: "aaa"
Content-Length: 34012

/* Http Body가 없음 */
```

- 변경한 경우 서버는 데이터를 재전송하고, 클라이언트는 캐시 데이터를 갱신한다.

```java
/* 3-2. 두 번째 요청의 응답 헤더 (변경한 경우) */

HTTP/1.1 200 OK
Content-Type: image/jpeg
cache-control: max-age=60  // 60초 동안 유효
Last-Modified: 2020년 11월 11일 10:00:00 //데이터 최종 수정일
Content-Length: 34012


j123kljoiasudlkjaweioluywlnfdo912u34ljko98udjkla
 slkjdfl;qkawj9;o4ruawsldkal;skdjfa;ow9ejk
```


- `ETag(Entity Tag)`로 캐시 데이터에 고유 버전 이름을 붙인다.

- **즉 ETag 만 서버에 보내서 같으면 유지, 다르면 데이터 재전송**

- 캐시 제어 로직을 서버에서 완전히 관리할 수 있다. 클라이언트는 단순히 ETag를 서버에게 제공하기만 하면 된다.

  - ex) 서버는 배타 오픈 기간인 3일 동안 파일이 변경되어도 ETag를 동일하게 유지.

  - ex) 애플리케이션 배포 주기에 맞추어 ETag 모두 갱신.



