리스트 매핑
List.of() 는 가변 인수를 사용하지 않고, 오버로딩을 사용한다.
왜?
내부적으로 가변 인수는 인수로 배열을 생성 후 리스트로 감싼다. 따라서 할당된 배열은 가비지 컬렉션의 비용이 든다. 10개까지는 오버로딩 한 List.of() 메서드를 사용하고, 10개 이상은 가변 인수를 사용한다.

### removeIf 메서드

- true면 제거하기.

```java
// 숫자로 시작하면 삭제하기
transactions.removeIf(tx -> Character.isDigit(tx.getReferenceCode().charAt(0)));
```

### replaceAll 메서드

- 리스트의 각 요소를 새로운 요소로 바꾸기

```java
referenceCodes.replaceAll(code -> Character.toUpperCase(code.charAt(0)) + code.substring(1));
```

## Map 처리

### Map에 forEach 사용하기

- Map은 Map.Entry를 사용하여 처리할 수 있었다. 하지만 forEach를 사용하면 간단하게 구현할 수 있다.
- 첫 번째 인자는 key를, 두 번째 인자는 value이다.

```java
ageOfFriends.forEach((friend, age) -> System.out.println(friend + "is" + age + "years old"))
```

### Map 정렬하기

- Entry.comparingByValue
- Entry.comparingByKey

```java
// key로 정렬하기
favoriteMovies.entrySet()
                .stream()
                .sorted(Entry.comparingByKey())
                .forEachOrdered(System.out::println);
```

### getOrDefault 메서드

### 계산 패턴

### 삭제 패턴

### 교체 패턴

### 합침

## 개선된 ConcurrentHashMap

### 리듀스와 검색

### 계수
