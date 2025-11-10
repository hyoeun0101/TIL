## 🔴 Log-BO

### 🟡 "Strict stubbing argument mismatch." 에러 해결: Argument Matchers 사용하기

- 나의 상황
  - A코드를 해석하면 pointHistoryTalbe.inert("1L", "1000L", "CHARGE", "20251110000000")가 호출되면 pointHistory를 반환하라는 뜻이다.
  - 그러나 B코드를 보면 System.currentTimeMillis() 때문에 A코드와 매칭이 안되는 것이다. 
  - 즉 Stubbing 한 인자 조합과 실제 호출 인자 조합이 달라서 작동을 안해서 `Strict stubbing argument mismatch.` 해당 오류가 발생하는 것이다.

```java
// 테스트 코드
@Test
@DisplayName("유효한 유저 ID로 포인트를 충전하면 포인트 내역을 insert한다.")
public void givenUserPoint_whenChargePoint_thenReturnIncreasedPoint33() {
    //given
    long userId = 1L;
    long chargePoint = 1000L;
    long now = Instant.parse("2025-11-10T00:00:00Z").toEpochMilli();
    PointHistory pointHistory = new PointHistory(1L, userId, chargePoint, TransactionType.CHARGE, now);
    when(pointHistoryTable.insert(userId, chargePoint, TransactionType.CHARGE, now)) //✅ A코드
            .thenReturn(pointHistory);

    //when
    UserPoint result = pointService.chargeUserPoint(userId, chargePoint);

    //then
    verify(pointHistoryTable, times(1)).insert(userId, chargePoint, TransactionType.CHARGE, now);
}

// PointService 코드

@Service
@RequiredArgsConstructor
public class PointService {
    //...

    public UserPoint chargeUserPoint(long userId, long chargePoint) {
        PointHistory pointHistory 
                = pointHistoryTable.insert(userId, chargePoint, TransactionType.CHARGE, System.currentTimeMillis()); //✅ B코드

        return new UserPoint(userId, chargePoint, System.currentTimeMillis());
    }
}
```

- 해결
  - Argument Matchers 사용하기.
  - Arguent Matchers는 stub을 좀 더 유연하게 작성할 수 있도록 도와주는 클래스이다.
  - 위의 경우에서 pointHistoryTable.insert()의 마지막 인자가 일치하지 않아도 stub이 작동할 수 있게 해준다.
  - 단 모든 파라미터에 matcher를 사용해야 한다.

```java
@Test
@DisplayName("유효한 유저 ID로 포인트를 충전하면 포인트 내역을 insert한다.")
public void givenUserPoint_whenChargePoint_thenReturnIncreasedPoint() {
    //given
    long userId = 1L;
    long chargePoint = 1000L;
    long now = Instant.parse("2025-11-10T00:00:00Z").toEpochMilli();
    PointHistory pointHistory = new PointHistory(1L, userId, chargePoint, TransactionType.CHARGE, now);
    when(pointHistoryTable.insert(
            eq(userId),
            eq(chargePoint),
            eq(TransactionType.CHARGE),
            anyLong() //✅ 아무값이나 들어와도 매칭됨.
    )).thenReturn(pointHistory);

    //when
    UserPoint result = pointService.chargeUserPoint(userId, chargePoint);

    //then
    verify(pointHistoryTable, times(1)).insert(
            eq(userId),
            eq(chargePoint),
            eq(TransactionType.CHARGE),
            anyLong());
}

```
<br>

---