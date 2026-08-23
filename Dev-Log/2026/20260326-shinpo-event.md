## # Claude Code 사용하기
```text
현재 컴파일오류는 무시해줘.                                          
EventV2FrontServiceImpl.java와  RoEventV2FrontMapper.xml 이 관련있고, EventV2ServiceImpl.java와 RoEventV2Mapper.xml이 관련있어.                                                                                                   
현재 어떤 테이블이 있는지 정리해주고, 각 테이블의 기능도 한눈에 볼 수 있게 정리해줘.                                                                                                                                              
어떤 기능을 제공하고 있는지 정리해줘.

해당 내용을 README.md 파일로 만들어줘.
```

```text
EVENTV2_REWARD_COND 테이블에 대해 더 자세히 정리해줘.       
해당 내용을 EVENT_REWARD_TABLE.md 파일로 정리해줘. 
```

```text
EVENTV2 테이블도 더 자세히 설명하여 EVENTV2_TABLE.md 파일을 만들어줘. 
```

```text
1. 지금까지 있던 요구사항을 크게 2가지로 나눠서 정리해줘.                                                                                                                                                                                                                                                         
   - 하나는  이벤트(미션+추첨형 위주),다른 하나는 빙고 이벤트야.                          
   - 큰로직만 한눈에 볼 수 있게 간단히 정리해줘.                                                                                                                                                                                                                                                                    
   -  DB의 핵심 컬럼만 정리해.                                                                                                                                                                                                                                                                                    
  2. 이벤트는 EVENT.md 파일로 정리해주고, 빙고 이벤트는 BINGO.md 파일로 정리해줘. 기존 파일은 수정하지마. 내가 직접 삭제/수정할거야.                                                                                                                                                                                
  3. 크게 관리자에서 동작하는 로직, 배치로 동작하는 로직, 앱 화면에서 동작하는 로직 이렇게 정리해. 이벤트, 빙고이벤트 각각 정리해.
  EVENT.md로 이벤트 관련 로직의 큰 그림을 볼 수 있어야 해. BINGO.md도 마찬가지야.                                                                                                                                                                                                                                   

```
## # FO

| 태그                   | 역할           |
|----------------------|--------------|
| `<table>`             | 테이블 전체 컨테이너  |
| `<colgroup></colgroup>` | 열 너비 사전 정의   |
| `<thead>`              | 헤더 영역        |
| `<tbody>`              | 데이터 영역       |
| `<tr>`                 | 행(row) 하나    |
| `<td>`                 | 데이터 셀        |
| `colspan = "N"`        | N개 열을 하나로 병합 |
| `rowspan = "N"`         | N개 행을 하나로 병합 |

### ## javascript some
- 하나라도 조건에 부합하면 true

```javascript
const arr = [1,2,3,4,5];

// 하나라도 3보다 크면 true
arr.some(x => x > 3); //true
// 하나라도 10보다 크면 true
arr.some(x => x > 10) // false

```

### ## Cluade Code 사용하기
```text
1. 조회를 클릭하면 brandList가 셋팅되고, grdMst가 셋팅된다.                                                                                                                                                                       
2. 단 grdMst에 뿌려주는 것은 selectedMrhstNoList에 있는 값은 filter가 되어야 한다. (brandList도 필터를 할지는 알아서 판단.)
3. grdMst에 뿌려준 것 중 addToGroup으로 추가할 경우, grdMst에서 해당 row가 보이지 말아야 한다.(brandList도 같이 필터할지는 알아서 판단. )      
4. deleteGroupMrhst를 할 때도 마찬가지로 grdMst에 다시 추가된다.

------------------------------------------------

remainingMrhstNos를 필터할 필요없이 , selectedMrshtNoList에서 현재 삭제하려는 mrhstNo만 삭제하면 되나?   
```
- 단순한 로직을 좀 더 복잡한 코드로 생성한다.
- 처음 짜준 코드가 직관적으로 읽히지 않아서 직접 어떻게 짜달라고 설명을 했다.
  - filter 두 번으로 사용 가능한걸, flatmap, map, filter를 모두 사용하는 로직을 짜줌.

### 화면 요구사항 정리
```text
## 대상 선택 팝업
- 닫기 버튼 클릭: 변경사항이 없을 경우 팝업 닫기. (closeModal) 변경사항이 있을 경우 '설정한 내역이 적용되지 않습니다' 얼럿 노출
- 조회 버튼 클릭: 유형, 명 또는 코드 조건을 기준으로 조회. 유형을 선택하지 않았을 경우 조회 버튼 비활성화
- 유형 select : '선택'이 default.
    - 공통일 경우,목록 그리드 딤 처리, 조회 버튼, 그룹 추가버튼, 입력 필드 비활성화
    
- 추가 버튼 
    - 유형을 선택하지 않았을 경우 추가 버튼 비활성화. 목록에서 대상을 선택하지 않고 추가 버튼을 클릭하면 '브랜드/제휴사를 선택해주세요.' 얼럿 노출.
- 그룹 추가 버튼 : 유형을 선택하지 않았을 경우 해당 버튼 비활성화. 
- 목록에서 selectedMrhstNoList에 포함되면 목록에서 삭제가 아니라 비활성화 처리.
```

<br>

---

### ## svcm 그리드 사용법
```javascript
// 리스트 데이터로 set
this.grd.invoke('resetData', list);

this.grd.invoke('getRowCount');
// 체크한 row list
this.grd.invoke('getCheckedRows')

// 모두 체크 취소
this.grd.invoke('uncheckAll');

this.grd.invoke('refreshLayout');

// 그리드 컬럼 재설정
this.grd.invoke('setColumns', this.grdProps.columns);

this.grd.invoke('setBodyHeight', this.grdProps.bodyHeight);

```

### ## Lodash
- Lodash : 자주 쓰는 유틸 함수 모음집(라이브러리)
- Mixin : 여러 컴포넌트에서 공통으로 쓸 코드를 한 파일에 모아두는 Vue의 기능
- 
## # BO

### ## join? 각각 select? N+1 문제 trade-off

- 상황 : reward cond 1개 - brand group N개 - brand M개
- 이 상황에서 그룹과 브랜드를 조회할 때 조인으로 조회할까?
  - 그러면 그룹이 중복인 상태로 M개 나옴.

- N+1문제?
  - 데이터 1건을 조회한 후, 연관 데이터를 N번 추가 조회하는 패턴.
  - 목록 조회에서 문제가 됨.
  - 단건 조회는 1개 조회 + 10개 => 11번 select.
  - 목록 조회는 50개 * 11 => 550q번 select가 되버림.
  
- 결론 : 각각 조회해서 스트림으로 set하기.


### ## PostgreSQL 인덱스

- PostgreSQL은 PK를 만들면 자동으로 unique B-tree index를 만들고 NOT NULL을 강제한다.
- 
- PK에는 자동 인덱스가 붙는다.
- 따라서 PK 조회는 빠르다.

- 기본 규칙은 왼쪽부터 맞아야 한다.
- PostgreSQL 멀티 컬럼 B-tree 인덱스 (A,B,C)


### ## 계속 헷갈리는 JOIN....

- INNER JOIN : 양쪽에 모두 있는 것만 남긴다.
- LEFT OUTER JOIN : 왼쪽 테이블은 모두 남기고, 오른쪽에 매칭되는 게 있으면 붙있다. 없으면 NULL


### ## 로직 고민
**<상황>**
- 화면에서 group_sn을 기준으로 stamp 이미지 매핑해야 함.
- 그런데 group_sn이 자동 증가 PK이라 프론트에서 값을 알 수 없음.

**<설계 옵션>**
1. 배열 인덱스(rowKey)로 매핑하자.
   - 프론트에서 group 배열의 index를 임시 key로 사용.
   - 백엔드에서 groupList를 순서대로 insert/update한 후 채번 획득.
   - 
2. group_sn을 자동 증가하지 말고, 직접 채번하자.


### ## auto identity가 안전한 이유
- DB 내부 시퀀스 자체가 락 없이 원자적으로 증가함.
- 요청이 동시에 와도 DB가 각자 다른 번호를 보장함.
- Max + 1의 문제점 : 읽고, 계산해서, insert 함.


### ## @Transactional 동작 핵심
- @Transactional은 기본적으로 Spring AOP 프록시 기반으로 동작한다.
- Controller -> Service 프록시 객체 


### ## postgreSQL insert or update
```sql
INSERT INTO table_name () VALUES ()
ON CONFLICT (column_name1, column_name2)
DO UPDATE SET column_name3 = ''
```

### ## @Transactional 동작 핵심
- @Transactional은 Spring AOP 프록시 기반으로 동작한다.


## # Refactoring


### ## 리팩토링 방향 트레이드 오프

**방식1. 컬럼 추가 (현재 방식)**
- **장점**
  - 단일 테이블만 select - join없이 한 번에 조회
  - 기존 Mapper, Model, Service 코드 변경 최소
  - 배포/마이크레이션 리스크 없음

- **단점**
  - 타입 추가마다 운영 DB에 ALTER TABLE ADD COLUMN 필요 => 운영 스키마 변경 반복
  - 현재도 13개 컬럼이 타입별로 분산되어 있음 -> 타입이 늘수록 null 컬럼이 많아짐.
  - 동일한 성격의 컬럼이 타입별로 별도로 존재함.

**방식2. 별도 테이블로 분리 (리팩토링 방향)**
- **장점**
  - 새 이벤트 타입 추가 시 기존 테이블 무관 -> 새 테이블만 추가
  - 각 테이블의 책임이 명확하고 null 컬럼 없음.
  - 동일한 성격의 타입은 하나의 테이블로 관리 가능.
  
- **단점**
  - 조회 시 타입별로 LEFT JOIN이 필요. -> 쿼리 복잡도 증가
  - Mapper XML, DAO, Model, Service까지 전 레이어 수정 공수가 큼.
  - 저장 시 트랜잭션 내 insert 대상 테이블 증가.


### ## Mockito의 stub 방식
- when()
- doAnswer()

```java

class EventV2ServiceTest { 
    @Test
    public void test() {
        // insertEventV2를 호출하면 1 반환
        when(mapper.insertEventV2RewardCond(any())).thenReturn(1);

        
        // mock 메서드가 호출될 때 람다 안의 코드가 실제로 실행함.
        // inv.getArgument(0) : insertEventV2RewardCond 호출 시 넘겨진 첫 번째 인자
        doAnswer(inv -> {
            ((EventV2RewardCondModel) inv.getArgument(0)).setRewardCondSn(1);
            return 1;
        }).when(mapper).insertEventV2RewardCond(any());
    }
}

```
- insert하면서 자동 채번할 때 doAnswer 사용할 수 있음.

### ## lenient()

- @ExtendWith(MockitoExtension.class)의 기본 동작
  - 규칙 1 — stub을 만들었으면 반드시 사용해야 한다.
  - 규칙 2 — 예상한 인자와 다른 인자로 호출되면 stub이 적용 안 된다
- lenient()는 규칙 1(미사용 stub 감지)를 끈다.

```java
// 호출 안돼도 무시
lenient().when(mapper.insertEventV2(any())).thenReturn(1);
```

### ## 테스트에서 테스트 대상 객체가 null
- @InjectMocks로 주입하는 service가 null이다.
- @Test import 확인하기
  - Junit4 : import org.junit.Test;
  - Junit5 : import org.junit.jupiter.api.Test;
- Junit5로 변경하니 잘 됨.


### ## selectKey와 useGeneratedKeys 차이
- selectKey는 insert 실행 전이나 후에 키 값을 조회해서 파라미터에 세팅하는 기능이다.

```xml

<insert id="insertEvent">
  <selectKey>
    SELECT NEXTVAL('EVENT_SEQ')
  </selectKey>
</insert>
```

- `order="BEFORE"` : selectKey를 먼저 실행 -> keyProperty에 값을 셋팅 -> insert를 실행 
- `order="AFTER` : insert 먼저 실행 -> selectKey 실행 ()