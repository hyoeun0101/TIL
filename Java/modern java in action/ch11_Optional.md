## 🍎 값이 없는 상황을 어떻게 처리할까?

- null 확인 코드를 추가해서 예외 처리하기.
- 하지만 null 확인 코드를 일일히 추가하게 되면 코드의 가독성이 너무 떨어지며 까먹고 null 확인 코드를 뺴먹으면 문제가 발생할 수도 있다.
- 다른 언어는 null 대신 무얼 사용하나?
  - 그루비는 안전 내비게이션 연산자(?.)를 사용한다.
  - 자바7에서도 비슷한 제안이 있었지만, 이는 단순히 if문을 추가해서 null을 처리하는 로직을 뒤로 미루는 것밖에 되지 않았기에 채택되지 않았다.
  - 하스켈은 optional value을 저장할 수 있는 Maybe라는 형식을 제공한다.
  - 스칼라는 Optional[T]라는 구조를 제공한다.

## 🍎 Optional 클래스

- 자바8은 하스켈과 스칼라의 영향을 받아 java.util.Optional<T>라는 새로운 클래스를 제공한다.

## 🍎 Optional 적용 패턴

사용법은 [Optional](https://github.com/hyoeun0101/TIL/blob/main/Java/Optional.md) 참조

[예제코드]

```java
public class Person {
    // 사람이 차를 소유할 수도 안 할수도 있다.
    private Optional<Car> car;

    public Optional<Car> getCar() {return car;}
}

public class Car {
    // 차는 보험에 가입되어 있을 수도 없을 수도 있다.
    private Optional<Insurance> insurance;

    public Optional<Insurance> getInsurance() {return insurance;}
}

public class Insurance {
    // 보험회사는 반드시 이름이 있다.
    private String name;
    public String getName() {return name;}
}
```

### Optional의 map

```java
Optional<Insurance> optInsurance = Optional.ofNullable(insurance);
Optional<String> name = optInsurance.map(Insurance::getName);//optInsurance이 null이면 아무일도 일어나지 않는다.
```

- map: Optional의 내용물을 바꾼다.

### Optional의 flatMap

```java
Optional<Person> optPerson = Optional.of(person);
// 컴파일 오류
Optional<String> name = optPerson.map(Person::getCar) //Optional<Optional<Car>>
                                 .map(Car::getInsurance)
                                 .map(Insurance::getName);

Optinal<String> name = optPerson.flatMap(Person::getCar) //Optional<Car>
                                .flatMap(Car::getInsurance) //Optional<Insuracne>
                                .map(Insurance::getName); //Optional<String>
```

- flatMap : 평준화 과정, 평준화는 두 Optional을 합치면서 둘 중 하나라도 null이면 빈 Optional을 생성한다.

### 도메인 모델에 Optional 사용

- Optional은 직렬화를 지원하지 않는다. 따라서 도메인 모델에 Optional을 사용할 때 문제가 될 수 있다. 직렬화 모델이 필요하면 필드에 Optional 객체를 선언하지 말고, getter가 Optional 객체를 반환하도록 하자.

```java
public class Person {
    private Car car;
    public Optional<Car> getCarAsOptional() {
        return Optional.ofNullable(car);
    }
}
```

### Optional의 Stream

```java
public Set<String> getCarInsuranceNames(List<Person> persons) {
    return persons.stream() //Stream<Person>
                  .map(Person::getCar)  //1. Stream<Optional<Car>>
                  .map(optCar -> optCar.flatMap(Car::getInsurane)) // 2. Stream<Optional<Insurance>>
                  .map(optInsurance -> optInsurance.map(Insurance::getName)) //3. Stream<Optional<String>>
                  .flatMap(Optional::stream) // 4. Stream<Stream<String>>이지만 flatMap을 써서 Stream<String>
                  .collect(toSet());
}
```

- 4번째 과정에서 Optional<String>이 null이면 다음과 같이 filter, map을 사용하여 처리할 수 있다.

```java
// 세번째 결과물
Stream<Optional<String>> nameStream = ...;
Set<String> result = nameStream.filter(Optional::isPresent)
                               .map(Optional::get)
                               .collect(toSet());
```

- 스트림을 사용할 때, Optional.of로 생성한 Optional 객체의 경우, flatMap, map 같은 메서드를 사용하면 NPE가 발생한다.
- Optional.ofNullable로 생성한 Optional 객체의 경우, 메서드가 동작하지 않으며 Optional.empty가 반환된다.

### 두 Optional 합치기

```java
// 가장 싼 보험료를 제공하는 보험회사 찾아라
public Insurance findCheapestInsurance(Person person, Car car) {
    // 1.다양한 보험회사가 제공하는 서비스 조회
    // 2. 모든 결과 데이터 비교
    Insurance cheapestCompany = new Insurance();
    return cheapestCompany;
}


//person, car 의 시그니처만으로 둘다 아무 값을 반환하지 않음을 명시적으로 보여준다.
// 하지만 null 확인 코드와 다를게 없다. 아래의 메서드로 리펙토링하기
public Optional<Insurance> nullSafeFindCheapestInsurance(Optional<Person> person, Optional<Car> car) {
    if(person.isPresent() && car.isPresent()) {
        return Optional.of(findCheapestInsurance(person.get(), car.get()));
    } else {
        return Optional.empty();
    }
}
```

```java
public Optional<Insurance> nullSafeFindCheapestInsurance(Optional<Person> person, Optional<Car> car) {
    //person이 비어있다면, 람다식은 실행되지 않고, 그대로 빈 Optional을 반환.
    //car이 비어있으면 빈 Optional을 반환, 결국 이 메서드는 빈 Optional을 반환.

    //person, car 값이 모두 있으면 findCheapestInsurance 호출
    return person.flatMap(p -> car.map(c -> findCheapestInsurance(p, c)));
}
```

### Optional의 filter

- Optional이 비어있으면 filter는 아무 동작도 하지 않는다. 값이 있으면 filter의 Predicate 람다식을 실행하고 true면 Optional 객체는 아무 변화가 없고, false이면 Optional은 빈 상태가 된다.

```java
// 원래 코드. null 체크와 특정값과 일치하는지 if문으로 체크
Insurance i = ...;
if(i != null && "CambridgeInsurance".equals(i.getName())) {
    System.out.println("ok");
}

//Optional 사용하기
Optional<Insurnace> optIns = ...;
optIns.filter(ins -> "CambridgeInsurance".equals(ins.getName()))
      .ifPresent(x -> System.out.println("ok"));
```

```java
public String getCarInsuranceName(Optional<Person> person, Optional<Car> car, int minAge) {
    return person.filter(p -> p.getAge() >= minAge)
                 .flatMap(Person::getCar)
                 .flatMap(Car::getInsurance)
                 .map(Insurance::getName)
                 .orElse("Unknown");
}
```

## 🍎Optional을 사용한 실용 예제

### 1. 잠재적으로 null이 될 수 있는 대상은 Optional로 감싸자.

- Map에 key와 대응되는 값이 없을 경우를 대비하여 다음과 같이 Optional을 사용할 수 있다.

```java
Optional<Object> value = Optional.ofNullable(map.get("key"));
```

### 2. Optional 예외처리 응용하기

- 자바 API에서 Integer.parseInt 같이 어떤 이유에서 값을 제공할 수 없을 때, null이 아닌 예외를 발생시키는 경우가 있다.
- 다음과 같이 예외가 발생하면 빈 Optional을 반환하도록 할 수 있다.

```java
public static Optional<Integer> stringToInt(String s) {
    try {
        return Optional.of(Integer.parseInt(s));
    } catch(NumberFormatException e) {
        return Optional.empty();
    }
}
```

### 3. 기본형 Optional을 사용하지 마라.

- Optional은 하나의 요소밖에 감싸지 못한다. 따라서 기본형 특화 Optional로 성능을 개선할 수 없다.
- 오히려 기본형 특화 Optional은 map과 같은 유용한 메서드를 사용할 수 없기 때문에 사용을 권장하지 않는다.

### 4. 마지막으로 Optional 응용해보기.

[원래코드] 프로퍼티에서 문자형 숫자를 읽고, 정수형으로 변환 후 반환하기

```java
public int readDuration(Properties props, String name) {
    String value = props.getProperty(name);
    if(value != null) {
        try {
            int i = Integer.parseInt(value);
            if(i > 0) return i;
        } catch(NumberFormatException e) {}
    }

    return 0;
}
```

[Optional 사용하여 개선하기]

```java
public int readDuration(Properties props, String name) {
    // 있을수도 있고, 없을 수도 있고
    return Optional.ofNullable(props.getProperty(name));
                   .flatMap(OptionalUtility::stringToInt)
                   .filter(i -> i > 0)
                   .orElse(0);
}

public Optional<Integer> stringToInt(String value) {
    try {
        return Optional.of(Integer.parseInt(value))
    } catch(NumberFormatException e) {
        return Optional.empty();
    }
}
```
