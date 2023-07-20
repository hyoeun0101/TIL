## 람다식
= 익명 클래스의 객체!    
람다식은 `익명 객체`라 할 수 있다. 그럼 이 익명 객체를 어느 타입의 참조변수가 참조할까?    
바로 `함수형 인터페이스`!   

## 함수형 인터페이스 (Functionl Interface)
: 추상 메서드가 하나뿐인 인터페이스, 람다식을 다루기 위한 인터페이스다. @FunctionalInterface 애너테이션을 붙인다.    

```java
@FunctionalInterface
interface MyFunction{
    int max(int a, int b);//추상 메서드
}

class Exam{
    MyFunction f = (a,b)->a+b;
    System.out.println(f.max(1,2));
}
```

## java.util.function 패키지

|함수형 인터페이스|메서드|설명|
|---------------|----------------|---|
|java.lang.Runnable| void run()|매개변수X, 반환값 X|
|Supplier<T>|T get()|매개변수X, 반환값O|
|Comsumer<T>|void accept(T t)|매개변수O, 반환값X|
|BiConsumer<T,U>|-|-|
|Predicate<T>|boolean test(T t)|매개변수O, 반환값 T/F|
|BiPredicate<T,U>|-|-|
|Function<T,R>|R apply(T t)|매개변수O, 반환값O|
|UnaryOperator<T>|T apply(T t)|Function의 자손, 매개변수와 반환타입이 같음|
|BiFunction<T,U>|-|-|
|BinaryOperator<T>|-|BiFunction의 자손, 두개의 매개변수와 반환타입이 같음|

## Predicate의 결합
- 디폴트 메서드 : and(), or(), negate()
- static 메서드 : isEqual()
```java
class Ex14_3{
    public static void main(String[] args){
        Predicate<Integer> p = i -> i < 100;
        Predicate<Integer> q = i -> i < 200;
        Predicate<Integer> r = i -> i%2==0;
        Predicate<Integer> notP = p.negate();

        Predicate<Integer> all = notP.and(q.or(r));
        System.out.println(all.test(150));

        String str1 = "abc";
        String str2 = "abc";
        boolean result = Predicate.isEqual(str1).test(str2);
        System.out.println(result);
    }
}
```
## 컬렉션 프레임워크와 함수형 인터페이스
```java
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class Ex{
    public static void main(String[] args) {
        ArrayList<Integer> list = new ArrayList<>();
        for(int i=0;i<10;i++){
            list.add(i);
        }
        //요소 하나씩 반환
        list.forEach(i->System.out.print(i+","));//0,1,2,3,4,5,6,7,8,9

        //조건이 true이면 제거
        list.removeIf(x->x%2==0 || x%3==0);
        System.out.println(list);//1,5,7
        //모든 요소 변환
        list.replaceAll(i->i*10);
        System.out.println(list);//10,50,70

        Map<String,String> map = new HashMap<>();
        map.put("1", "1");
        map.put("2", "2");
        map.put("3", "3");
        map.put("4", "4");

        map.forEach((k,v)->System.out.println(k+":"+v));

    }
}
```

## 메서드 참조
`클래스이름::메서드이름`    
`참조변수::메서드이름`- 잘안씀
