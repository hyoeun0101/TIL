## 🍎 raw 타입은 사용하지 마라.
- List<T> : generic type
- List<String> : parameterized type
- List<?> : unbounded wildcard type
- List<? extends Number> : bounded wildcard type
- List : raw type


### raw type?
- raw type이란 타입 선언에서 제네릭 타입을 사용하지 않은 타입을 말한다.
- 로 타입을 사용하게 되면 잘못된 타입이 컬렉션에 추가되어도 컴파일 시점에 에러를 잡지 못하고, 런타임 시점에 오류가 발생한다.
- 즉 로 타입은 제네릭 타입이 가져다주는 타입 안전성과 표현력을 모두 잃는다.
    - 로 타입이 아직 남아있는 이유는 호환성때문이다.

```java
package org.effectvie.item26;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class Exam01 {
    enum Coin {
        COIN_10, COIN_50, COIN_100
    }
    enum Stamp {
        STAMP_10, STAMP_50, STAMP_100
    }
    //로 타입을 사용하면 컴파일 시점에 오류를 잡지 못한다.
    private static final Collection stamps = new ArrayList();

    public static void main(String[] args) {
        stamps.add(Coin.COIN_10);
        
        for(Iterator i = stamps.iterator(); i.hasNext()) {
            Stamp stamp = (Stamp) i.next(); //ClassCastException 발생!
        }
    }
}
```
- 반드시 제네릭 타입을 사용하자!
```java
package org.effectvie.item26;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class Exam01 {
    enum Coin {
        COIN_10, COIN_50, COIN_100
    }
    enum Stamp {
        STAMP_10, STAMP_50, STAMP_100
    }
    //제네릭 타입을 사용해야 컴파일 에러가 발생한다.
    private static final Collection<Stamp> stamps = new ArrayList();

    public static void main(String[] args) {
        stamps.add(Coin.COIN_10); // 컴파일 에러!
        
        for(Iterator i = stamps.iterator(); i.hasNext()) {
            Stamp stamp = (Stamp) i.next(); //ClassCastException 발생!
        }
    }
}
```

### List vs List<Object>
- List는 제네릭 타입을 아예 사용하지 않은 것이고, List<Object>는 모든 타입을 허용한다는 의미다.
- List에 List<String>을 넘길 수 있지만, List<Object>에 List<String>을 넘기면 컴파일 에러가 발생한다.

### List vs List<?>
- 실제 타입이 무엇인지 신경쓰고 싶지 않다면 와일드카드 타입<?>을 사용하자.
- 로 타입은 타입 불변성을 보장해주지 않는다.
```java
package org.effectvie.item26;

import java.util.*;

public class Exam01 {
    public static void main(String[] args) {
        Set<Integer> s1 = new HashSet();
        s1.add(1);
        Set<Integer> s2 = new HashSet();
        s2.add(2);
        numElInCommon(s1, s2);
    }
    private static int numElInCommon(Set s1, Set s2) {
        s1.add("A"); //s1은 Integer인데 String이 들어간다.
        int result = 0;
        for(Object o1 : s1) {
            if(s2.contains(o1)) result++;
        }
        return result;
    }
}
```
- 와일드 타입을 사용하면 타입 불변성을 보장해준다.
```java
package org.effectvie.item26;

import java.util.*;

public class Exam01 {
    public static void main(String[] args) {
        Set<Integer> s1 = new HashSet();
        s1.add(1);
        Set<Integer> s2 = new HashSet();
        s2.add(2);
        numElInCommon(s1, s2);
    }
    private static int numElInCommon(Set<?> s1, Set<?> s2) {
        s1.add("A"); //컴파일 에러
        int result = 0;
        for(Object o1 : s1) {
            if(s2.contains(o1)) result++;
        }
        return result;
    }
}
```


### 로 타입을 사용하는 경우
1. class 리터럴에는 로 타입을 사용해야 한다. 자바 명세는 class 리터럴에 매개변수화 타입을 사용하지 못하게 했다.
    - ex) List.class, String[].class, int.class는 허용하고 List<String>.class, List<?>.class는 허용하지 않는다.
2. new 연산자, instanceof 연산자에는 제네릭을 사용할 수 없다. 컴파일 시점에 실제 타입이 어떤 타입인지 모르기 때문이다.
```java
if(o instanceof Set) {
    Set<?> s = (Set<?>) o;
}
```
