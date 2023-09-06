# Optional<T>
T타입의 객체를 감싸는 래퍼 클래스   
- java.util.Optional 은 JDK1.8부터 추가
- Optional로 한번 감싸므로써 NullPointerException이 발생하지 않음

`0X100` ->Optional 객체 `0X200` -> `"hello"`
```java
public final class Optional<T>{
    private final T value;
}
```

## Optional<T> 객체 생셩하기
```java
String str = "abc"l
Optional<String> optVal = Optional.of(str);

Optional<String> optVal = Optional.of("abc");

Optional<String> optVal = Optional.of(new String("abc"));
```

null일 가능성 있다면 ofNullable
```java
Optional<String> optVal = Optional.of(null);//NullPointerException 발생

Optional<String> optVal = Optional.ofNullabl(null);//null이어도 예외발생x
```

기본값으로 초기화하기
```java
Optional<String> optVal = Optional.<String>empty();//빈 객체로 초기화
```

## Optional<T> 객체 값 가져오기
- get() : null이면 `NoSuchElementException` 발생
- ofElse(값) : null이면 대체 값 지정
- orElseGet(Supplier) : null이면 람다식 실행하여 값 지정, 디폴트 메서드를 만드는데 시간이 걸리거나(효율성때문) Optional이 비어있을때 기본값이 필요한 상황에 사용한다.
- orElseThrow(Supplier) : null이면 지정된 예외 발생
```java
Optional<String> optVal = Optional.of("abc");
String str1 = optVal.get(); // null이면 예외발생
String str2 = optVal.ofElse("");//null이면 ""으로 대체

String str3 = optVal.orElseGet(String::new);//null이면 빈 객체 생성
String str4 = optVal.orElseThrow(NullPointerException::new);//null이면 예외 발생
``` 

- isPresent() : null이면 false, 아니면 true
- ifPresent(Consumer) : 값이 있으면 람다식 실행, 없으면 아무 일도 안함
- ifPresentOrElse(Consumer, Runnable) : 값이 비어있으면 Runnable을 실행
```java
if(Optional.ofNullable(str).isPresent()){
    System.out.println(str);
}

Optional.ofNullable(str).ifPresent(System.out::println);
```
## OptionalInt, OptionalLong, OptionalDouble
getAsInt(), getAsLong(), getAsDouble()

```java
OptionalInt opt = OptionalInt.of(0);//0을 저장
OptionalInt opt2 = OptionalInt.empty();//기본 초기화 0을 저장
```
opt와 opt2는 같을까?   다름!

```java
System.out.println(opt.isPresent());//true
System.out.println(opt2.isPresent());//false
System.out.println(opt.equals(opt2));//false

```
