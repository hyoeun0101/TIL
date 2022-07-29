# Calendar
: 추상 클래스

getInstance()로 객체 생성. 오버로딩되어 있음.

### get()메서드
```java
Calendar c = Calendar.getInstance();
System.out.println(c.get(Calendar.YEAR));
```
|필드|설명|
|----|----|
|YEAR|올해년도|
|MONTH|몇 월(0~11)|
|DATE, DATE_OF_MONTH|몇 일|
|올해의 몇째주|WEEK_OF_YEAR|
|이달의 몇째주|WEEK_OF_MONTH|
|이 해의 몇 일|DATE_OF_YEAR|
|요일(1-일요일,2-월요일,3-화요일...)|DAY_OF_WEEK|
|이 달의 몇 번째 요일|DAY_OF_WEEK_IN_MONTH|
|오전-0, 오후-1|AM_PM|
|시간(0~11)|HOUR|
|시간(0~23)|HOUR_OF_DAY|
|분, 초|MINUTE, SECOND, MILLISECOND|

이 달의 마지막 날   
: c.getActualMaximum(Caldendar.MONTH)
