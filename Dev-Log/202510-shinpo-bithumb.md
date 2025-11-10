## 🔴 Log-BO

### 🟡 기존 테이블 not null
- 상황
  - 기존 제휴멤버십 포인트 카드 저장해두는 테이블에서 포인트 카드번호가 not null이다.
  - 여기서 제휴사 빗썸을 추가해야 하는데 빗썸은 포인트 카드번호가 존재하지 않는다.
  - 따라서 단순히 포인트카드번호 not null 때문에 기존 테이블을 사용하지 못하는 상황이다.
  - not null을 하지 않고, java 코드로 유효성 체크해서 넣었다면 null을 방지하고, 기존 테이블을 쓸 수 있었을 것이다.

- **not null 속성을 넣을 때 확장성을 생각하고 넣자.**
- 결국 해당 테이블 안쓰고 동의 테이블 관리 사용함.

<br>

---
### 🟡 @JsonAnyGetter, @JsonAnySetter
- @JsonAnyGetter : Map 필드를 Json으로 Serialization 해줌.

<br>

---

### 🟡 FilterChain

<br>

---

### 🟡 배치를 java로 돌린다
- 배치를 돌리는 방법이  2가지가 있다. shell로 돌리기, java로 돌리기
1. shell로 돌리기
   - shell script를 써서 돌림. 이런 경우 보통 로직이 프로시저로 되어 있음.


2. java로 돌리기
   - 내가 아는 배치 돌리기. java 코드로 돌림.

<br>

---

### 🟡 카테시안 곱 = cross join

```sql
-- A_TABLE x B_TABLE
SELECT *
FROM A_TABLE, B_TALBE
```
- A_TABLE x B_TABLE
  - 두 테이블의 모든 행을 곱한 결과. 이를 **CROSS JOIN**이라고 한다.
  - A_TABLE의 튜플 개수 : 10, B_TABLE의 튜플 개수 : 5 => 결과는 50개 행 반환.

```sql
-- INNER JOIN
SELECT *
FROM A_TABLE, B_TABLE
WHERE A_TABLE.CODE = B_TABLE.CD;
```
- 두 테이블의 컬럼 비교를 걸어주면 INNER JOIN이 된다.


<br>

---

### 🟡 ssgpnt_coreapi 주요 로직

**RequestValidateInterceptor**

```java
public class RequestValidateInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 요청 로그
        // clientId, apiUrl 검증.
        // 토큰 체크
        return true;
    }
}

```

**ApiServiceLoadingUtil**

```java
import java.util.HashMap;

public class ApiServiceLoadingUtil implements InitializingBean {
    private static Map<String, Object> _map = new HashMap<>();
    
    @Override
    public void afterPropertiesSet() throws Exception {
        // _map 세팅하는 로직
        // clientId, apiUrl, columns를 조회해서 _map에 저장함.
    }
}

```
- 처음 컴파일 실행할 때 afterPropertiesSet이 실행됨.

<br>

---

### 🟡 MERGE INFO ... USING

## 🔴 Log - FO

---




## 🔴 Review

---

## 🔴 Refactoring



