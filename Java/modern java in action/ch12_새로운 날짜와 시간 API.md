- 자바8에서 새로운 날짜와 시간 라이브러리를 제공하는 이유
- 사람이나 기계가 이해할 수 있는 날짜와 시간 표현 방법
- 시간의 양 정의하기
- 날짜 조작, 포매팅, 파싱
- 시간대와 캘린더 다루기

- java 1.0에서는 java.util.Date 클래스로 날짜와 시간 관련 기능을 제공했다. 
- java.time 패키지에서 LocalDate, LocalTime, LocalDateTime, Instant, Duration, Period 등 새로운 클래스를 제공한다. 이 새로운 API는 모두 불변 객체이다!

## 🍎LocalDate와 LocalTime

### LocalDate
- LocalDate는 날짜를 표현하는 불변 객체이다.
- 정적 팩토리 메서드 of로 생성한다.
- 팩터리 메서드 now는 현재 날짜 정보를 얻는다.

```java
LocalDate today = LocalDate.now(); //오늘 날짜

LocalDate date = LocalDate.of(2024,1,6);

int year = date.getYear(); //2024

Month month = date.getMonth(); // JANUARY

int day = date.getDayOfMonth(); // 6

DayOfWeek yoil = date.getDayOfWeek(); //SATURDAY

int monthLen = date.lengthOfMonth();//31

boolean leapYear = date.isLeapYear(); //true
```

- get 메서드에 TemporalField를 전달해서 날짜 정보를 얻을 수도 있다.
  - TemporalField : 시간 관련 객체에서 어떤 값에 접근할지 정의하는 인터페이스
  - ChronoFiled는 TemporalField 인터페이스의 구현 클래스이다.

```java
LocalDate date = LocalDate.of(2024, 1, 6);

// get메서드 사용
int year = date.get(ChronoField.YEAR); //2024
int month = date.get(ChronoField.MONTH_OF_YEAR); //1
int day = date.get(ChronoField.DAY_OF_MONTH); //6

// 메서드 사용
int year1 = date.getYear(); //2024
Month month1 = date.getMonth(); //JANUARY
int day1 = date.getDayOfMonth(); //6
```

### LocalTime
- LocalTime은 시간을 표현하는 불변 객체이다.
- 마찬가지로 정적 팩토리 메서드 of를 사용하여 생성한다.
```java
LocalTime time = LocalTime.of(13, 45, 20);

int hour = time.getHour(); //13
int minute = time.getMinute(); //45
int second = time.getSecond(); // 20
```

### parse 메서드
- parse 메서드에 문자열을 전달하여 LocalDate, LocalTime을 생성할 수 있다.
  - 제공한 문자열을 LocalDate, LocalTime으로 파싱할 수 없으면 DateTimeParseException(RunTimeException)이 발생한다.
```java
// 날짜
LocalDate date = LocalDate.parse("2023-11-20");
int year = date.getYear(); //2023
Month month = date.getMonth(); //NOVEMBER
int day = date.getDayOfMonth(); //20

// 시간
LocalTime time1 = LocalTime.parse("14:30:10");
int hour1 = time1.getHour(); //14
int minute1 = time1.getMinute(); //30
int second1 = time1.getSecond(); //10
```

- parse 메서드에 DateTimeFormatter를 전달할 수도 있다.
  - DateTimeFomatter는 날짜, 시간의 포맷 형식을 지정하며 java.util.DateFormat 클래스를 대체하는 클래스이다.

## 🍎LocalDateTime
- LocalDateTime은 날짜와 시간 모두 표현할 수 있는 클래스이다.

```java
// 1. of로 생성
LocalDateTime dateTime = LocalDateTime.of(2017, Month.APRIL, 1, 13, 45, 20);

// 2. LocalDate와 LocalTime 조합
LocalDate date = LocalDate.of(2017, 4, 1);
LocalTime time = LocalTime.of(13, 45, 20);
LocalDateTime dateTime1 = LocalDateTime.of(date, time);

// 3. LocatDate에 LocalTime 세팅
LocalDateTime dateTime2 = date.atTime(time);

// 4. LocalTime에 LocalDate 세팅
LocalDateTime dateTime3 = time.atDate(date);


// LocatDateTime에서 날짜와 시간 추출
LocalDate localDate = dateTime.toLocalDate();
LocalTime localTime = dateTime.toLocalTime();

```


## 🍎 Instant : 기계의 날짜와 시간
- Instant는 기계 전용 유틸리티로 초와 나노초(10억분의 1초) 정보를 포함한다.
- 사람이 읽을 수 있는 시간 정보(시,분,초)는 제공하지 않는다.
- 유닉스 에포크 시간(unix epoch time)을 기준으로 특정 지점까지의 시간을 초로 표현한다.
- Duration, Period 클래스와 함께 사용된다.
- offEpochSeconde 메서드를 통해 생성한다.

```java
Instant instant = Instant.ofEpochSecond(3); //1970-01-01T00:00:03Z

Instant instant1 = Instant.ofEpochSecond(3, 0); //1970-01-01T00:00:03Z

Instant instant2 = Instant.ofEpochSecond(2, 1_000_000_000); //1970-01-01T00:00:03Z

Instant instant3 = Instant.ofEpochSecond(4, -1_000_000_000); //1970-01-01T00:00:03Z


//모두 같은 Instant를 반환한다.
System.out.println("instant equals instant3 " + instant.equals(instant3)); //true

// UnsupportedTemporalTypeException 발생. Instant는 사람이 읽을 수 있는 정보를 제공하지 않는다.
int year = Instant.now().get(ChronoField.YEAR);
```


## 🍎 Duration, Period
### Duration
- 두 시간 객체의 차이를 표현하는 불변 클래스 ex) PT1H30M30S
- 두 개의 LocalTime, 두 개의 LocalDateTime, 두 개의 Instant로 Duration을 만들 수 있다.
- 초와 나노초로 시간 단위를 표현한다.
- between 메서드를 통해 생성한다.
```java
LocalTime time1 = LocalTime.of(10,0);
LocalTime time2 = LocalTime.of(11, 30);

Duration duration1 = Duration.between(time1, time2);
System.out.println("duration1 = " + duration1); // PT1H30M

LocalDateTime dateTime1 = LocalDateTime.of(2024,1,1,0,0,0);
LocalDateTime dateTime2 = LocalDateTime.of(2024, 1, 2, 0, 0, 0);

Duration duration2 = Duration.between(dateTime1, dateTime2);
System.out.println("duration2 = " + duration2); // PT24H

Instant instant1 = Instant.ofEpochSecond(3);
Instant instant2 = Instant.ofEpochSecond(4);

Duration duration3 = Duration.between(instant1, instant2);
System.out.println("duration3 = " + duration3); // PT1S
```
- between메서드 외의 다양한 메서드로 생성할 수 있다.

```java
Duration oneDay = Duration.ofDays(10); // PT240H
Duration threeMinutes = Duration.ofMinutes(3); //PT3M
Duration threeMinutes2 = Duration.of(3, ChronoUnit.MINUTES); //PT3M

```
### Period
- 두 날짜 객체의 차이를 표현하는 불변 클래스. ex) P2Y6M30D
- between 메서드를 통해 생성한다.
```java
LocalDate date1 = LocalDate.of(2023, 1, 1);
LocalDate date2 = LocalDate.of(2024, 1, 1);
Period period = Period.between(date1, date2); // P1Y
```
- between메서드 외의 다양한 메서드로 생성할 수 있다.
```java
Period tenDays = Period.ofDays(10); // P10D
Period threeWeeks = Period.ofWeeks(3); // P21D
Period twoYearSixMonthOneDay = Period.of(2, 6, 1); // P2Y6M1D
```

![Alt text](/Java/modern%20java%20in%20action/img/duration&period.png)





## 🍎 날짜 변경
- LocalDate, LocalTime, LocalDateTime, Instant의 값을 변경하면 새로운 객체를 만든다.

```java
// 기존 객체를 변경하지 않고, 새로운 객체를 만든다.
LocalDate date1 = LocalDate.of(2017, 9, 17);

// year 변경.
LocalDate date2 = date1.withYear(2011); // 2011-09-17

// month 변경.
LocalDate date3 = date2.withMonth(2); //2011-02-17
// LocalDate date3 = date2.with(ChronoField.MONTH_OF_YEAR, 2);

LocalDate date4 = date3.withDayOfMonth(10); // 2011-02-10
```

## 🍎 TemporalAdjusters
- TemporalAdjsters 클래스는 TemporalAdjuster 인터페이스의 구현 클래스로 보다 복잡한 시간과 날짜를 조정하는 다양한 정적 메서드를 제공한다.

```java
import static java.time.temporal.TemporalAdjusters.*;

LocalDate date1 = LocalDate.of(2014, 3, 18);
LocalDate date2 = date1.with(nextOrSame(DayOfWeek.SUMDAY)); // 2014-03-23
LocalDate date3 = date2.with(lastDayOfMonth()); //2014-03-31
```
<img width="581" alt="스크린샷 2023-08-25 오후 11 26 30" src="https://github.com/PSVM2022/Dopamin/assets/96059261/96aeceac-ff74-476d-bc73-d0254e778321">

### TemporalAdjuster 인터페이스 구현하기
- TemporalAdjusters 클래스에 정의되어 있지 않을 때는 커스텀 TemporalAdjuster 구현을 만들 수 있다.
```java
@FunctionalInterface
public interface TemporalAdjuster {
  Temporal adjustInto(Temporal temporal);
}
```


## 🍎 포맷팅
- java.time.format이 새로 추가되었다. 핵심은 DateTimeFormatter 클래스이다.

### DateTimeFormmater
- 날짜나 시간을 특정 형식의 문자열로 만든다.
- BASIC_ISO_DATE, ISO_LOCAL_DATE 상수를 미리 정의하고 있다.
- java.util.DateFormat 클래스와 달리 모든 DateTimeFormatter는 스레드에서 안전하게 사용할 수 있다.

```java
// LocalDate의 format 메서드에 전달
LocalDate date = LocalDate.of(2014, 3, 18);
String strDate1 = date.format(DateTimeFormatter.BASIC_ISO_DATE); // 20140318
String strDate2 = date.format(DateTimeFormatter.ISO_LOCAL_DATE); // 2014-03-18
```
```java
//ofPattern 메서드 사용
DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
LocalDate date = LocalDate.of(2014, 1, 1);

String strDate = date.format(formatter); // 01/01/2014
```

- 지역화된 DateTimeFormatter
```java
DateTimeFormatter italFormatter = DateTimeFormatter.ofPattern("d.MMMM yyyy", Locale.ITALIAN);
DateTimeFormatter koreaFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd", Locale.KOREA);


LocalDate date = LocalDate.of(2014, 3, 18);

String italDate = date.format(italFormatter); // 18.marzo 2014
String koreaDate = date.format(koreaFormatter); // 2014.03.18
```

- DateTimeFormatterBuilder 클래스로 더 세부적인 포매터를 제어.
  - 대소문자를 구분하는 파싱
  - 정해진 형식과 일치하지 않는 경우 다른 방식의 파서 사용
  - 패딩
  - 포매터의 선택사항 등

```java
DateTimeFormatter italianFormatter = new DateTimeFormatterBuilder()
          .appendText(ChronoField.DAY_OF_MONTH)
          .appendLiteral(". ")
          .appendText(ChronoField.MONTH_OF_YEAR)
          .appendLiteral(" ")
          .appendText(ChronoField.YEAR)
          .parseCaseInsensitive()
          .toFormatter(Locale.ITALIAN);
```

