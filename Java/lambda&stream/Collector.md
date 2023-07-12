## 🍎Collector

- 스트림 전체는 reduce(), 그룹별로 나눠 사용할 때는 collect() 메서드를 사용한다.
- collect메서드의 파라미터는 Collector 인터페이스이다.
- Collector 인터페이스의 구현 클래스 Collectors 클래스가 있다.

## 🍎 Collectors의 메서드 정리

### 요약 연산 - summingInt, averagingInt, summarizingInt

- summingInt, summingDouble 등 : 값을 누적하여 더한다.
- averagingInt, averagingDoublre 등 : 평균값 계산
- summarizingInt, summarizingDouble 등 : 합계, 평균, 최댓값, 최솟값 등을 한 번에 계산

```java
import static java.util.stream.Collectors.*;

//summingInt 예시
int totalCalories = menu.stream().collect(summingInt(Dish::getCalories));

//averagingInt 예시
double avgCalories = menu.stream().collect(averagingInt(Dish::getCalories));

//summarizingInt 예시
IntSummaryStatistics menuStatistics = menu.stream().collect(summarizingInt(Dish::getCalories));
//IntSummaryStatistics{count, sum, min, average, max} 값이 담겨있다.

```

### 최대값, 최솟값 구하기 - maxBy, minBy

```java
// 점수 높은 학생 뽑기
OptionalInt topScore = studentStream.mapToInt(Student::getTotalScore).max();//Stream<Student> -> IntStream ,max 점수뽑기
Optional<Student> topStudent = stuStream1.max(Comparator.comparingInt(Student::getTotalScore)); // 점수 높은 학생 뽑기
Optional<Student> top = stuStream2.collect(Collectors.maxBy(Comparator.comparingInt(Student::getTotalScore)));//위와같음

```

### 문자열 연결 - joining

- 스트림의 모든 요소(문자열인 경우) 하나의 문자열로 연결한다.
- joining 메서드는 내부적으로 StringBuilder를 이용해서 문자열을 하나로 만듦.

```java
import static java.util.stream.Collectors.*;

String stuNames = stuStream.map(Student::getName).collect(joining());
String stuNames = stuStream.map(Student::getName).collect(joining(","));
String stuNames = stuStream.map(Student::getName).collect(joining(",", "[", "]"));
```

### 리듀싱 - reducing

- 초기값에 BinaryOperator를 계산하여 누적함. reduce랑 동작방식 같음
- reducing 세 가지
  - `reducing(초기값, Function, BinaryOperator)`
    - Function을 계산한 결과로 BinaryOperator를 실행.
  - `reducing(초기값,BinaryOperator)`
  - `reducing(BinaryOperator)`
    - reduce 메서드와 마찬가지로 초기값이 없으면 Optional로 반환한다.

```java
// 메뉴의 모든 칼로리 합계 계산하기
int totalCalories = menu.stream().collect(reducing(0, Dish::getCalories, (i,j) -> i + j));

// 칼로리 높은 요리 찾기
Optional<Dish> mostCalorieDish = menu.stream().collect(reducing((d1, d2) -> d1.getCalories > d2.getCalories() ? d1 : d2));

```

- collect와 reduce 차이
  - collect는 누적 계산 후 그 결과를 변환해도 되지만, reduce는 누적하여 계산한 결과를 변환하면 의미적으로 잘 못 사용되는 것이다.
  - 병렬 : reduce를 병렬 떄 사용하면 누적값에 대해 동시성 문제가 발생한다. 따라서 병렬 실행 때는 collect를 사용해야 한다.

### 그룹화 - groupingBy

- 종류 세 가지

  - `groupingBy(Function)`
    - 사실 `groupingBy(Function, toList())` 이다.
  - `groupingBy(Function, Collector)`
    - Function으로 그룹화한 뒤, Collector 실행
  - `groupingBy(Function, Supplier)`

- 요리 종류로 단순 그룹화하기

```java
Map<Dish.Type, List<Dish>> dishesByType = menu.stream().collect(groupingBy(Dish::getType));
```

- 칼로리로 그룹화하기

```java
Map<CaloricLevel, List<Dish>> dishesByCaloricLevel = menu.stream().collect(groupingBy( dish -> {
    if(dish.getCalories() <= 400) {
        return CaloricLevel.DIET;
    } else if (dish.getCalories() <= 700) {
        return CaloricLevel.NORMAL;
    } else {
        return CaloricLevel.FAT;
    }
}));
```

- 요리 종류로 그룹화한 후 필터링하기

```java
// 칼로리 높은 음식 그룹화하기
Map<Dish.Type, List<Dish>> dishesByType = menu.stream().collect(groupingBy(Dish::getType, filtering(dish -> dish.getCalories() > 500, toList())));
```

- 요리 종류로 그룹화한 후 매핑하기

```java
// 이름으로 그룹화하기
Map<Dish.Type, List<String>> dishNamesByType = menu.stream().collect(groupingBy(Dish::getType), mapping(Dish::getName, toList()));
```

- 다중화 그룹화
  - gourpingBy를 두 번 써서 여러 번 그룹화가 가능하다.

### 중첩 컬렉터 - collectingAndThen

- collectingAndThen(Collector, Function)
- Collector 메서드를 실행한 결과로 Function을 실행한다.

```java
// 타입으로 그룹화하고, 요리 중 칼로리 높은 것울 추출하고, 그 결과를 Optional::get을 실행하여 반환한다.
menu.stream().collect(groupingBy(Dish::getType, collectingAndThen(maxBy(comparingInt(Dish::getCalories)), Optional::get)));
```

### ture, false로 그룹화 - partitioningBy

- partitioningBy(Predicate)
- partitioningBy(Predictae, Collector)

```java
// 채식인 요리, 아닌 요리로 그룹화하기
Map<Boolean, List<Dish>> partitionedMenu = menu.stream().collect(partitioningBy(Dish::isVegetarian));

```

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

### combiner

- 병렬화 리듀싱 과정에서 사용한다.
-

```java
public BinaryOperator<List<T>> combiner() {
  return (list1, list2) -> {
    list1.addAll(list2);
    return list1;
  }
}
```

### characteristics

-

## 🍎Collector 인터페이스를 구현하여 커스텀 컬렉터 만들기
