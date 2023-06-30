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

- 시그니처는 함수형 인터페이스의 추상 메서드를 말하며 이는 곧 람다 표현식의 시그니처이다.
- 함수 디스크립터 : 람다 표현식의 시그니처를 서술하는 메서드
- ex) `Predicate<Apple>`의 시그니처는 `test`이고, 함수 디스크립터는 `Apple -> boolean` 이다.

## 🍎 3. 실행 어라운드 패턴에 람다 활용하기

- 자원 처리할 때 이 패턴을 가진다.
- 자원을 열고, 처리한 다음, 자원을 닫는 패턴. (init -> 작업 -> close)
- 이렇게 설정과 정리 과정으로 감싸있는 형식의 코드를 실행 어라운드 패턴이라고 한다.
- 작업을 처리하는 코드를 람다로 작성하여 동작 파라미터화한다.

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

## 🍎4. 함수형 인터페이스 사용하기

![in](https://github.com/PSVM2022/Dopamin/assets/96059261/80d13c49-0e02-4f26-b568-f8eb5e0ad2ba)

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

### 람다 표현식 예외 처리하기

- 예외를 던지는 람다 표현식을 만들려면 함수형 인터페이스에 예외를 정의하거나, 람다를 try/catch로 감싸야 한다.

```java
@FunctionalInterface
public interface BufferedReaderProcessor {
    String process(BufferedReader b) throw IOException; //예외 정의
}
...
BufferedReaderProcessor p = (BufferedReader br) -> br.readLine();

```

- 자바 API에서 제공하는 함수형 인터페이스(Function, Predicate 등)은 람다 표현식 안에서 try/catch로 처리해야 한다.

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

컴파일러가 람다의 형식을 어떻게 확인하는지,
피해야 할 사앟ㅇ은 무엇인지

1. 컴파일러가 람다의 타입 확인하는 법
   - 람다가 사용되는 콘텍스트를 이용해서 람다의 타입을 추론할 수 있다. taget type?
   - `List<Apple> heavierThan150g = filter(inventory, (Apple apple) -> apple.getWeight() > 150);`
     - filter 메서드의 선언을 확인한다.
     - filter의 두 번째 파라미터는 Predicate<T> 타입이다. T은 Apple로 대치된다.
     - Predicate의 시그니처 test 메서드를 확인한다. test는 Apple을 인자로 받아 boolean을 반환한다.
     - 람

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

```java
@FunctionalInterface
public interface Comparator<T> {
    int compare(T o1, To2);

    default Comparator<T> reversed() {
        return Collections.reversOrder(this);
    }

    default Comparator<T> thenComparing(Comparator<? super T> other) {
        ...
    }
    ...
}
```

- `reversed()` : 디폴트 메서드 `reversed`를 사용하면 역정렬을 할 수 있다.
- `thenComparing` : 조건이 같다면 `thenComparing`으로 Comparator를 연결하여 추가할 수 있다.

```java
inventory.sort(comparing(Apple::getWeight)
                .reversed() // 무게를 내림차순으로 정렬
                .thenComparing(Apple::getCountry) //두 사과의 무게가 같으면 국가별로 정렬
                );
```

### Predicate 조합

```java
@FunctionalInterface
public interface Predicate<T> {
    boolean test(T t);

    default Predicate<T> and(Predicate<? super T> other) {
        Objects.requireNonNull(other);
        return (t) -> test(t) && other.test(t);
    }

    default Predicate<T> negate() {
        return (t) -> !test(t);
    }

    default Predicate<T> or(Predicate<? super T> other) {
        Objects.requireNonNull(other);
        return (t) -> test(t) || other.test(t);
    }
    ...

}
```

```java
Predicate<Apple> redApple = apple -> RED.equals(Apple::getColor); // 빨간 사과

//negate 예시
Predicate<Apple> notRedApple = redApple.negate(); //빨간색이 아닌 사과

//and 예시
Predicate<Apple> redAndHeavyApple = redApple.and(apple -> apple.getWeight() > 150); // 빨간색이고, 무거운 사과

//or 예시
Predicate<Apple> redAndHeavyOrGreenApple = redApple.and(apple -> apple.getWeight() > 150)
                                                   .or(apple -> GREEN.equals(apple.getColor())); // 빨간색이면서 무거운 사과 또는 녹색 사과
```

### Function 조합

```java
@FunctionalInterface
public interface Function<T, R> {

    R apply(T t);

    default <V> Function<V, R> compose(Function<? super V, ? extends T> before) {
        Objects.requireNonNull(before);
        return (V v) -> apply(before.apply(v));
    }

    default <V> Function<T, V> andThen(Function<? super R, ? extends V> after) {
        Objects.requireNonNull(after);
        return (T t) -> after.apply(apply(t));
    }

}
```

- `andThen` : 주어진 함수를 먼저 적용한 결과를 다른 함수의 입력으로 전달한다.
- `compose` : 인수로 주어진 함수를 먼저 실행하고, 그 결과를 외부 함수의 인수로 전달한다.

```java
//andThen 예시
Function<Integer, Integer> f = x -> x + 1;
Function<Integer, Integer> g = x -> x * 2;
Function<Integer, Integer> h = f.andThen(g); // f의 결과를 g의 입력으로 전달, 즉 g(f(x))
int result = h.apply(1); // 4

//compose 예시
Function<Integer, Integer> f = x -> x + 1;
Function<Integer, Integer> g = x -> x * 2;
Function<Integer, Integer> h = f.compose(g); // g의 결과를 f의 입력으로 전달, 즉 f(g(x))
int result = h.apply(1); // 3
```
