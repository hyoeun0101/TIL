# 챕터 1. 자바 8,9,10,11 : 무슨 일이 일어나고 있는가?

- 자바가 거듭 변화하는 이유
- 컴퓨터 환경의 변화
- 자바에 부여되는 시대적 변화 요구
- 자바 8과 자바 9의 새로운 핵심 기능 소개

---

## 🍎 1.1 역사의 흐름

가장 큰 변화를 가져온 자바 8

- 자바 1.0 : 스레드, 락, 메모리 모델
- 자바 5 : 스레드 풀, 병렬 실행 컬렉션(concurrent collection)
- 자바 7 : 포크, 조인 프레임워크
- 자바 8 : 스트림 API, 메서드에 코드를 전달하는 기법(람다, 메서드 참조), 인터페이스의 디폴트 메서드
  - 코드를 전달하는 간결 기법(람다와 메서드 참조)
  - 메서드에 코드를 전달하는 기법으로 동작 파라미터화를 구현. 익명 클래스를 이용해서 동작 파라미터화를 구현한다.
  - 함수형 프로그래밍
- 디폴트 메서드
- 자바 9 : 리액티브 프로그래밍(병렬 실행 기법, RxJava)

## 🍎 1.2 왜 아직도 자바는 변화하는가?

### 1.2.1 프로그래밍 언어 생태계에서 자바의 위치

- 객체지향 언어로 각광을 받았다.
- 하지만 언어는 변화하는 환경에 적응하며 변해야 살아남을 수 있다.

### 1.2.2 스트림 처리

- 스트림 API의 핵심은 기존엔 한 번에 한 항목을 처리했지만, 자바8에서는 작업을 고수준으로 추상화해서 일련의 스트림으로 만들어 처리할 수 있다는 것이다.
- 또한 스트림 파이프라인을 이용해서 입력 부분을 여러 CPU 코어에 할당할 수 있다. 그럼 복잡한 작업없이 공짜로 `병렬성`을 얻을 수 있다.

### 1.2.3 동작 파라미터화로 메서드에 코드 전달하기

- 동작 파라미터화 : 코드를 인수로 전달해주는 것
- 즉, 람다를 의미한다.

### 1.2.4 병렬성과 공유 가변 데이터

- 멀티 스레딩에선 공유 가변 데이터에 접근해선 안된다.
- 기존에는 synchronized를 이용해서 동시성을 제어했지만, 일반적으로 syncronized는 시스템 성능에 악영향을 미친다.
- 자바8의 스트림을 이용하면 기존의 자바 스레드 API보다 쉽게 병렬성을 활용할 수 있다.

- 함수형 프로그래밍에서는 우리가 하려는 작업이 최우선시 되며, 그 작업을 어떻게 수행하는지는 별개의 문제로 취급한다.

## 🍎 1.3 자바 함수(메서드 참조와 람다)

### 1.3.1 메소드와 람다를 일급 시민으로

- 메서드 참조: 메서드를 값으로 취급

```java
File[] hiddenFiles = new File(".").listFiles(new FileFilter() {
    public boolean accept(File file) {
        return file.isHidden();
    }
})
```

```java
File[] hiddenFiles = new File(".").listFiles(File::isHidden);
```

- File 클래스에는 이미 isHidden이라는 메서드가 있는데 굳이 FileFilter로 감싼 다음에 FileFilter를 인스터화해야 하나?
  - 이 메서드를 값으로 사용하라. 함수를 값으로 넘겨주는 것!!!!!!

### 1.3.3 메서드 전달에서 람다로

- 메서드 전달은 다음과 같이 작성할 수 있다.

```java
filterApples(inventory, Apple::isGreenApple);
filterApples(inventory, Apple::isHeavyApple);
```

- 메서드 전달을 람다로 변경하자.

```java
filterApples(inventory, (Apple a) -> GREEN.equals(a.getColor()));
filterApples(inventory, (Apple a) -> a.getWeight() > 50);

//위에 두 줄을 다음과 같이 작성할 수 있다.
filterApples(inventory, (Apple a) -> GREEN.equals(a.getColor()) || a.getWeight() > 50);

```

- 아마도 자바는 filter 기능을 일반적인 라이브러리 메서드로 만드는 방향으로 갔을지도 모른다. 다음과 같이 말이다.

```java
//이렇게
filter(list, (Apple a -> a.getWeight() > 150));
```

- 하지만 병렬성 때문에 이렇게 설계하진 않았다. 대신 스트림 API를 제공한다!

## 🍎 1.4 스트림

- 거의 모든 자바 애플리케이션은 컬렉션을 만들고 활용한다. 다음 코드를 스트림을 활용하여 변경해보자.

```java
// 기본 코드
Map<Currency, List<Transaction>> txByCurrenciesMap = new HashMap<>(); //그룹화된 트랜잭션 더할 Map생성
for(Transaction tx : transactions) {
  if(tx.getPrice() > 1000) { //1000 보다 크면
    Currency currency = tx.getCurrency();
    List<Transaction> txForCurrency = txByCurrenciesMap.get(currency);

    //해당 currency가 Map에 없으면
    if(txForCurrency == null) {
      //List 생성 후 Map에 추가
      txForCurrency = new ArrayList<>();
      txByCurrenciesMap.put(currency, txForCurrency);
    }
    //List에 tx 넣기
    txForCurrency.add(tx);
  }
}
```

```java
//스트림으로 작성
import static java.util.stream.Collectors.groupingBy;

Map<Currency, List<Transaction>> txByCurrenciesMap = transcations.stream()
                                                    .filter((Transaction t) -> t.getPrice() > 1000)
                                                    .collect(groupingBy(Transaction::getCurrency));
```

- 스트림 API를 이용하면 내부 반복을 통해 거대한 데이터를 처리하기 쉽다.
- 컬렉션은 데이터를 어떻게 저장하고 접근할지 중점을 두는 반면, 스트림은 데이터에 어떤 계산을 할 것인지에 중점을 둔다.

### 1.4.1 멀티스레딩은 어렵다

- 이전 자바 버전에서 멀티스레딩을 구현하기 쉽지 않았다. 멀티스레드 환경은 동시성 문제때문에 스레드를 잘 제어해야 하기 때문이다.
- 자바 8은 스트림 API로 `컬렉션 처리 시 복잡한 코드`, `멀티코어 활용 어려움` 문제를 해결했다.
- 스트림은 스트림 내의 요소를 쉽게 병렬 처리할 수 있는 환경을 제공한다.(ex)`paralleStream()`)
  - 큰 스트림을 병렬로 처리할 수 있게 작은 스트림으로 분할한다.
  - 가변 공유 객체를 통해 병렬성 사용.(?)
  - 함수형 프로그래밍에서 함수형이란 `함수를 값으로 사용한다`, `프로그램이 실행되는 동안 컴포넌트 간에 상호작용이 일어나지 않는다`를 의미한다.

## 🍎 1.5 디폴트 메서드와 자바 모듈

- 기존의 구현 클래스를 고치지 않고 인터페이스 변경 가능.
- 주의할 것 : 여러 인터페이스 구현시, 여러 인터페이스의 디폴트 메서드 충돌이 발생.
- 하지만 디폴트 메서드는 프로그램이 쉽게 변화할 수 있는 환경을 제공하는 것이므로 프로그래머가 직접 디폴트 메서드를 구현하는 상황은 흔치 않다.

## 🍎 1.6 함수형 프로그래밍에서 가져온 다른 유용한 아이디어

- 자바에 포함된 함수형 프로그래밍의 핵심 : 메서드 호출과 람다, 병렬 실행

### 1.7 마치며

- 자바 8의 변화
  - 메서드 참조와 람다: 함수는 일급값이다. 메서드를 함수형값으로 넘겨주고, 익명 함수(람다)를 구현한다.
  - 스트림 : 스트림과 컬렉션을 적절하게 활용하여 가독성 좋은 코드를 구현한다.
  - 멀티 스레딩 : 멀티코어 프로세서를 온전히 활용한다.
  - 디폴트 메서드 : 기존 인터페이스의 구현을 변경하지 않고, 인터페이스를 변경할 수 있다.
  - Optional : null처리 방법을 제공한다.
