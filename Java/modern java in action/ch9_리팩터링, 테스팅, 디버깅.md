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

- 전략 패턴이란?
  - 변하는 부분을 전략으로 만든다. 클라이언트에서 어떤 전략을 사용할 것인지 선택하여 생성한다.
- 필요한 요소 : 클라이언트, 전략 인터페이스, 전략 인터페이스 구현
  [원래 전략 패턴]

```java
// 전략 인터페이스
public interface ValidationStrategy {
    boolean execute(String s);
}

//전략 인터페이스 구현
public class IsAllLowerCase implements ValidationStrategy {
    @Override
    public boolean execute(String s) {
        return s.matches("\\d+");
    }
}

public class Validator {
    private final ValidationStrategy strategy;
    public Validator(ValidationStrategy v) {
        this.strategy = v;
    }
    public boolean validate(String s) {
        return strategy.execute(s);
    }
}
```

```java
// 전략 주입하기
Validator numericValidator = new Validator(new IsNumeric());
boolean b1 = numericValidator.validate("aaaa"); //false 반환

Validator lowerCaseValidator = new Validator(new IsAllLowerCase());
boolean b2 = lowerCaseValidator.validate("bbbb"); //true;
```

[람다 표현식 사용]

- 람다 표현식은 전략 코드를 캡슐화한다.???

```java
Validator numericValidator = new Validator((String s) -> s.matches("[a-z]+"));
boolean b1 = numericValidator.validate("aaaa"); //false 반환

Validator lowerCaseValidator = new Validator((String s) -> s.matches("//d+"));
boolean b2 = lowerCaseValidator.validate("bbbb"); //true;
```

### 2. 템플릿 메서드 패턴

- 변하지 않는 부분을 상위 클래스(추상 클래스)로 만들고, 변하는 부분은 추상 메서드로 구현한다.

```java
// 추상클래스 구현 필요
abstract class OnlineBanking {
    public void processCustomer(int id) {
        Customer c = Database.getCustomerWithId(id);
        makeCustomerHappy(c);
    }
    abstract void makeCustomerHappy(Customer c);
}
```

[람다 표현식 사용]

```java
abstract class OnlineBankingLambda {
    public void processCustomer(int id, Consumer<Customer> makeCustomerHappy) {
        Customer c = Database.getCustomerWithId(id);
        makeCustomerHappy.accept(c);
    }
    abstract void makeCustomerHappy(Customer c);
}４６００보
```

```java
new OnlineBankingLambda().processCustomer(1337,
                        (Customer c) -> System.out.println("Hello, "+ c.getName()));
```

### 3. 옵저버 패턴

- 어떤 이벤트가 발생했을 때 한 객체가 다른 객체 리스트(옵저버)에 자동으로 알림을 보낸다.
- GUI 애플리케이션에서 옵저버 패턴이 자주 등장한다.
- 사용자가 버튼을 클릭하면 옵저버에 알림이 전달되고, 동작 수행
- 트위터같은 알림 시스템

```java
// 다양한 옵저버를 그룹화할 Observer 인터페이스
interface Observer {
    void notify(String tweet);
}
```

```java
// 여러 옵저버 정의
class NYTimes implements Observer {
    @Override
    public void notify(String tweet) {
        if(tweet != null && tweet.contains("money")) {
            System.out.println("Breaking news in NY!" + tweet);
        }
    }
}

class Guardian implements Observer {
    @Override
    public void notify(String tweet) {
        if(tweet != null && tweet.contains("queen")) {
            System.out.println("Yet more news from London..." + tweet);
        }
    }
}

class LeMonde implements Observer {
    @Override
    public void notify(String tweet) {
        if(tweet != null && tweet.contains("money")) {
            System.out.println("Today cheese, wine and news! " + tweet);
        }
    }
}
```

```java
interface Subject {
    // 새로운 옵저버를 등록
    void registerObserver(Observer o);
    // 트윗의 옵저버에 트윗을 알리기
    void notifyObservers(String tweet);
}
```

### 4. 의무 체인 패턴

### 5. 팩토리 패턴

## 🍎 람다 테스팅

### 보이는 람다 표현식의 동작 테스팅

### 람다를 사용하는 메서드의 동작에 집중하라

### 복잡한 람다를 개별 메서드로 분할하기

### 고차원 함수 테스팅

## 🍎 디버깅
