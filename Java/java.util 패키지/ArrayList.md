## 🔴 java.util.ArrayList

- 배열의 크기가 변경 가능하다. 내부적으로 배열을 사용하지만 자동으로 크기가 조절된다.
- 요소 추가/삭제 가능
- 리스트처럼 데이터를 순차적으로 저장한다.

## 🔴 java.util.Arrays

- 배열을 다루는 정적 메서드 모음이다.
- 배열을 리스트로 변환할 때 사용한다.
- 배열 복사, 변환, 정렬 등 유틸 기능을 제공한다.

```java
import java.util.Arrays;

int[] numbers = {1, 2, 3}; 
Arrays.sort(numbers); //numbers 배열 내부 변경됨.
System.out.println(Arrays.toString(numbers)); // [1, 2, 3]
```

### Arrays.asList()
- 고정 크키 리스트를 반환한다.
- 요소 추가/삭제 불가능.

```java
List<String> list = Arrays.asList("A", "B", "C");
list.add("D"); // UnsupportedOperationException 예외 발생!!
```