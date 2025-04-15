### 🍎 COUNT

- COUNT(\*) == COUNT(숫자)
- COUNT(컬럼명) : 결과값 중 NULL 값은 COUNT 하지 않는다.
- COUNT(NULL) : 결과값 0

### 🍎 OVER

---

## 🔴 PostgreSQL

### 🟡 DATE_TRUNC() 사용법

```sql
-- 오늘: 2025-03-21


SELECT DATE_TRUNC('month', CURRENT_DATE);
-- 결과 : 2025-03-01 00:00:00.000000 +00:00


SELECT DATE_TRUNC('year', CURRENT_DATE);
-- 결과 : 2025-01-01 00:00:00.000000 +00:00

```

### 🟡 ARRAY_AGG() 사용법
```sql
-- 쿠폰 테이블에 있는 모든 coupon_no 값을 배열로 묶어 출력.
SELECT ARRAY_AGG(COUPON_NO) FROM COUPON;

-- 결과
-- {1,2,3,4,5}
```

### 🟡 FOR UPDATE 사용법

```sql
SELECT coupon_id
FROM coupon
WHERE user_id = '1234'
FOR UPDATE;
-- 이 쿼리를 실행하는 트랜잭션이 끝날 때까지 해당 행을 잠금(lock)
```
- 다른 트랜잭션에서 같은 coupon_id를 수정, 삭제하는 것을 방지한다.

- 즉 동시성 문제 방지하기 위해 사용.

