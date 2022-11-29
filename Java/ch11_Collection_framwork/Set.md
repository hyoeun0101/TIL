## HashSet

: 중복 X, 순서 X

- 중복 X, 순서 O => `LinkedHashSet`
- 두 인스턴스를 같은 것으로 인식하기

```java
HashSet set = new HashSet();
set.add(new Person("kim",10));
set.add(new Person("kim",10));
```

서로 주소가 다르기 때문에 다른 객체이다. 다르게 하려면? Person클래스에 `equals()`와 `hashCode()`를 오버라이딩 해야한다.

```java
public boolean equals(Object obj){
    if(!(obj instanceOf Person)){
        Person p = (Person) obj;
        return name.equals(p.name) && age==p.age;
    }
}

public int hashCode(){
    return Objects.hash(name,age);
}
```

## TreeSet

= 이진 탐색 트리 : 왼쪽에는 작은 값, 오른쪽에는 큰 값을 저장. `정렬`

- 정렬, 검색(범위 검색)에 유리하다.
  - 정렬을 유지한다.
- 중복X
- 노드의 추가, 삭제에 시간이 걸린다.
- 왼쪽 마지막 레벨이 제일 작은 값, 오른쪽 마지막 레벨의 값이 제일 큰 값이다.
