## 🍎 Optional<T> 객체 생성
### Optional.of()
```java
String str = "abc";

Optional<String> optVal = Optional.of(str);

Optional<String> optVal = Optional.of("abc");

Optional<String> optVal = Optional.of(new String("abc"));
```
### Optional.ofNullable()
- 객체 T가 null일 가능성이 있다면 ofNullable()로 생성해야 한다.

```java
Optional<String> optVal = Optional.of(null);//NullPointerException 발생

Optional<String> optVal = Optional.ofNullabl(null);//null이어도 예외발생x
```
### Optional.<T>empty()

- 빈 객체로 생성한다.
```java
Optional<String> optVal = Optional.<String>empty();//빈 객체로 초기화
```

## 🍎 Optional<T> 객체 읽기
### get()
- 객체 T가 Null이면 `NoSuchElementException` 발생한다.

### ofElse(대체값)
- 객체 T가 null이면 대체값으로 지정한다.

### orElseGet(Supplier)
- 객체T가 null이면 람다식의 반환값으로 지정한다.
- 디폴트 메서드를 만드는데 시간이 걸리거나 Optinal이 비었을 때 기본값이 필요한 상황에 사용한다.

### orElseThrow(Supplier)
- 객체T가 null이면 제공하는 예외를 발생시킨다.

```java
Optional<String> optVal = Optional.of("abc");

String str1 = optVal.get(); // null이면 예외발생

String str2 = optVal.ofElse("");//null이면 ""으로 대체

String str3 = optVal.orElseGet(String::new);//null이면 빈 객체 생성

String str4 = optVal.orElseThrow(NullPointerException::new);//null이면 예외 발생
``` 
## 🍎 이외 메서드
### isPresent()
- 객체T가 null이면 false, null이 아니면 true
```java
if(Optional.ofNullable(str).isPresent()){
    System.out.println(str);
}
```
### ifPresent(Consumer)
- 객체 T가 null이 아니면 람다식 실행, null이면 실행 안한다.
```java
Optional.ofNullable(str).ifPresent(System.out::println);
```
### ifPresentOrElse(Consumer, Runnable)
- 객체T가 null이면 Runnalbe을 실행한다.

## 🍎 기본형 특화 Optional
- Optional은 어짜피 하나의 요소만 감싸기 때문에 기본형 특화 Optional로 성능을 개선할 수 없다. 또한 map같은 유용한 메서드를 사용할 수 없기 때문에 사용을 권장하지 않는다.
- OptionalInt, OptionalLong, OptionalDouble
- getAsInt(), getAsLong(), getAsDouble()

```java
OptionalInt opt = OptionalInt.of(0);//0을 저장
OptionalInt opt2 = OptionalInt.empty();//기본 초기화 0을 저장
```
- opt와 opt2의 값은 다르다.

```java
System.out.println(opt.isPresent());//true
System.out.println(opt2.isPresent());//false
System.out.println(opt.equals(opt2));//false

```

## 🍎 Optonal 안티 패턴
### collection, stream, 배열은 Optional로 감싸지 말자.
- Optional<List>를 반환하기 보다는 그냥 빈 ArrayList를 반환하는 것이 좋다. 그렇게 하면 클라이언트 코드에서 Optional 처리 코드를 넣지 않아도 된다. 
### Optional을 map의 키나 값으로 사용하지 말자.
- Optional을 map에서 사용하면 모호한 상황이 생긴다.
- key 자체가 없는 경우, key는 있지만 빈 Optional인 경우
### isPresent()를 사용하지 말자.
- if 지옥과 별 다를게 없는 로직이 된다.
```java
if(school.isPresent()) {
    Optional<ClassRoom> classRoom = school.getClassRoom();
    if(classRoom.isPresent()) {
        Optional<Teacher> teacher = classRoom.getTeacher();
        if(teacher.isPresent()) {
            Optional<Subject> subject = teacher.getSubject();
            if(subject.isPresent()) {
                String subjectName = subject.getSubjectName();
                return subjectName;
            }
        }
    }
}
return null;
```

- 메서드를 사용하자!
```java
Optional.ofNullable(school).map(School::getClassRoom)    //Optional<School>
                           .map(ClassRoom::getTeacher)   //Optional<ClassRoom>
                           .map(Teacher::getSubject)     //Optional<Teacher>
                           .map(Subject::getSubjectName) //Optional<Subject>
                           .orElse(null);
                           ```