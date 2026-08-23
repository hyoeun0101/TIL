## 🔴 Log-BO
### 🟡 mybatis xml - selectKey 사용법 1

```xml
<isnert id="insertCoupon">
    <selectKey resultType="String" keyProperty="couponPblNo" order="BEFORE">
        select seq_coupon.NEXTVAL FROM dual
    </selectKey>
    INSERT INTO COUPON.COUPON_PBL (컬럼) VALUES (값)
</insert>
```

- insert 실행 전에 자동 생성된 키 값을 가져오거나, 특정 값을 미리 조회할 때 사용된다. 보통 PK를 생성하는 경우에 많이 활용.

- selectKey를 실행 후 결과값을 keyProperty인 `couponPblNo`에 저장한다. resultType은 String이다. 

- `order="BEFORE"`은 INSERT 전 실행. 즉 시퀀스 값을 미리 조회해서 사용
- `order="AFTER"`은 INSERT 후 실행. 즉 DB에서 자동 생성된 PK 값을 조회

<br>

---

### 🟡 mybatis xml - selectKey 사용법 2
- DB에서 자동생성된 PK 값 가져오기

```xml
<insert id="createCert" parameterType="파라미터 타입" useGeneratedKeys="true" keyProperty="certSn" keyColumn="certification_sn"
>
INSERT INTO ... VALUES ...
</insert>
```

```java
interface RwMapper {
    void createCert(Param param); // param의 certSn에 가져온다!
}
```
- 자동생성된 컬럼 certification_sn 값을 파라미터의 certSn에 가져옴. 

<br>

---

### 🟡 NEXTVAL 사용법
- 시퀀스 다음 값을 가져온다.

1. 먼저 시퀀스를 생성해야 한다.

```sql
create sequence my_seq
start with 1
increment by 1
nocache nocycle;
```

2. 사용하기
- insert문
```sql
INSERT INTO users (id, name)
VALUES (my_seq.NEXTVAL, '이름');
```

- select문
```sql
-- postgreSQL

SELECT NEXTVAL('my_seq');
```


<br>

---

### 🟡 정규화?

- 이게 무슨 뜻일까?
```java
params.toString().replaceAll("[\r|\n]", "");

```


<br>

---

### 🟡 JOIN 정리

### left outer join = left join = 합집합

- 왼쪽 기준으로 합집합. 즉 왼쪽은 무조건 모두 select.

- 왼쪽 테이블은 있는데 오른쪽 테이블이 없으면 오른쪽 테이블의 컬럼은 모두 NULL로 표시.


### inner join = join = 교집합

- 왼쪽 기준으로 교집합 . 왼쪽, 오른쪽 둘 다 있어야 나옴.

<br>

---

### 🟡 User-Agent에서 클라이언트 OS 정보 추출.

<br>

---

### 🟡 java.util.regex Matcher 사용법

```java
String target = "My Phone number is 010-1234-5678";

String patternString = "\\d{3}-\\d{4}-\\d{4}"; //전화번호 패턴

Pattern pattern = Pattern.compile(patternString);
Matcher matcher = pattern.matcher(target);

if (matcher.find) {
    System.out.println("Found: " + matcher.group()); //매칭된 전체 문자열 출력
} else {
    System.out.println("No match found.");
}

```
<br>

---

### 🟡 오버라이딩 vs 오버로딩
#### 메서드 오버로딩

### 오버로딩 Overloading

- 동일한 책임을 가진 메서드인데 사용하는 매개변수가 다를 때. 가독성을 높이기 위해 사용한다.

- 오버로딩 조건 3가지
    - 메서드 이름이 같다.
    - 매개 변수의 개수, 데이터 타입이 다르다.
    - 리턴 타입은 상관없다.

```java
// 컴파일 에러!!
// Compile Time Error: method add(int,int) is already defined in class Adder
class Adder {
    static int add(int a, int b) {
        return a+b;
    }
    
    static double add(int a, int b) {
        return a+b;
    }
}
```

```java
class OverloadingCalculation1 {
    /* 메서드 오버로딩 */
    void sum(int a, long b) {
        System.out.println(a+b);
    }

    /* 메서드 오버로딩 */
    void sum(int a, int b, int c) {
        System.out.println(a+b+c);
    }
}
```

<br>

---

### 🟡 WITH AS

- 서브쿼리를 가독성 있게 작성하고 반복 사용할 수 있도록 해줌.
- 서브쿼리를 WITH AS로 빼서 작성.


```sql
WITH 임시이름 AS (
    SELECT ...
    FROM ...
    WHERE ...
)
SELECT ...
FROM 임시이름
```



<br><br>



## 🔴 Review

### 🟡 서브쿼리 -> OUTER JOIN

- 내가 짠 쿼리 : 공통코드를 서브쿼리로 짰다.
    - 서브쿼리는 주쿼리 실행된 후에 실행된다. 때문에 쿠폰 건수가 5개면 총 10번의 쿼리가 실행된다.

```sql
SELECT PBL_NO
     , MBER_NO
     , ( SELECT VAL3 FROM SVCM WHERE CODE_VALUE = '11') AS GIFT_YN
FROM COUPON_PBL
WHERE PBL_NO = '123';
```

- 수정 : JOIN을 걸어주자. 그 중 OUTER JOIN으로 걸어야 한다.
    - WHY? INNER JOIN으로 걸게 되면 공통코드가 삭제되면 이거랑 묶여있는 쿠폰이 안나온다. 그렇게 때문에 해당 공통코드가 삭제되더라도 쿠폰이 나올 수 있게 OUTER JOIN으로 걸어줘야 한다.

```SQL
-- 수정 쿼리

SELECT PBL_NO
     , MBER_NO
     , B.VAL3 AS GIFT_YN
FROM COUPON_PBL A
    LEFT OUTER JOIN SVCM B 
        ON (
                A.CTMMNY_NO = B.CTMMNY_NO
            AND A.OPERT_CODE = B.CODE_VALUE
            )
```


<br>

---

### 🟡 NOW() 실수
- where절에 NOW(), CURRENT_DATE에 대해 유의하자!!!

- Date만 비교해야하는데 NOW()를 사용해서 시간까지 비교해버림.

```sql
-- 수정 전
SELECT *
FROM COUPON_PBL
WHERE NOW() BETWEEN TO_DATE(VALID_BEGIN_DE, 'YYYYMMDD') AND TO_DATE(VALID_END_DE, 'YYYYMMDD');

-- 수정 후
SELECT *
FROM COUPON_PBL
WHERE CURRENT_DATE BETWEEN TO_DATE(VALID_BEGIN_DE, 'YYYYMMDD') AND TO_DATE(VALID_END_DE, 'YYYYMMDD');
```

<br>

---

### 🟡 SQL 로직 VS Java 로직
- DB에서 데이터를 조회할 때 SQL 조작이 나을까? Java 조작이 나을까?

- 컬럼 A 값에 따라 컬럼 B를 C컬럼 또는 D컬럼으로 뽑아야 하는 상황

- 나의 결정 
    - Model에 필드 추가하기 귀찮으니 java에서 분기 처리하자!

- 문제
    - 그럼 건수 100개 나오면 100번 루프 돌아야 함.

- 수정
    - SQL의 CASE WHEN 으로 뽑는게 성능이 더 좋다.
    -  웬만한 조회는 그냥 SQL로 처리하자!
- 결론
    - 간단한 조회, 필터링, 대량 데이터 처리 -> SQL

    - 복잡한 계산, 중간 단계 비즈니스 로직 처리 -> Java

<br>

---

### 🟡 조회할 때 DB index
- index는 테이블의 특정 컬럼을 기준으로 정렬된 별도의 구조를 만들고, 그 구조를 이용해 빠르게 데이터를 찾는다.

- select문에서 where, order by, join 등에 사용된 컬럼에 인덱스를 걸어주면 성능 향상이 된다.

```sql
create INDEX 인덱스명 ON 테이블명(컬럼명명);
```

- **주의할 점**
    - 인덱스 컬럼을 함수로 감싸면 인덱스 사용 안됨.

```sql
-- 인덱스 걸린 gift_de

WHERE TO_DATE(GIFT_DE)

```

- DB index 걸 때 해당 컬럼 타입 변환하면 안됨.
- 문제상황 : index에 GIFT_DE를 걸어놓음. 
```sql

SELECT *
FROM POINT_GIFT
WHERE TO_DATE(GIFT_DE, 'YYYYMMDD') >= NOW();

```




<br><br>

## 🔴 Refactoring

### 🟡 메서드 하나에 로직 여러 개 넣지말기.

- 현재 코드의 문제점
    - updateCouponPbl 메서드를 사용하고 싶음.
    
    - 해당 메서드에 couponPbl을 select한 후 update하는 로직이 들어있음.
    
    - 기존 select 쿼리와 내가 쓰고 싶은 select 쿼리가 다름.
    - 결국 updateCouponPbl 활용하지 못하고 동일한 코드 만듦. select하는 부분만 다른 상황.

```java

private int updateCouponPbl(String code, CouponParam param) {
    // 1. 조회
    Coupon coupon = getCouponPblByNo(code, param);

    //2. valid check

    //3. 갱신
    this.rwMapper.updateCouponPbl(param);
}

```
    
- 그리고 로직 좀 하나에 다 넣자. 여기저기서 valid 체크를 해;;;;; valid 체크하는 건 걍 하나의 함수에 싹 넣어서 처리하자.

<br><br>

---
### 🟡 메서드 파라미터 다형성

- shinsegaenm-ext: SndngServiceImpl - setSndngValue

- 내가 답답한 이유: 기존 코드를 수정하지 못해서 답답하다.

- 현재 상황
    - shinsegaenm-ext: SndngServiceImpl - setSndngValue
    - setSndngValue은 전송할 문자 내용을 세팅한다.

    - 여기서 문자 내용은 templateId에 따라 동적이다.

    - 또 내용을 세팅하기 위한 파라미터(sendByEtcParams)가 존재.

```java

public class SndngServiceImpl implements SndngService {
    /** 전송할 문자 내용 세팅 */ 
    Map<String, String> setSndngValue(String templateId, Member member, SendParams sendByEtcParams) {
        if ("10".equals(templateId)) {
            sendByEtcParams.get


        }
    }
}

```

- 여기서 문제는 `SendParams sendByEtcParams`에서 발생한다. templateId마다 내용 세팅이 다르기 때문.



- snedByEtcParams는 내용을 세팅(특정 Service에서 조회해옴)할 때 필요한 파라미터다.

<br><br>

---
### 🟡 select 조회 메서드 만들 때 고려사항

1. java로 처리할까 vs SQL로 처리할까?
    - 코드의 활용성이냐 vs 성능이냐. 트레이드 오프 발생.
    - 성능 고려해서 SQL로 짜자.

2. by what..
    - 지금 문제점
        - 선물 가능한 쿠폰 조회할 땐 by mberNo
        - 쿠폰 조회할 땐 by couponPbl
    - 서비스 계층은 통합하고, 쿼리는 통합하자!!
    - 즉 서비스는 여러개. 쿼리는 하나.

    - xml 쓸 때 By 뭐로 조회하는지 체크하고 작성하자.

```java

public List<CouponDto> getCouponsByMberId(String mberId) {
    return couponMapper.selectCoupons(
        Map.of("mberId", mberId));
}

public CouponDto getCouponByCouponId(String couponId) {
    List<CouponDto> result = couponMapper.selectCoupons(
        Map.of("couponId", couponId));

    return result.isEmpty() ? null : result.get(0);
}

public Optional<CouponDto> getCouponOfMber(String mberId, String couponId) {
    List<CouponDto> result = couponMapper.selectCoupons(
        Map.of("mberId", mberId, "couponId", couponId));
    
    return result.stream().findFirst();
}

```

<br><br>

---
### 🟡 update후 select하는 로직

```java
//현재 코드
public void main() {
    // 잔액 조회 
    List<Object> list;
    list = this.rwMapper.selectJpoint(params);

    if (list.size() > 0) {
        // 마이너스 처리
        minusProc(list, params);

        // 다시 조회
        list = this.rwMapper.selectJpoint(params);

        // 사용 처리
        useProc(list, params);
    }
}

private void minusProc(List<Object> list, Object params) {

    for(Object o : list) {
        params.setSn(o.getSn());
        this.rwMapper.updateUse(params);        
    }

}
```
- 해당 코드는 조회하고 update하고 다시 조회하는 로직이다.

- 나였으면 update List를 하고 반환값으로 list를 주겠다.

```java
//현재 코드
public void main() {
    // 잔액 조회 
    List<Object> list;
    list = this.rwMapper.selectJpoint(params);

    if (list.size() > 0) {
        // 마이너스 처리
        list = minusProc(list, params);

        // 사용 처리
        useProc(list, params);
    }
}

private List<Object> minusProc(List<Object> list, Object params) {

    for(Object o : list) {
        params.setSn(o.getSn());
        this.rwMapper.updateUse(params);        
    }

    list = this.rwMapper.selectJpoint(params);

    return list;
}
```

<br><br>

---
### 🟡 MSA에서 서비스 호출 원칙
- API Gateway는 외부 요청을 도메인 서비스로 분배한다.

- 도메인 서비스는 서로 직접 호출 or 이벤트 기반 메시징을 통해 통신한다.
    - Kafka/RabbitMQ 등의 메시징 시스템을 사용.

- 도메인 서비스가 서로의 DB에 직접 접근해선 안된다. API를 통해서 조회해야 함.

- **정리**
    - mber 서비스에서 svcm 서비스 조회해야 할 때
    - 값이 거의 안바뀜: mber 서비스 로컬 DB에 복사 + 주기적 동기화(ex:1일마다, 1시간마다)
    - 값이 가끔 바뀜: 이벤트 기반 캐시 갱신 + 주기적 보정 동기화
    - 자주 바뀜: 실시간 API 호출 or Redis로 중앙 집중 캐시

- **Redis를 활용한 캐싱 전략**
    - 상황: mber 서비스에서 svcm 서비스를 자주 조회. 가끔 변경됨.

    1. mber 서비스 -> Redis에서 code 값 조회. 없으면 svcm API 호출

    2. 호출 결과를 Redis에 TTL없이 캐싱

    3. svcm에서 code 값 변경하면 Redis를 직접 갱신 또는 삭제
