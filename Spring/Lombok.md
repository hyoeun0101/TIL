### 🍎 @ToString

- toString 메서드를 작성하지 않아도 된다.
- 부모 클래스도 출력하고 싶다면 (callSuper=true)로 설정한다. default는 false이다.


### 🍎 @NoArgsConstructor
- 파라미터가 없는 기본 생성자를 만든다.
- 하지만 final이 붙은 필드가 있는데 이를 사용하면 error 발생. `@NoArgsConstructor(force = true)`로 작성하면, final 필드는 자동 초기화가 된다.
- @NonNull 필드는 force = true 옵션을 주어도 초기화가 되지 않기 때문에 프로그래머가 할당해주어야 함.


### 🍎 @RequiredArgsConstructor
- 초기화되지 않은 final 필드, @NonNull 필드를 파라미터로 가진 생성자를 만든다.
- @NonNull 필드의 경우, 생성자 내부에 명시적으로 null을 체크하는 로직을 생성한다.

### 🍎 @AllArgsConstructor
- 모든 필드를 파라미터로 가진 생성자를 만든다.
- 마찬가지로 @NonNull 필드는 생성자 내부에 null을 체크하는 로직을 생성한다.


- 위의 세 생성자 애노테이션이 생성하는 생성자의 접근자는 당연히 private.
- 그렇지만 static factory method를 사용하면 생성자의 인스턴스 생성이 가능하다.

```java
//옵션 staticName에 of를 작성하면
@RequriedArgsConstructor(staticName = "of")

//new 연산자 사용하지 않고 아래와 같이 생성자 생성이 가능.
MapEntry.of("foo",5);
```
- 주의할 것 : 선언된 필드의 순서대로 생성자 파라미터를 생성한다. 이는 에러를 초래할 수 있어서 사용을 금지해야 한다는 주장도 있다고 한다.

### 🍎 @NonNull
- 필드에 null을 허용하지 않음

### 🍎 @Nullable
- 필드에 null을 허용함.

