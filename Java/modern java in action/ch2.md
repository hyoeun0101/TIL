# 챕터2. 동작 파라미터화 코드 전달하기

- 변화하는 요구사항에 대응
- 동작 파라미터화
- 익명 클래스
- 람다 표현식 미리보기
- 실전 예제 : Comparator, Runnable, GUI

- `동작 파라미터화`란 어떻게 실행할 것인지 아직 결정하지 않은 코드 블록을 의미한다.

## 2.1 변화하는 요구사항에 대응하기

### 2.1.1 첫번 째,녹색 사과 필터링

```java
public static List<Apple> filterGreenApples(List<Apple> inventory) {
    List<Apple> result = new ArrayList<>();
    for(Apple apple: inventory) {
        if(GREEN.equals(apple.getColor())) {
            result.add(apple);
        }
    }
    return result;
}
```

### 2.1.2 두번 째, 빨간 사과도 필터링

```java
public static List<Apple> filterApples(List<Apple> inventory, Color color) {
    List<Apple> result = new ArrayList<>();
    for(Apple apple: inventory) {
        if(apple.getColor().equals(color)) {
            result.add(apple);
        }
    }
    return result;
}
```

- 필터링 부분은 계속 바뀐다.
  - 이는 DRI(don't repeat yourself, 같은 것을 반복하지 말 것) 원칙을 어긴다.

### 2.1.3 세번 째, 가능한 모든 속성으로 필터링

```java
public static List<Apple> filterGreenApples(List<Apple> inventory, Color color,int weight, boolean flag) {
    List<Apple> result = new ArrayList<>();
    for(Apple apple: inventory) {
        if((apple.getColor().equals(color) && flag) || (apple.getWeight() > weight && !flag)) {
            result.add(apple);
        }
    }
    return result;
}
```

- 정말 마음에 들지 않는다;;
- 이제 동작 파라미터화의 필요성을 알겠는가?

## 2.2 동작 파라미터화

- Predicate : boolean 값 반환하는 함수
- 메서드가 다양한 동작(전략)을 받아서 내부적으로 동작을 수행한다.

### 2.2.1 네번 째, 추상적 조건으로 필터링 : Predicate

```java
public static List<Apple> filterGreenApples(List<Apple> inventory, ApplePredicate p) {
    List<Apple> result = new ArrayList<>();
    for(Apple apple: inventory) {
        if(p.test(apple)) {
            result.add(apple);
        }
    }
    return result;
}
```

```java
//코드, 동작 전달하기
public class AppleRedAndHeavyPredicate implements ApplePredicate {
    public boolean test(Apple apple) {
        return RED.equals(apple.getColor() && apple.getWeight() > 150);
    }
}

```

```java
//실행
List<Apple> redAndHeavyApples = filterApples(inventory, new AppleReadAndHeavyPredicate());
```

## 2.3 복잡한 과정 간소화 = 익명 클래스 사용하기

### 2.3.1 다섯 번째, 익명 클래스 사용

```java
//실행 시 익명 클래스 사용하기
List<Apple> redAndHeavyApples = filterApples(inventory, new ApplePredicate() {
        @Override
        public boolean test(Apple a) {
            return RED.equals(a.getColor());
        }
    }
);
```

- 하지만 익명 클래스는 여전히 많은 공간을 차지하며, 한눈에 알아보기 힘들다.

### 2.3.2 여섯 번째, 람다 표현식 사용

```java
//실행 시 람다 사용하기
List<Apple> redAndHeavyApples = filterApples(inventory, (Apple a) -> RED.equals(a.getColor()));
```

### 2.3.4 일곱 번째, 리스트 형식으로 추상화

```java
public static <T> List<T> filter(List<T> list, Predicate<T> p) {
    List<T> result = new ArrayList<>();
    for(T e : list) {
        if(p.test(e)) {
            result.add(e);
        }
    }
    return result;
}
```

- 이제 Apple 말고도 다양한 타입을 가진 리스트에 filter메서드를 사용할 수 있다.

```java
List<Apple> redApples = filter(inventory, (Apple a) -> RED.equals(a.getColor()));

List<Integer> smallNumbers = filter(inventory, (Integer n) -> n < 10);
```

## 2.4 실전 예제

- Comparator로 정렬하기
- Runnable로 코드 블록 실행하기
- GUI 이벤트 처리하기

### 2.4.1 Comparator로 정렬하기

- java.util.Comparator 인터페이스를 이용해서 sort의 동작을 파라미터화하기

```java
public interface Comparator<T> {
    int compare(T o1, T o2);
}
```

```java
inventory.sort( new Comparator<Apple>() {
        @Override
        public int compare(Apple a1, Apple a2) {
            return a1.getWeight().compareTo(a2.getWeight());
        }
    }
);

//람다로 작성
inventory.sort((Apple a1, Apple a2) -> a1.getWeight().compareTo(a2.getWeight()));

```

### 2.4.2 Runnable로 코드 블록 실행하기

```java
Thread tread = new Thread(new Runnable() {
        @Override
        public void run() {
            System.out.println("hello");
        }
    }
);

//람다로 작성
Thread tread = new Thread(() -> System.out.println("hello"));

```

### 2.4.3 GUI 이벤트 처리하기

- 자바 5부터 지원하는 ExecutorService 추상화 개념.
- ExecutorService 인터페이스 : 태스크 제출과 실행 과정의 연관성을 끊어준다.
  - 태스크를 스레트 풀로 보내고 결과를 Future로 저장한다.
  - Callable 인터페이스를 이용해 결과를 반환하는 태스크를 만든다.

```java
//java.util.concurrent.Callable
public interface Callble<V> {
    V call();
}
```

```java
ExecutorService es = ExecutorService.newCachedThreadPool();
Future<String> threadName = es.submit(new Callable<String>() {
        @Override
        public String call() throws Exception {
            return Thread.currentThread().getName();
        }
    }
);

//람다로 작성
Future<String> threadName = es.submit(() -> Thread.currentThread().getName(););
```
