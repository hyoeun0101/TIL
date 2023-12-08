## 🍎Collector 인터페이스의 추상 메서드

```java
public interface Collector<T, A, R> {
  Supplier<A> supplier();
  BiConsumer<A, T> accumulator();
  Function<A, R> finisher();
  BinaryOperator<A> combiner();
  Set<Characteristics> characteristics();
}
```

- T는 수집될 스트림의 타입이다.
- A는 누적자, 즉 중간 결과를 누적하는 객체의 타입이다.
- R은 연산 결과 타입이다.

### supplier

- 컬렉션을 생성하여 반환한다.

```java
public Supplier<List<T>> supplier(0 {
  return () -> new ArrayList<>();
  // return () -> ArrayList::new;
})
```

### accumulator

- 컬렉션에 값을 누적한다.(추가한다)

```java
public BiConsumer<List<T>, T> accumulator() {
  return (list, item) -> list.add(item);
  // return List::add;
}
```

### finisher

- 누적한 결과를 담은 객체를 최종 결과로 반환한다.

```java
// 누적 객체가 이미 최종 결과 상태일 경우, 항등 함수 반환
public Funtion<List<T>, List<T>> finisher() {
  return Function.identity();
}
```
- 정리하자면 supplier로 컬렉션 생성, accumulator로 누적, finisher로 최종 결과 반환 과정을 거친다.

### combiner

- 병렬화 리듀싱 과정에서 사용한다. 두 개의 서브 스트림의 결과를 병합한다.

```java
public BinaryOperator<List<T>> combiner() {
  return (list1, list2) -> {
    list1.addAll(list2);
    return list1;
  }
}
```

### 정리

- 순차 스트림의 경우

  - 순차 알고리즘으로 처리한다.
  - supplier로 누적할 것 얻고, accumulator로 데이터 누적. 스트림 요소가 남아있으면 스트림의 다음 항목 누적.
  - 스트림 요소 남아있지 않으면 finisher 실행

- 병렬 스트림의 경우
  - 스트림을 두 개의 서브파트로 분할, 재귀적으로 서브파트를 최대한 작게 만듦.
  - 각각의 서브 파트에서 순차 알고리즘을 실행, 즉 병렬로 처리.
  - 각각의 서브 파트의 결과를 combiner로 합침.
  - 결과를 finisher를 통해 반환

### characteristics

- 스트림을 병렬로 처리할 것인지 정하고, 병렬로 처리 시 어떤 최적화를 사용할 건지 선택한다.
- unordered : 리듀싱 결과는 스트림 요소의 순서에 영향을 받지 않음.
- concurrent : 스트림의 병렬 수행 가능, unordered 없으면, 데이터가 정렬되지 않은 상황에서만 병렬 수행 가능
- identity_finish : 최종 결과로 누적자 객체를 사용

## 🍎Collector 인터페이스를 구현하여 커스텀 컬렉터 만들기
