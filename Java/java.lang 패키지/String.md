## 🍎 String

String은 `immutable 클래스`이다. String이 생성한 문자열은 `변경불가`이다.
String을 생성하는 방법은 `문자열 리터럴`과 `String 생성자`가 있다.

### 문자열 리터럴

- 문자열 리터럴로 문자열을 만들면 컴파일 시 클래스 파일에 저장된다.
- 그래서 같은 내용의 문자열을 또 사용했을 때, 똑같은 문자열을 생성하지 않고 클래스 파일에 있는 문자열을 사용한다.
- 클래스 파일이 클래스 로더에 의해 메모리에 올라갈 때, 클래스 파일의 리터럴은 JVM의 constant pool에 저장된다.

```java
String str = "abc";
```

이 코드가 실행되면 Heap Area의 String constant pool에 다음과 같이 저장된다.

![img](https://user-images.githubusercontent.com/96059261/207234126-46d8b9d0-6aee-45bb-ab26-374e086ae64b.png)

문자열 리터럴은 String Pool에 저장된다. 따라서 String Pool에 값이 존재할 경우 해당 인덱스만 참조한다.  
new 연산자를 사용한 문자열은 상수풀에 저장되지 않고, 새로운 객체가 생성된다.

### constant pool(상수풀)

: `리터럴 상수 값을 저장`하는 곳. String, 숫자,특정 상수에 대한  
Method Area에 있어서 GC의 대상이 아니다.

## 빈 문자열

빈 문자열은 길이가 0인 char배열이 생성된다.

## 🍎 String의 equals()와 hashCode()

String에는 다음과 같이 equals()와 hashCode()가 오버라이딩되어 있어서 객체의 참조변수의 값이 아니라 객체의 값을 비교하는 것이다.

```java
public boolean equals(Object anObject) {
    if (this == anObject) {//리터럴일 경우 값이 같으면 true
        return true;
    }
    if (anObject instanceof String) {//String 객체일 경우
        String aString = (String)anObject;//String으로 변환
        if (coder() == aString.coder()) {//coder가 같은지 비교한다. 인코딩 타입에 맞는 equals()를 호출하고 있다.
            return isLatin1() ? StringLatin1.equals(value, aString.value)
                                : StringUTF16.equals(value, aString.value);
        }
    }
    return false;
}

public int hashCode() {
    int h = hash;
    if (h == 0 && value.length > 0) {
        hash = h = isLatin1() ? StringLatin1.hashCode(value)
                                : StringUTF16.hashCode(value);
    }
    return h;
}
```

---

String 내부 코드를 보니 char[]이 아닌 byte[]를 사용하고 있었다. 찾아보니 java 11에서는 byte[]배열을 사용한다. 왜 byte배열로 바뀌었는지 의문이 들어 찾아보았다.

```java
public final class String
    implements java.io.Serializable, Comparable<String>, CharSequence {
          @Stable
    private final byte[] value;
    private final byte coder;
}
```

coder는 byte 배열을 인코딩하는데 사용하는 변수다.
String 문자열을 지원하는 인코딩은 LATIN-1(1byte), UTF-16(2byte) 두 가지가 있다.  
기존의 char형은 UTF-16 기반으로 2byte를 참조한다.  
영문는 1byte로 표현이 가능하지만, 문자열은 기본적으로 char형이었기 때문에 2byte를 사용했다. 즉 1byte는 의미없는 값으로 채워졌다는 것이다.  
byte형을 사용하면 영문의 경우 1byte로 표현이 가능하게 되어 메모리 공간을 절약할 수 있게 되었다.

### String이 Java에서 Immutable한 이유

- 캐싱
  - JVM이 String Constant Pool 영역을 만들어 데이터를 공유하여 사용한다.
- 동기화
  - 어떤 데이터이든 Immutable하면 멀티 쓰레드 환경에서 동기화 문제가 발생하지 않기 때문에 안전하다.
- 보안성
  - DB의 보안이 중요한 정보는 String으로 전달되는데, 만약 Mutable하다면 참조에 의해 값이 변경될 수 있다.

---

## 🍎 StringBuffer

: String과 유사하지만 StringBuffer는 `mutable`이다. 내부적으로 문자열 수정을 위한 Buffer를 가지고 있다.  
**생성자 4개**

```java
@HotSpotIntrinsicCandidate
public StringBuffer() {
    super(16);
}
@HotSpotIntrinsicCandidate
public StringBuffer(int capacity) {
    super(capacity);
}
@HotSpotIntrinsicCandidate
public StringBuffer(String str) {
    super(str.length() + 16);
    append(str);
}
public StringBuffer(CharSequence seq) {
    this(seq.length() + 16);
    append(seq);
}

```

- 지정한 문자열보다 16만큼 더 크게 생성하는 것을 볼 수 있다.

- equals()는 오버라이딩되어 있지 않아서 ==연산자와 같다.따라서 `str.toString()`으로 String으로 변환 후 equals()를 사용하자.
- `동기화`되어 있다.
  - 각 메서드별로 synchronized 키워드가 존재하며, 멀티쓰레드 환경에서 동기화를 지원한다.
  - 멀티쓰레드가 아닌 경우 동기화는 불필요하게 성능만 떨어뜨린다.

## 🍎 StringBuilder

- 동기화 뺀 `StringBuffer`
- StringBuffer와 마찬가지로 `mutable`하다.
- 단일쓰레드 환경이라면 StringBuilder를 사용하는 것이 성능적으로 좋다.

## 🍎 String.valueOf()와 toString()

- `String.valueOf()` : 파라미터가 null이면 문자열 "null"을 만들어 반환한다.
  - 따라서 null 체크 시 `"null".equals(str)` 이렇게 해야한다.
- `toString` : 대상 값이 null이면 NullPointerException이 발생한다.

```java
//map.get("nullValue") 값이 null이라면
String str1 = String.valueOf(map.get("nullValue"));// str1="null"
String str2 = map.get("nullValue").toString(); //NullPointerException 발생
```
