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
__정렬하기 sorted()__
```java
Stream<String> strStream = Stream.of("dd","aaa","CC","cc","b");
```
`기본 정렬-오름차순` //CCaaabccdd  
- strStream.sorted()
- strStream.sorted(Comparator.naturalOrder())
- strStream.sorted((s1,s2)->s1.compareTo(s2))
- strStream.sorted(String::compareTo)    
`역정렬-내림차순`    //ddccbaaaCC
- strStream.sorted(Comparator.reverseOrder())
- strStream.sorted(Comparator.<String>naturalOrder().reversed())    
`기본정렬+ 대소문자 구별안함`//aaabCCccdd
- strStream.sorted(String.CASE_INSENSITIVE_ORDER)    
`역정렬 + 대소문자 구별안함`//ddCCccbaaa (들어있는 순서대로 정렬)
- strStream.sorted(String.CASE_INSENSITIVE_ORDER.reversed())     
`길이 순 정렬`//bddCCccaaa
- strStream.sorted(Comparator.comparing(String::length))
`역`//aaaddCCccb
- strStream.sorted(Comparator.comparing(String::length).reversed())

__Comparator의 메서드__
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
Comparalbe을 구현하여 Student클래스의 기본정렬이 총점별 내림차순이 되도록 함.   

__map()__    
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

__peek()__    
연산과 연산 사이에 확인하기   
__flatMap()__    
모든 원소를 단일 스트림으로 반환    
ex) Stream<T[ ]> -> Stream<T>  할때, flatMap사용  

__map()과 flatmap()의 차이
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
map은 Stream<span color="red"><String[]></span> -> Stream<span color="red"><Stream&lt;String	&gt;></span>    
 flatMap은 Stream<span color="red"><String[]></span> -> Stream<span color="red">&lt;String&gt;</span>    
```java
String[] lineArr = {
    "i am eun",
    "i am happy"
};

Stream<String> lineStream = Arrays.stream(lineArr);
lineStream.flatMap(line->Stream)
```

## 스트림 최종연산
