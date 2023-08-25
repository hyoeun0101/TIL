- 자바8에서 새로운 날짜와 시간 라이브러리를 제공하는 이유
- 사람이나 기계가 이해할 수 있는 날짜와 시간 표현 방법
- 시간의 양 정의하기
- 날짜 조작, 포매팅, 파싱
- 시간대와 캘린더 다루기

## 🍎LocalDate, LocalTime, Instant, Duration, Period 클래스

- java.time 패키지는 LocalDate, LocalTime 등 새로운 클래스를 제공한다.

### LocalDate, LocalTime 사용

- LocalDate 날짜 표현하는 불변 객체.
  - 정적 팩토리 메서드 of로 생성한다.
  - 팩터리 메서드 now는 현재 날짜 정보를 얻는다.
  - get메서드에 TemporalField를 전달해서 정보를 얻을 수도 있다. ChronoField는 TemporalField 인터페이스를 정의한다.

```java
//of 예시
LocalDate date = LocalDate.of(2017, 9, 21);
int year = date.getYear(); //2017
Month month = date.getMonth(); //SEPTEMBER
int day = date.getDayOfMonth(); //21
DayOfWeek dow = date.getDayOfWeek();  //THURSDAY
int len = date.lengthOfMonth(); //31 (9월의 일수)
boolean leap = date.isLeapYear(); //false (윤년이 아님)

//now 예시
LocalDate today = LocalDate.now();

//get 예시
int year = date.get(ChronoField.YEAR);
int month = date.get(ChronoField.MONTH_OF_YEAR);
int day = date.get(ChronoField.DAY_OF_MONTH);
```

- 시간은 LocalTime으로 표현한다.
  - 정적 팩토리 메서드 of를 사용.
  - parse 정적 메서드에 DateTimeFormatter를 전달한다. DateTimeFormatter는 java.util.DateFormat 클래스를 대체하는 클래스이다. 제공한 문자열을 LocalDate, LocalTime으로 파싱할 수 없으면 DateTimeParseException(RuntimeExcetion임)이 발생한다.

```java
// of 예시
LocalTime time = LocalTime.of(13,45,20) //13:45:20
int hour = time.getHour(); //13
int minute = time.getMinute(); //45
int second = time.getSecond(); //20

// parse 예시
LocalDate date = LocalDate.parse("2017-09-21");
LocalTime time = LocalTime.parse("13:45:20");
```

### LoacalDateTime

```java
LocalDateTime dt1 = LocalDateTime.of(2017, Month.SEPTEMBER, 21, 13,45,20);
LocalDateTime dt2 = LocalDateTime.of(date, time);
LocalDateTime dt3 = date.atTime(time);
LocalDateTime dt4 = time.atDate(date);

dt1.toLocalDate(); // 2017-09-21
dt1.toLocalTime(); //13:45:20
```

### Instant 클래스 : 기계의 날짜와 시간

- unix epoch time을 기준으로 특정 지점까지의 시간을 초로 표현.
- 나노초 (10억분의 1초)의 정밀도를 제공한다.
- Instant는 사람이 읽을 수 있는 시간 정보를 제공하지 않는다. 기계 전용 유틸리티이다.

```java
Instant.ofEpochSecond(3);
Instant.ofEpochSecond(3, 0);
Instant.ofEpochSecond(2 , 1_000_000_000); // 2초 이후의 1억 나노초(1초)

int day = Instant.now().get(ChronoField.DAY_OF_MONTH); // 예외 발생

```

### Duration과 Period 정의

- Temporal 인터페이스 : 특정 시간을 모델링하는 객체의 값을 정의
- Duration : 두 시간 객체 사이의 시간 표현할 때, 두 개의 LocalTime, LocalDateTime, Instant로 Duration을 만들 수 있다. (초 단위로 표현하므로 LocalDate는 안됨)

```java
Duration d1 = Duration.between(time1, time2);
```

- Period : 년,월,일로 시간을 표현할 때

```java
Period tenDays = Period.between(LocalDate.of(2017, 9 , 11), LocalDate.of(2017, 9, 21));
```

```java
Duration threeMinutes = Duration.ofMunites(3);
Duration threeMinutes = Duration.ofMunites(3, ChronoUnit.MINUTES);

Period tenDays = Period.ofDays(10);
Period threeWeeks = Period.ofWeeks(3);
Period twoYearsSixMonOneDay = Period.ofDays(2,6,1);
```

## 🍎 날짜 조정, 파싱, 포매팅

```java
// 기존 객체를 변경하지 않고, 새로운 객체를 만든다.
LocalDate date1 = LocalDate.of(2017, 9, 21);
LocalDate date2 = date1.withYear(2011); // 2011-09-17
LocalDate date3 = date2.withDayOfMonth(25); //2011-09-25
LocalDate date4 = date3.with(ChronoField.MONTH_OF_YEAR, 2); //2011-02-25
```

### TemporalAdjusters 사용하기

- TemporalAdjsters에서 시간, 날짜를 조정하는 정적 메서드를 제공한다.

### 날짜와 시간 객체 출력, 파싱

## 🍎 다양한 시간대와 캘린더 활용법

### 시간대 사용하기

### UTC/Greenwich 기준의 고정 오프셋?

### 대안 캘린더 시스템 사용하기

- 이슬람력
