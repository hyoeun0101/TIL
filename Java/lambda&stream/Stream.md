스트림 : 어떤 데이터 형태이던 상관없이 같은 방식으로 다룰 수 있도록 함.

1. 스트림 생성하기
2. 스트림 중간연산 0~n번
3. 스트림 최종연산 1번

### 스트림의 특징

- 데이터 소스를 변경하지 않음. only Read
- 일회용이다. 닫히면 사용 못함.
- 작업을 내부 반복으로 처리함. forEach 내부에서 반복문 돌림.
- 지연된 연산. 최종연산을 해야 중간연산을 거쳐 수행되는 것임.
- 병렬 처리가 쉽다. parallel()

# 스트림 - 생성

Collection 인터페이스의 `stream` 메서드 - List, Set에 사용 ex) list.stream();  
기본형 스트림 타입 IntStream, LongStream, DoubleStream 제공

### 1. 배열 스트림

```java
Stream<String> strStream = Stream.of("a","b","c");
Stream<String> strStream = Stream.of( new String[]{"a","b","c"});
Stream<String> strStream = Arrays.stream(new String[]{"a","b","c"});
Stream<String> strStream = Arrays.stream(new String[]{"a","b","c"},0,3);
```

### 2. 임의의 수 만들기

아래는 무한 스트림이라서 limit()으로 제한이 필요.

```java
IntStream intStream = new Random().ints();//무한
intStream.limit(5).forEach(System.out::println);
```

### 3. 특정 범위의 정수

```java
IntStream intStream = IntStream.range(1,5);//1,2,3,4
IntStream intStream2 = IntStream.rangeClosed(1,5);//1,2,3,4,5
```

### 4. 람다식 iterate(), generate()

`람다식`을 매개변수로 받아서 `무한 스트림` 생성

- iterate(초기값, 람다식)
- generate(람다식)  
  기본형 스트림 타입 다룰 수 없음!!

```java
Stream<Integer> evenStream = Stream.iterate(0,i->i+2);//0,2,4,6,8...
Stream<Integer> randomStream = Stream.generate(Math::random);
Stream<Integer> oneStream = Stream.generate(()->1);//1,1,1...
```

기본형 스트림으로 다루기

```java
IntStream evenStream = Stream.iterate(i->i+2).mapToInt(Integer::valueOf);
Stream<Integer> stream = evenStream.boxed();//IntStream -> Stream<Integer>
```

### 5. 빈 스트림

요소가 없는 스트림일 때, null보단 빈 스트림으로 처리하기

```java
Stream emptyStream = Stream.empty();
```

# 스트림- 중간연산

```java
//skip(),limit()
IntStream intStream = IntStream.rangeClosed(1,10);//1~10
intStream.skip(3).limit(5).forEach(System.out::print);//45678
//distinct()
IntStream intStream = IntStream.of(1,2,2,3,3,3,4,5,5,6);
intStream.distinct().forEach(System.out::print);//123456
//filter(Predicate<> p)
IntStream intStream = Intstream.rangeClosed(1,10);//1~10
intStream.filer(i->i%2==0).forEach(System.out::print);//246810
```

### 1. sorted() -정렬하기

```java
Stream<String> strStream = Stream.of("dd","aaa","CC","cc","b");
```

🌞기본 정렬-오름차순 //CCaaabccdd

- strStream.sorted()
- strStream.sorted(Comparator.naturalOrder())
- strStream.sorted((s1,s2)->s1.compareTo(s2))
- strStream.sorted(String::compareTo)  
  🌞역정렬-내림차순 //ddccbaaaCC
- strStream.sorted(Comparator.reverseOrder())
- strStream.sorted(Comparator.<String>naturalOrder().reversed())  
  🌞기본정렬+ 대소문자 구별안함//aaabCCccdd
- strStream.sorted(String.CASE_INSENSITIVE_ORDER)  
  🌞역정렬 + 대소문자 구별안함//ddCCccbaaa (들어있는 순서대로 정렬)
- strStream.sorted(String.CASE_INSENSITIVE_ORDER.reversed())  
  🌞길이 순 정렬//bddCCccaaa
- strStream.sorted(Comparator.comparing(String::length))  
  🌞역//aaaddCCccb
- strStream.sorted(Comparator.comparing(String::length).reversed())

**sorted()의 매개변수인 Comparator의 메서드**

```java
import java.util.Comparator;
import java.util.stream.Stream;

public class Ex{
    public static void main(String[] args) {
        Stream<Student> studentStream = Stream.of(
            new Student("가", 3, 300),
            new Student("나", 1, 100),
            new Student("다", 2, 500),
            new Student("라", 2, 200),
            new Student("마", 3, 800)
        );

    studentStream.sorted(Comparator.comparing(Student::getBan)
                        .thenComparing(Comparator.naturalOrder()))
                        .forEach(System.out::println);
    }
}

class Student implements Comparable<Student>{
    String name;
    int ban;
    int totalScore;
    Student(String name, int ban, int totalScore){
        this.name = name;
        this.ban = ban;
        this.totalScore = totalScore;
    }

    @Override
    public int compareTo(Student s) {
        return s.totalScore - totalScore;//내림차순
    }

    String getName(){
        return name;
    }
    int getBan(){
        return ban;
    }
    int getTotalScore(){
        return totalScore;
    }
    @Override
    public String toString(){
        return String.format("%s, %d, %d",name,ban,totalScore);
    }

}
```

반별로 정렬, 총점으로 내림차순 정렬.  
Comparable을 구현하여 Student클래스의 기본정렬이 총점별 내림차순이 되도록 함.

### 2. map() - Stream으로 변환

`원하는 필드 뽑아내기`, `다른 타입의 스트림으로 변환하기`
요소를 Stream으로 반환

```java
import java.io.File;
import java.util.stream.Stream;

public class Ex{
    public static void main(String[] args) {
        File[] fileArr = {new File("Ex1.java"), new File("Ex1.bak"),
                        new File("Ex2.java"), new File("Ex1"), new File("Ex1.txt")
                        };

        Stream<File> fileStream = Stream.of(fileArr);

        Stream<String> fileNameStream = fileStream.map(File::getName);//Stream<File> -> Stream<String>
        fileNameStream.forEach(System.out::println);

        fileStream = Stream.of(fileArr);

        fileStream.map(File::getName)//Stream<File> -> Stream<String>
                .filter(s-> s.indexOf('.')!=-1)
                .map(s->s.substring(s.indexOf('.')+1))
                .map(String::toUpperCase)//Stream<String> -> Stream<String>
                .distinct()
                .forEach(System.out::print);
                //JAVABAKTXT
    }
}
```

### 3. peek()

연산과 연산 사이에 확인하기

```java
.peek((i)->System.out.println(i))
```

### 4. flatMap()

모든 원소를 단일 스트림으로 반환  
ex) Stream<T[ ]> -> Stream<T> 할때, flatMap사용

**🌞map()과 flatmap()의 차이🌞**

```java
Stream<String[]> strArrStream  = Stream.of(
    new String[]{"abc", "def", "ghi"},
    new String[]{"ABC","GHI","JKLMN"}
);
```

```java
Stream<Stream<String>> strStrStrm = strArrStream.map(Arrays::stream);
Stream<String> strStrm = strArrStream.flatMap(Arrays::stream);
```

map은 Stream<span color="red"><String[]></span> -> Stream<span color="red"><Stream&lt;String &gt;></span>  
 flatMap은 Stream<span color="red"><String[]></span> -> Stream<span color="red">&lt;String&gt;</span>

```java
String[] lineArr = {
    "i am eun",
    "i am happy"
};

Stream<String> lineStream = Arrays.stream(lineArr);
lineStream.flatMap(line->Stream)
```

# 스트림-최종연산

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

### 1. reduce(identity, accumulator)

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

### 2.collect(Collector collector) - 그룹별로 나눠서 사용하기

스트림 전체는 reduce(), 그룹별로 나눠 사용할 때는 collect()를 사용한다.  
`Collector`는 수집에 필요한 메서드를 정의해놓은 `인터페이스`이다. 다음과 같은 추상 메서드가 있다.

```
supplier()//누적할 곳
accumulator() //누적 방법
combiner() //결합방법(병렬)
finisher() //최종변환
```

`Collectors`는 `Collector의 구현 클래스`이다. collect()에서 이미 구현된 Collectors의 메서드를 사용한다.

### 3. 변환하기 - collect()

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
  - toArray()

```java
Student[] stuNames = stuStream.toArray(Student[]::new);//(i)->new Student[i]
Object[] stuNames = stuStream.toArray();
```

### 4. 통계내기

```java
long count = stuStream.count();//스트림 전체 count
long count = stuStream.collect(Collectors.counting());//그룹별 count 가능

long totalScore = stuStream.mapToInt(Student::getTotalScore).sum();
long totalScore = stuStream.collect(summingInt(Student::getTotalScore));//그룹별 sum 가능

OptionalInt topScore = studentStream.mapToInt(Student::getTotalScore).max();//Stream<Student> -> IntStream ,max 점수뽑기
Optional<Student> topStudent = stuStream1.max(Comparator.comparingInt(Student::getTotalScore)); // 점수 높은 학생 뽑기
Optional<Student> top = stuStream2.collect(Collectors.maxBy(Comparator.comparingInt(Student::getTotalScore)));//위와같음
```

### 5. 리듀싱 - reducing()

IntStream에는 매개변수 3개 collect밖에 없어서 boxed()로 Stream<Integer>로 변환해야한다.

```java
IntStream intStream = new Random.ints(1,46).distinct().limit(6);
//max값 뽑기
OptionalInt max = intStream.reduce(Integer::max);
Optional<Integer> max = intStream.boxed().collect(Collectors.reducing(Integer::max));

//점수 높은 학생 뽑기
Optional<Student> topStudent = stuStream.map(Student::getTotalScore).reduce(Integer::max);
Optoional<Student> topStudent = stuStream.collect(Collectors.reducing(Integer::max));

//totalScore 모두 더하기
int grandTotal = stuStream.map(Student::getTotalScore).reduce(0,Integer::sum);
int grandTotal = stuStream.collect(Collectors.reducing(0, Student::getTotalScore,Integer::sum));
```

### 5. 스트림을 문자열로 결합 - joining()

: 스트림의 모든 요소(문자열인 경우)를 하나의 문자열로 연결

```java
String stuNames = stuStream.map(Student::getName).collect(Collectors.joining());
String stuNames = stuStream.map(Student::getName).collect(Collectors.joining(","));
String stuNames = stuStream.map(Student::getName).collect(Collectors.joining(",", "[", "]"));

```

### collectingAndThen()

# 스트림 - 그룹화

### 1. partitioningBy() - true, false로 분할함.

```java
Collector partitioningBy(Predicate predicate);//분할조건
Collector partitioningBy(Predicate predicate, Collector downStream);//분할조건, 수집조건
[partitioningBy](./PartitioningByExam.java)
```

### 2. groupingBy() - n분할

```java
Collector groupingBy(Function classifier)
Collector groupingBy(Function classifier, Collector downStream);
Collector groupingBy(Function classifier, Supplier mapFactory, Collector downStream);
```

groupingBy()로 그룹화를 하면 기본적으로 List<T>에 담는다.

```java
Map<Integer, List<Student>> stuByBan = stuStream.collect(groupingBy(Student::getBan));//toList()가 생략됨.
```
