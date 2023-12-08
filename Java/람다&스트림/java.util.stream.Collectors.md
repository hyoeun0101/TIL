## 🍎Collector

- 스트림의 최종연산 중 그룹별로 나눌 때 collect()를 사용한다. 이 collect() 메서드의 파라미터가 Collector 인터페이스이다.
- collect() 메서드와 Collectors 클래스의 정적 메소드를 조합하여 사용한다.
- Collector 인터페이스의 구현 클래스 Collectors 클래스가 있다.

## 🍎 Collectors 클래스의 메서드 정리

### 요약 연산 - averagingXXX, summingXXX, summarizingXXX
- XXX에는 int, double, long이 들어갈 수 있다.

- `averagingXXX` : 평균값 계산
- `summingXXX` : 값을 누적하여 더한다.
- `summarizingXXX` : 합계, 평균, 최댓값, 최솟값 등을 한 번에 계산

```java

//summingInt 예시
// 요리의 칼로리를 모두 더함.
int totalCalories = menu.stream().collect(summingInt(Dish::getCalories));

//averagingInt 예시
// 요리의 칼로리의 평균값 계산
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
```java
// 내부적으로 groupingBy(Function, toList()); 호출
groupingBy(Function)

// Function으로 그룹화한 뒤, 지정된 키의 값에 대해 Collector 실행
// 내부적으로 groupingBy(Funtion, HashMap::new, Collector) 호출
groupingBy(Function, Collector) 

// Funtion으로 그룹화하는데 Supplier로 함.
groupingBy(Function, Supplier, Collector)
```

```java
//요리 종류로 단순 그룹화하기
Map<Dish.Type, List<Dish>> dishesByType = menu.stream().collect(groupingBy(Dish::getType));


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



// 그룹화한 후 필터링하기 - filtering
// 칼로리 높은 음식 그룹화하기
Map<Dish.Type, List<Dish>> dishesByType = menu.stream().collect(groupingBy(Dish::getType, filtering(dish -> dish.getCalories() > 500, toList())));



// 그룹화한 후 매핑하기 - mapping
// 이름으로 그룹화하기
Map<Dish.Type, List<String>> dishNamesByType = menu.stream().collect(groupingBy(Dish::getType), mapping(Dish::getName, toList()));
```

- 다중화 그룹화
  - gourpingBy를 두 번 써서 여러 번 그룹화가 가능하다.

### groupingByConCurrent
- groupingBy에선 HashMap을 사용하여 그룹핑한다면 groupingBy는 ConcurrentMap을 사용하여 그룹화한다.


### 중첩 컬렉터 - collectingAndThen

- collectingAndThen(Collector, Function)
- 첫 번째 파리미터(Collector 메서드)를 실행한 결과로 두 번째 파라미터 Function을 실행한다.

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

### counting()
- 요소의 개수 세기
```java
// 점수로 레벨을 나눈 다음, 각 레벨에 해당하는 학생 counting
// HIGH : 3명
// MID: 1명
// LOW : 5명
Map<Student.Level,Long> stuCntByLevel = Stream.of(stuArr).collect(
    groupingBy(s->{
        if(s.getScore() >=200)return Student.Level.HIGH;
        else if(s.getScore() >= 100)return Student.Level.MID;
        else return Student.Level.LOW;
    },counting()));

```

### 이 외
- toCollection
- toConCurrentMap
- toList
- toMap
- toSet