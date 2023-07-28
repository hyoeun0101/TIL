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

## 🍎 Map 처리

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

### 추가로 HashMap 성능 알아보기

- 자바8에서는 HashMap의 내부 구조를 바꿔 성능을 개선했다.
- 기존 : 해시코드를 사용함. 많은 키가 같은 해시코드를 반환하면 버킷을 LinkedList로 반환해야 했다. 따라서 O(n)이었다.
- 개선 : 버킷이 너무 커질 경우 이를 O(log(n))의 시간이 걸리는 정렬된 트리를 사용한다.

### getOrDefault 메서드

- 키가 존재하지 않으면 Default값 반환
- 원래 Map은 키가 존재하지 않으면 널을 반환한다.

```java
favoriteMovies.getOrDefault("키값", "디폴트값");
```

### 계산 패턴

- `computeIfAbsent` : 키 값이 없으면, 키를 맵에 추가하고 키가 있으면 값을 반환한다.
  - 정보를 캐시할 때 활용할 수 있다.

```java

friendsToMovies.computeIfAbsent("Rapheal",
                                    name -> new ArrayList<>()) // 키가 존재하지 않으면 실행
                                    .add("Star Wars");
```

- `computeIfPresent`: 키가 존재하면, 새 값을 계산하고 맵에 추가한다.
- `compute` : 키로 새 값을 계산하고 맵에 저장한다.

### 삭제 패턴 - remove

- `remove` : 키와 값이랑 매칭하는 값 삭제
- `removeIf` : 조건이 true면 삭제

```java
map.remove("키", "값");
```

### 교체 패턴 - replace

- `replaceAll` : BiFuntion을 적용한 결과로 각 항목을 교체한다.
- `replace` : 키가 존재하면 해당 값으로 값을 바꾼다.

```java
Map<String,String> map = new HashMap<>();
map.put("A","aaa");
map.put("B","bbb");
map.replaceAll((key,val) -> val.toUpperCase());
map.replace("A", "abc");
```

### 합침 - putAll, put

- `putAll` : 두 개의 맵을 합치는데 사용한다.
  - 중복된 키가 있을 땐 forEach와 merge를 사용해서 처리한다. 중복된 키를 어떻게 합칠지는 BiFunction로 작성한다.

```java
Map<String,String> family = Map.ofEntries(entry("Teo", "Star Wars"), entry("Cristina", "James Bond"));

Map<String,String> friends = Map.ofEntries(entry("Raphael", "Star Wars"));

Map<String,String> everyone = new HashMap<>(family);
everyone.putAll(friends); // 중복된 키가 없어서 잘 동작한다.
```

```java
Map<String,String> family = Map.ofEntries(entry("Teo", "Star Wars"), entry("Cristina", "James Bond"));

Map<String,String> friends = Map.ofEntries(entry("Raphael", "Star Wars"), entry("Cristina", "Maxtrix"));

Map<String,String> everyone = new HashMap<>(family);
friends.forEach((k,v) -> //중복된 키가 있으면 두 값을 연결
                    everyone.merge(k,v,(movie1, movie2) -> movie1 + "&" + movie2));
```

```java
// 영화 시청 카운트 세기
moviesToCount.merge(movieName 1, (key,count) -> count + 1); // 키가 없으면 값은 1, 있으면 1 더함.
```

## 🍎 개선된 ConcurrentHashMap

- 동시성 문제를 해결한 HashMap
- 내부 자료구조의 특정 부분만 잠궈서 동시 추가, 갱신 작업을 허용한다.

### ConcurrentHashMap의 연산

- forEach : 각 (키,값) 쌍에 주어진 액션을 실행
- reduce : 모든 키,값을 제공된 리듀스를 이용해 결과로 합침
- search : 널이 아닌 값을 반환할 때까지 각 키, 값 쌍에 함수를 적용

- ConcurrentHashMap의 상태를 잠그지 않고 연산을 수행한다. 즉 동시성 문제가 발생한다.
- 따라서 연산에 병렬성 기준값을 지정해야 한다.

```java
ConcurrentHashMap<String, Long> map = new ConcurrentHashMap<>();
long parallelismThreshold = 1;

Optional<Integer> maxValue = Optional.ofNullable(map.reduceValues(parallelismThreshold, Long::max));
```

### 계수 - mappingCount

- `mappingCount` : 맵의 매핑 개수를 반환
- 기존의 size 대신 이 메서드를 사용하는 것이 좋다. 매핑의 개수가 int의 범위를 넘어가는 상황을 대처할 수 있다.

### 집합뷰 - keySet

- `keySet` : ConcurrentHashMap을 Set으로 반환
- `newKeySet` : ConcurrentHashMap으로 유지됨.
