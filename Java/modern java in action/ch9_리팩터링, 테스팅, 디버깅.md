- 람다 표현식으로 코드 리팩토링하기
- 람다 표현식이 객체지향 설계 패턴에 미치는 영향
- 람다 표현식 테스팅
- 람다 표현식과 스트림 API 사용 코드 디버깅

## 🍎 리팩토링

### 1. 익명 클래스를 람다 표현식으로 리팩토링하기

```java
Runnable r1 = new Runnable() {
    @Override
    public void run() {
        System.out.println("Hello");
    }
};

//람다 표현식으로 리팩토링
Runnable r2 = () -> System.out.println("Hello");
```

**주의할 점**

- 모든 익명 클래스를 람다 표현식으로 변환할 수 있는 건 아니다!!!
- 익명 클래스에서 this는 익명 클래스 자신을 가르키지만, 람다 표현식의 this는 람다를 감싸는 클래스를 가르킨다.
- 익명 클래스는 감싸고 있는 클래스의 변수를 가릴 수 있다.

```java
int a = 10;
Runnable r1 = () -> {
    int a = 2;          // 컴파일 에러 발생
    System.out.println(a);
};

Runnable r2 = new Runnable() {
    @Override
    public void run() {
        int a = 2;     // ok.
        System.out.println(a);
    }
};
```

- 람다 표현식은 오버로딩에 따른 모호함이 발생한다. 따라서 명시적 형변환을 사용해야 한다.

```java
interface Task {
    void execute();
}
public static void doSomething(Runnable r) { r.run(); }
public static void doSomething(Task a) { a.execute(); }
```

```java
// Task 구현하는 익명 클래스
doSomething(new Task() {
    @Override
    public void execute() {
        System.out.println("Danger~~");
    }
});

// 람다 표현식 사용
doSomething(() ->  System.out.println("Danger~~"));
```

doSomething 메서드를 호출할 때, Runnable을 파라미터로 가진 메서드를 호출하는 건지, Task를 파라미터로 가진 메서드를 호출하는 건지 파악할 수 없다. 따라서 다음과 같이 명시적 형변환을 사용해야 한다.

```java
doSomething((Task)() ->  System.out.println("Danger~~"));
```

### 2. 람다 표현식을 메서드 참조로 리팩토링하기

- 메서드 참조는 코드를 간결하게 하고, 코드의 의도를 명확하게 한다.

```java
inventory.sort((Apple a1, Appl a2) -> a1.getWeight().compareTo(a2.getWeight()));

//리팩토링
inventory.sort(comparing(Apple::getWeight)); // 코드가 문제 자체를 설명한다.
```

- 저수준 리듀싱 연산보다 Collectors API를 사용하면 코드의 의도가 더 명확해진다. 메서드 이름만 보아도 어떤 동작을 수행하는지 알 수 있는 것이 핵심이다!

```java
int totalCalories = menu.stream().map(Dish::getCalories).reduce(0, (a,b) -> a+b);

//Collectors API 사용하기
int totalCalories = menu.stream().collect(summingInt(Dish::getCalories));
```

### 3. 명령형 데이터 처리를 스트림으로 리팩토링하기

- 컬렉션을 반복문으로 처리하는 코드보다 스트림 API로 처리해야 한다. 스트림 API는 데이터 처리를 더 명확하게 보여준다.
- 스트림 API는 최적화(쇼트서킷, lazy Init 등), 병렬성을 제공한다.

### 코드 유연성 개선하기

- 함수형 인터페이스 적용
- 조건부 연기 실행
  - 실행 코드에서 객체의 상태를 계속 확인한 후 객체의 메서드를 호출하는 경우, 내부적으로 숨기는 것이 좋다. 객체의 상태를 확인하고, 메서드를 호출하도록 새로운 메서드를 구현하자. 이는 코드의 가독성도 높이고, 캡슐화도 강화된다.

```java
// log를 찍을 때마다 logger객체 상태를 매번 확인한다.
// logger의 상태가 isLoggable로 클라인트 코드로 노출된다.
if(logger.isLoggable(Log.FINER)) {
    logger.finer("Problem: "+ generateDiagnostic());
}

// 내부적으로 확인하는 log 메서드를 사용하도록 한다. 불필요한 if문을 제거
// 하지만 항강 로깅 메시지를 평가하게 된다.
logger.log(Level.FINER, "Problem: "+ generateDiagnostic());
```

```java
logger.log(Level.FINER, () -> "Problem: "+ generateDiagnostic());

// 내부 코드
public void log(Level level, Supplier<String> mgsSupplier) {
    if(logger.isLoggable(level)) {
        log(level, msgSupplier.get());
    }
}
```

- 실행 어라운드
  - 반복적인 코드를 람다로 변환할 수 있다. 객체의 동작은 함수형 인터페이스를 통해 입력받는다.

```java
String oneLine = processFile((BufferedReader b) -> b.readLine());

String towLines = processFile((BufferedReader b) -> b.readLine() + b.readeLine());

public static String processFile(BufferedReaderProcessor p) throws IOException {
    try( BufferedReader br = new BufferedReader( new FileReader('test.txt'))) {
        return p.process(br);
    }
}

public interface BufferedReaderProcess {
    String process(BufferedReader b) throws IOException;
}
```

## 🍎 람다로 객체지향 디자인 패턴 리팩토링하기

### 1. 전략 패턴

### 2. 템플릿 메서드 패턴

### 3. 옵저버 패턴

### 4. 의무 체인 패턴

### 5. 팩토리 패턴

## 🍎 람다 테스팅

### 보이는 람다 표현식 테스트하기

## 🍎 디버깅
