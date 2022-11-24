- allMatch: 모든 요소가 조건 만족하면 true
- anyMatch: 한 요소라도 조건 만족하면 true
- nonMatch: 모든 요소가 조건 만족하지 않으면 true
- finddFirst: 첫 번째 요소를 반환, 순차 스트림에 사용
- findAny: 아무거나 반환, 병렬 스트림에 사용, 앞에 .parallel() 사용
- forEachOrdered() : 병렬 처리 때 순서 유지, 앞에 .parallel()로 병렬처리함.

```java
Optiional<Student> result = stuStream.filter(s->s.getTotalScore()<=100).findFirst();//100점 이하인 학생이 있으면 반환, 없으면 null
Optional<Student> result = paralleStream.filter(s->s.getTotalScore()<=100).findAny();
```

### reduce(identity, accumulator)

: 스트림의 요소를 하나씩 꺼내면서 누적연산

- identity: 초기값
- accumulator: 이전 연산결과와 스트림의 요소에 수행할 연산

```java
int sum = intStream.reduce(0,(a,b)-> a+b);

//이와 같음
int a = identity;//초기값
for(int b:stream){
    a = a+b;
}
```

```java
int max = intStream.reduce(Integer.MIN_VALUE, (a,b)->a > b ? a: b);
int min = intStream.reduce(Integer.MAX_VALUE, (a,b)->a<b?a:b);
int count = intStream.reduce(0, (a,b)->a+1);//intStream의 요소 개수 반환
```

### collect(Collector)

: 스트림 전체는 reduce(), 그룹별로 나눠 사용할 때는 collect()

`Collector`는 수집에 필요한 메서드를 정의해놓은 `인터페이스`

```
supplier()//누적할 곳
accumulator() //누적 방법
combiner() //결합방법(병렬)
finisher() //최종변환
```

`Collectors`는 Collector의 구현 클래스

- 변환
- 스트림->컬렉션
  - toList(), toSet(), toMap(), toCollection()

```java
//Stream<Student> -> Stream<String> -> List<String>
List<String> names = stuStream.map(Student::getName).collect(Collectors.toList());
//Stream<Student> -> ArrayList<Stream>
ArrayList<Stream> list = names.stream().collect(Collectors.toCollection(ArrayList::new));
//Stream<Person> -> Map<String,Person>
Map<String,Persion> map = personStream.collect(Collectors.toMap(p->p.getRegId, p->p));
```

- 스트림 -> 배열

```java
Student[] stuNames = stuStream.toArray(Student[]::new);//(i)->new Student[i]
Object[] stuNames = stuStream.toArray();
```

- 통계

```java
long count = stuStream.count();//스트림 전체 count
long count = stuStream.collect(Collectors.counting());//그룹별 count 가능

long totalScore = stuStream.mapToInt(Student::getTotalScore).sum();
long totalScore = stuStream.collect(summingInt(Student::getTotalScore));//그룹별 sum 가능

OptionalInt topScore = studentStream.mapToInt(Student::getTotalScore).max();//Stream<Student> -> IntStream ,max 점수뽑기
Optional<Student> topStudent = stuStream1.max(Comparator.comparingInt(Student::getTotalScore)); // 점수 높은 학생 뽑기
Optional<Student> top = stuStream2.collect(Collectors.maxBy(Comparator.comparingInt(Student::getTotalScore)));//위와같음
```
