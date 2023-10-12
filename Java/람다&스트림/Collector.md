## 🍎Collector

- 스트림 전체는 reduce(), 그룹별로 나눠 사용할 때는 collect() 메서드를 사용한다.
- collect메서드의 파라미터는 Collector 인터페이스이다.
- Collector 인터페이스의 구현 클래스 Collectors 클래스가 있다.

## 🍎 Collectors 클래스의 메서드 정리

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


### 그룹화 - groupingBy

- 종류 세 가지

  - `groupingBy(Function)`
    - 사실 `groupingBy(Function, toList())` 이다.
  - `groupingBy(Function, Collector)`
    - Function으로 그룹화한 뒤, Collector 실행
  - `groupingBy(Function, Supplier)`


```java
//요리 종류로 단순 그룹화하기
Map<Dish.Type, List<Dish>> dishesByType = menu.stream().collect(groupingBy(Dish::getType));
```


```java
//칼로리로 그룹화하기
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

```java
// 그룹화한 후 필터링하기 - filtering
// 칼로리 높은 음식 그룹화하기
Map<Dish.Type, List<Dish>> dishesByType = menu.stream().collect(groupingBy(Dish::getType, filtering(dish -> dish.getCalories() > 500, toList())));
```

```java
// 그룹화한 후 매핑하기 - mapping
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

### ture, false로 분할 - partitioningBy

- partitioningBy(Predicate)
- partitioningBy(Predictae, Collector)

```java
// 채식인 요리, 아닌 요리로 그룹화하기
Map<Boolean, List<Dish>> partitionedMenu = menu.stream().collect(partitioningBy(Dish::isVegetarian));

```

### 이 외
- toList(), toMap(), toSet()

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
