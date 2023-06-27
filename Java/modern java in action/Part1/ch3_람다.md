# 챕터 3. 람다 표현식

- 람다란 무엇인가?
- 어디에, 어떻게 람다를 사용하는가?
- 실행 어라운드 패턴
- 함수형 인터페이스, 형식 추론
- 메서드 참조
- 람다 만들기

## 🍎 1. 람다란 무엇인가?

- 람다 표현식: 메서드로 전달할 수있는 익명 함수를 단순화한 것.

## 🍎 2. 어디에, 어떻게 람다를 사용할까?

- `함수형 인터페이스`라는 문맥에서 람다 표현식을 사용할 수 있다.

### 함수형 인터페이스

- 오직 하나의 추상 메서드만 가진다.
- 디폴트 메서드가 있더라도 추상 메서드가 오직 하나면 함수형 인터페이스다.
- 함수형 인터페이스를 사용하서 람다 표현식으로 구현 클래스를 전달할 수 있다.

### 함수 디스크립터

- 시그니처 : 함수형 인터페이스의 추상 메서드 = 람다 표현식의 시그니처
- 함수 디스크립터 : 람다 표현식의 시그니처를 서술하는 메서드

- 한개의 void 메소드 호출 역시 중괄호가 필요없다.

```java
process(() -> System.out.println("haha"));
```

## 🍎 3 람다 활용: 실행 어라운드 패턴

- 자원 처리할 때 이 패턴을 가진다.
- 자원을 열고, 처리한 다음, 자원을 닫는다.
- init -> 작업 -> close
- 이렇게 설정과 정리 과정으로 감싸있는 형식의 코드를 실행 어라운드 패턴이라고 한다.

```java
// 1. 기존 코드
public String processFile() throws IOException {
    try(BufferedReader br = new BufferedReader(new FileReader("data.txt"))) {
        return br.readeLine();
    }
}
```

```java
// 2. 동작 파라미터화하기
@FunctionalInterface
public interface BufferedReaderProcessor {
    String process(BufferedReader b) throw IOException;
}


public String processFile(BufferedReaderProcessor p) throws IOException {
    try(BufferedReader br = new BufferedReader(new FileReader("data.txt"))) {
        return p.process(br);
    }
}
```

```java
//3. 실행하기
String oneLine = processFile((BufferedReader br) -> br.readLine());

String twoLine = processFile((BufferedReader br) -> br.readLine() + br.readLine());
```

## 🍎4. 함수형 인터페이스 사용

- 종류 : Predicate, Comsumer, Function
- 기본형 특화
  - 박싱 : 기본형 -> 참조형
  - 언박싱 : 참조형 -> 기본형
  - 오토박싱 : 박싱과 언박싱이 자동으로 이루어지는데, 이는 메모리 비용이 소모된다.
  - `IntPredicate`
  - Predicate<Integer>는 Integer 객체로 박싱하지만, IntPredicate는 박싱하지 않음.

```java
public interface IntPredicate {
    boolean test(int t);
}
```

### 람다 표현식 예외처리

- 함수형 인터페이스는 예외 허용 x
- 예외를 던지는 람다 표현식을 만들려면 함수형 인터페이스에 예외를 정의하거나, 람다를 try/catch로 감싸야 한다.

```java
@FunctionalInterface
public interface BufferedReaderProcessor {
    String process(BufferedReader b) throw IOException;
}

```

- 자바 API에서 제공하는 함수형 인터페이스(Function, Predicate 등)은 람다 표현식 안에서 try/catch로 처리

```java
Function<BufferedReader, String> f = (BufferedReader b) -> {
    try {
        return b.readLine();
    } catch(IOException e) {
        throw new RuntimeException(e);
    }
}
```

## 🍎 5. 형식 검사, 형식 추론, 제약

- 타입 검사
- 같은 람다, 다른 함수형 인터페이스 : 같은 람다 표현식이라도 함수형 인터페이스에 따라 다르게 해석할 수 있다.
- 타입 추론 : 파라미터 타입을 작성하지 않아도 된다.
- 지역 변수 사용
  - 람다에서 사용하는 지역 변수는 final이다.

## 🍎 6. 메서드 참조

```java
inventory.sort((Apple a1, Apple a2) -> a1.getWeight().compareTo(a2.getWeight()));

//메서드 참조, java.util.Comparator.comparing 활용
inventory.sort(comparing(Apple::getWeight));
```

- 가독성을 높인다.

| 람다                                     | 메서드 참조                     |
| ---------------------------------------- | ------------------------------- |
| (Apple apple) -> apple.getWeight()       | Apple::getWeight                |
| () -> Thread.currentThread().dumpStack() | Thread.currentThread::dumpStack |
| (str, i) -> str.substring(i)             | String::subString               |
| (String s) -> Sysmtem.out.println(s)     | System.out::println             |
| (String s) -> this.isValidName(s)        | this::isValidName               |

### 기존 메서드를 메서드 참조로

- `클래스이름::메서드이름`
- `참조변수::메서드이름`- 잘안씀

1. 정적 메서드 참조
   - Integer::parseInt
2. 다양한 형식의 인스턴스 메서드 참조
   - String::length
3. 기존 객체의 인스턴스 메서드 참조
   - expensiveTx::getValue
   - this.isValidName

### 메서드 참조 만들어보기

```java
List<String> str = Arrays.asList("a","b","A","B");
//람다
str.sort((s1, s2) -> s1.compareToIgnoreCase(s2));
//메서드 참조
str.sort(String::compareToIgnoreCase)
```

| 람다                                      | 메서드 참조               |
| ----------------------------------------- | ------------------------- |
| (args) -> ClassName.staticMethod(args)    | ClassName::staticMethod   |
| (arg0, rest) -> arg0.instanceMethod(rest) | ClassName::instanceMethod |
| (args) -> expr.instanceMethod(args)       | expr::instanceMethod      |

### 생성자 참조

```java
//Supplier<Apple> c1 = () -> new Apple();
Supplier<Apple> c1 = Apple::new;
Apple a1 = c1.get(); //Supplier의 get으로 Apple 객체 생성


//Function<Integer, Apple> c2 = (Integer weight) -> new Apple(weight);
Function<Integer, Apple> c2 = Apple::new;
Apple a1 = c2.apply(100); //생성자의 인수로 넣는다.

```

```java
// 리스트에 담은 weight로 Apple를 생성
List<Integer> weight = Arrays.asList(7,3,4,10);
List<Apple> apples = map(weight, Apple::new);

public List<Apple> map(List<Apple> list, Function<Integer, Apple> f) {
    List<Apple> result = new ArrayList<>();
    for(Integer i : list) {
        result.add(f.apply(i));
    }
    return result;
}
```

- 생성자의 인수가 두 개면 BiFunction을, 3개면 TriFunction을 사용해야 한다.

```java
BiFunction<Color, Integer, Apple> bf = Apple::new;

Apple a1 = bf.apply(GREEN, 110);
```

## 🍎7. 람다, 메서드 참조 활용

1. 기존 코드

```java
public class AppleComparator implements Comparator<Apple> {
    @Override
    public int compare(Apple a1, Apple a2) {
        return a1.getWeight().compareTo(a2.getWeight());
    }
}
...

inventory.sort(new AppleComparator);
```

2. 익명 클래스 사용

```java
inventory.sort(new Comparator<Apple> {
    @Override
    public int compare(Apple a1, Apple a2) {
        return a1.getWeight().compareTo(a2.getWeight());
    }
})
```

3. 람다 표현식 사용

```java
inventory.sort((Apple a1, Apple a2) -> a1.getWeight().compareTo(a2.getWeight()));
//또는
inventory.sort((a1, a2) -> a1.getWeight().compareTo(a2.getWeight()));

//또는
import static java.util.Comparator.comparing;

inventory.sort(comparing(apple -> apple.getWeight()));
```

4. 메서드 참조 사용

```java
inventory.sort(comparing(Apple::getWeight));
```

## 🍎8.담다 표현식 조합

### Comparator 조합
