# java.util.Calendar 클래스

: 추상 클래스여서 직접 객체 생성할 수 없고, getInstance()로 구현체 반환한다.
**추상 클래스로 선언된 이유는??**

- 나라마다 사용하는 달력 체계가 다르기 때문에 각 달력 체계마다 구현부가 달라야한다.
  **public static Calendar getInstance()**
  getInstance()는 내부에서 인스턴스를 만든 후 반환만 한다. 구체적으로 createCalendar()를 반환한다.
  createCalendar()의 코드를 살짝 보면 조건에 따라 다른 객체를 생성하는 것을 볼 수 있다.  
  이 코드를 보니 OCP, DIP가 잘 적용되어있다는 것을 확인할 수 있었다.

```java
if (cal == null) {
    if (aLocale.getLanguage() == "th" && aLocale.getCountry() == "TH") {
        cal = new BuddhistCalendar(zone, aLocale);
    } else if (aLocale.getVariant() == "JP" && aLocale.getLanguage() == "ja"
                && aLocale.getCountry() == "JP") {
        cal = new JapaneseImperialCalendar(zone, aLocale);
    } else {
        cal = new GregorianCalendar(zone, aLocale);
    }
}
return cal;
```

java.util.GregorianCalendar 클래스는 Calendar의 자식 클래스로 그레고리오 달력을 구현한 클래스이며, 대부분의 나라에서는 이 달력 체계를 사용한다. 태국은 BuddhistCalendar의 인스턴스를 사용한다.
**getInstance()는 왜 static일까?**

- 인스턴스 멤버를 사용하지 않기 때문.
- Calendar는 추상 클래스이기 때문에 객체를 생성할 수 없다. 따라서 static 멤버로 선언해야한다.

```java
public class Ex{
    public static void main(String[] args) {
        Calendar today = Calendar.getInstance();
        int year = today.get(Calendar.YEAR);
        int month = today.get(Calendar.MONTH)+1; //0:1월
        int day = today.get(Calendar.DATE);
        char[] yoilArr = {' ','일','월','화','수','목','금','토'};
        int yoil = yoilArr[today.get(Calendar.DAY_OF_WEEK)];//1:일
        System.out.printf("오늘은 %d년 %d월 %d일 %c요일입니다.",year,month,day,yoil);

        System.out.println("이달의 마지막 달: "+today.getActualMaximum(Calendar.DATE));

        //시간
        System.out.println("0:오전, 1:오후 =>"+today.get(Calendar.AM_PM));
        System.out.println("시간(0~11): "+today.get(Calendar.HOUR));
        System.out.println("시간(0~23): "+today.get(Calendar.HOUR_OF_DAY));
        System.out.println("분(0~59): "+today.get(Calendar.MINUTE));
        System.out.println("초(0~59): "+today.get(Calendar.SECOND));
    }
}

```

**add()와 roll()의 차이**

- add() : 지정한 필드의 값을 증가, 감소시킬 수 있다. DATE를 증가시켰다면 월도 증가한다.
- roll() : add()와 비슷한데 이 메서드는 다른 필드에 영향을 미치지 않는다. DATE를 증가시키면 MONTH에 영향이 가지않는다. 단!! DATE가 말일 이라면 MONTH를 바꿨을 때, 그 달의 말일로 바뀐다.
  - 예) 3월 30일, `roll(Calendar.MONTH, 1)` => 4월 31일

**Date와 Calendar 변환**

1. Date -> Calendar

```java
Date d = new Date();
Calendar cal = Calendar.getInstance();
cal.setTime(d);
```

2. Calendar -> Date

```java
Calendar cal = Calendar.getInstance();
Date date = new Date(cal.getTimeInMillis());
```
