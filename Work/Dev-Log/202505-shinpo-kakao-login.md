## 🔴 Log-BO
### 🟡 Redis?

- Redis는 메모리 기반의 Key-Value 저장소이다. 즉 메모리에 데이터를 저장하기 때문에 접근이 빠르다. Redis를 캐시로 사용하면 DB에서 데이터를 읽어오는 IO 작업을 줄이고 속도를 높일 수 있다.

- Redis는 TTL(Time-To-Live)를 제공한다. 데이터의 만료 시간을 설정하고 해당 시간이 지나면 자동으로 데이터가 삭제된다.

<br>

---
### 🟡 Redis Pipeline이란?

- Redis 서버에 명령을 하나 보낼 때마다 네트워크 왕복(RTT)이 발생한다.

```java
// Redis 서버와 1000번의 RTT 발생.
for(int i = 0; i < 1000; i++) {
    redisTemplate.opsForValue().set("key" + i, "value" + i);
}
```
- 파이프라인 방식
    - Redis 서버에 명령을 한번에 요청-응답할 수 있게 해준다.
    - 즉 네트워크 왕복 횟수를 줄여 성능 최적화 가능.


```java
// results 값 => 파이프라인에서 실행된 각 명령의 결과를 리스트로 반환한다.

List<Object> results = redisTemplate.executePipelined((RedisCallback<Object>) conn -> {

    for(int i = 0; i < 1000; i++) {
        byte[] key = redisTemplate.getStringSerializer().serialize("key" + i);
        byte[] value = redisTemplate.getStringSerializer().serialize("value" + i);

        conn.set(key, value);
    }
})
```
<br>

---

### 🟡 Redis의 Sorted Set
- Sorted Set (= `ZSet`): 정렬된 집합을 말하며 score에 따라 정렬된다.


```java

List<Object> results = redisTemplate.executePipelined((RedisCallback<Object>) conn -> {

    // 1. "myzset"이란 ZSET에 값 "member1"을 score=10.5로 추가
    conn.zAdd("myzset".getBytes(), 10.5, "member1".getBytes());

    // 2. "myzset"이란 ZSET에서 score 범위가 0.0 ~ 50.0인 요소 삭제
    conn.zRemRangeByScore("myzset".getBytes, 0.0, 50.0);

    // 3. ZSET에서 순위 0~2번째 요소 삭제. 즉 상위 3개 요소 삭제
    conn.zRemRange("myzset".getBytes, 0, 2);

    // 해당 키에 만료시간(초) 설정
    //4. myzset 키는 1시간 뒤 자동 삭제됨.
    conn.expire("myzset".getBytes, 3600);

    // ZSET에서 score가 특정 범위에 있는 요소의 수를 반환
    // 5. score가 10.0 이상 100.0 이하인 요소 개수 반환
    conn.zCount("myzset".getBytes, 10.0, 100.0);
})
```

- results 변수에는 각 명령의 결과값이 리스트로 담겨있다.

<br>

---

### 🟡 ParameterizedTypeReference<T> ??
- RestTemplate 같은 클래스에서 제네릭 타입을 응답받을 때 유용하게 쓰인다.

- Java에서는 타입 소거(Type Erasure)때문에 런타임에 제네릭 타입을 알 수 없다.

```java
RestTemplate restTemplate = new RestTemplate();

// 런타임 때 List<String> ---> List 가 된다.
List<String> response = restemplate.getForObject(url, List.class);
```
- 제네릭 정보가 사라지면 타입이 안전하지 않다.
- `ParameterizedTypeReference<T>`를 사용하면 런타임에 제네릭 타입을 유지할 수있다.


```java
RestTemplate restTemplate = new RestTemplate();

ResponseEntity<List<String>> response = restTemplate.exchange(
    url,
    HttpMethod.GET,
    null,
    new ParameterizedTypeReference<List<String>>() {}
);

List<String> list = response.getBody();
```

<br>

---



### 🟡 HTTP 303 See Other + cookie + header

__상황__
1. 카카오 로그인 페이지 불러오기.
    - 화면에서 window.location으로 api에 get 요청.
```javascript
window.location.replace('/api/group/login-sns/kakao');
```

2. 이 api에서 HTTP 303 + location + Set-Cookie 반환
    - location의 값은 카카오 로그인 url.

3. 브라우저에서 location의 url로 화면 전환. 이 때 location의 url을 GET 요청으로 Header를 새로 만들어 요청하는 것이다.
    - 따라서 이 때 Header를 커스텀할 순 없다.
    - 대신 303 응답에 Set-Cookie로 쿠키를 실어 보내면 브라우저에 쿠키가 남아있어 이 쿠키를 활용하면 된다.

4. 카카오에서 콜백.
    - 여기서 쿠키의 정보 활용 가능!


<br>

---

### 🟡  join on VS where

- join할 때 on에 조건을 걸어주는게 나을까? where에 거는게 나을까?
- 일단 on과 where의 차이점에 대해 알아보자.

    - on 조건 : 두 테이블을 어떤 기준으로 조인할지 정의.
    - where 조건 : 조인된 결과에서 필터링. 

```sql
-- ON 조건 걸기
SELECT *
  FROM EXT.AGREE A
  LEFT OUTER JOIN MBER.MBER_AGREE B
    ON A.GROUP_CD = B.GROUP_CD
   AND B.MBER_NO = '12345'
 WHERE A.CHECK_YN = 'Y'
```
```sql
-- WHERE 조건 걸기
SELECT *
  FROM EXT.AGREE A
  LEFT OUTER JOIN MBER.MBER_AGREE B
    ON A.GROUP_CD = B.GROUP_CD
 WHERE A.CHECK_YN = 'Y'
   AND B.MBER_NO = '12345'

```

- `LEFT JOIN` 에서 제일 큰 차이가 있다.
    - ON에 걸면 조건이 안맞는 건 NULL로 들어간다.
    - 하지만 WHERE에 걸면 필터링됨.

-> ROW가 어떻게 나오느냐에 따라 결정. 성능은 쿼리 결과에 따라 달라짐.



## 🔴 Log - FO

### 🟡 beforeEnter, beforeRouteEnter

- router > index.js의 beforeEach 실행.
- 페이지 그려짐
    - src > main.js created 실행.
    - MobileLayout.vue의 beforeRouteEnter() 실행
    - src > main.js mounted 실행.

- main.js created에서 /autoLogin 비동기 동작안하고 바로 MobileLayout.vue의 beforeRouteEnter() 실행.


- 문제
    - 회원 상태에 따라 온라인 전용 화면 vs 오프라인 전용 화면이 그려져야함.
    - 그런데 지금 화면이 그려지고 회원 상태를 체크해서 화면을 수정함.
    - 오프라인 전용 화면에서 튕기는 이유
    - 화면을 다 그려 => 상태 변화.

- `beforeEnter`는 
- 오프라인 로그인 전용 `isLoginOfflieUser` 상태를 하나 만들었다.

- 그런데 그 다음 api 전송하는데 토큰 오류가 발생한다.

<br>

---

### 🟡 router에서 next() 동작 방식
- 문제 상황
    - `beforeRouteUpdate`에서 next() 호출 후 그 다음 코드가 바로 실행되었다.
```javascript
beforeRouteUpdate(to, from, next) {

    if (store.state.isLoginOfflineMember) {
        next(); //여기서 바로 넘어가길 바람.
    }
    // 하지만 문제 발생! 아래 실행 코드가 실행됨.
    document.getElementById('target').style.display = 'none';

    next();
}
```

- next()는 "라우터 전환을 계속 진행하라"라는 신호를 주는 것 뿐, 그 자체가 비동기 함수처럼 동작하진 않는다.


```javascript
//안전한 패턴
beforeRouteUpdate(to, from, next) {
    next();

    this.$nextTick(() => {
        const el = document.getElementById('target');
        if (el) el.style.display = 'none';
    })

}
```


<br>

---
### 🟡 $route.params created 세팅 오류!!

- created에서 $route.params 세팅하는데 값이 세팅이 안됨.
```javascript
created() {
    this.phLoginParams = {...this.$route.params}
}

```
- 이유는? 


<br>

---
### 🟡 input focus 안됨

<br>

---
### 🟡 window.open
- 카카오 페이지 팝업창 열 때 window.open을 사용했다.

- window.open은 브라우저 내비게이션을 트리거한다. 내비게이션은 항상 GET 요청으로 시작.

- Header 조작은 불가하다. 


<br>

---
### 🟡 Vuex
- Vuex는 state에 직렬화 가능한 값만 저장하는 게 원칙이다.

- 함수는 직렬화가 안되므로, 페이지 리로드/전환 중에 state를 보존하려고 할 때 변할 수 있다.


<br><br><br><br><br>

## 🔴 Review
### 🟡 snsToken과 관련해서...

- snsToken은 개인키로 암복호화를 한다. 

### 🟡 ext - loginRestrict 로직 수정
1. handleLogin2Factor, handleAllRestrict, handleAccessRestrict 각 함수들을 LoginRestrcitService에 넣는다.

2. 파라미터는 Group, Mber, Cooper 공통 LoginRestrict 파라미터를 생성한다.

3. GroupLogin, MberLogin 각각 LoginRestrict 상속한다.

4. LoginRestrictService의 파라미터에 LoginRestrict 파라미터로 선언.

### 🟡 ext - login 로직 수정

- validation은 api쪽에 두어야 함.

- 제일 큰 문제가 모든 로직이 ext에 있다는거..; 최악이에요

### 🟡index
- 처음 개발할 때 index 잡으면 오래 안걸리지만 운영에 데이터 많을 때 index 잡으면 오래걸린다.

- 그래서 처음 개발할 때 index 잘잡는게 중요함.

## 🔴 Refactoring
