## 🍎 제네릭
- 다양한 타입을 다루는 메서드나 컬렉션 클래스에서 컴파일 시 타입 체크를 해주는 기능

### 제네릭의 장점
- 컴파일 시 타입 체크를 해줌으로써 타입 안전성을 높인다.
- 타입 체크와 형변환 코드가 필요없어서 코드가 간결해진다. 다룰 객체의 타입을 미리 명시함으로써 번거로운 형변환을 줄여준다.

### 제네릭의 제약
1. static 멤버에 타입 매개변수 T를 사용할 수 없다.
    - T는 인스턴스별로 다르게 동작하므로, 인스턴스 변수로 간주되기 때문이다.
```java
class Box<T> {
    static T item; //에러!!
}
```
2. 제네릭 타입의 배열을 생성할 수 없다. 하지만 제네릭 배열 타입의 참조변수를 선언하는 건 가능하다.
    - 이는 new 연산자 때문인데, 이 연산자는 컴파일 시점에 타입 T가 뭔지 정확히 알아야 한다. 
    - 하지만 Box<T> 클래스를 컴파일 하는 시점엔 T가 어떤 타입인지 알 수가 없다.
    - 제네릭 배열을 생성해야 한다면 Reflection API의 newInstance()와 같이 동적으로 객체를 생성하는 메서드를 사용해야 한다. 또는 Object 배열을 생성 후 T[]로 형변환해야 한다.
```java
class Box<T> {
    T[] itemArr; // T타입의 배열을 참조할 순 있음.

    T[] toArray() {
        T[] tempArr = new T[itemArr.length]; //에러. 제네릭 배열은 생성할 수 없다.
        return tempArr;
    }
}
```
3. new 연산자, instanceof 연산자 뒤에 올 수 없다. 컴파일 시점에 타입 T가 무엇인지 정확히 알 수 없기 때문이다.

## 🍎 지네릭 클래스
```java
class Box<T> {// 원시 타입:Box, 타입 매개변수: T
    T item;

    void setItem(T item) {
        this.item = item;
    }

    T getItem() {
        return item;
    }
}

...

Box<String> box = new Box<String>(); //String을 매개변수화된 타입이라고 부른다.
```

- 단 이전 코드와 호환을 위해 제네릭 클래스인데도 예전의 방식으로 객체를 생성하는 것이 허용된다. 이 경우 타입이 안전하지 않다는 경고문을 보여준다.

```java
Box b = new Box(); //Ok. T는 Object로 간주된다.
b.setItem("ABC"); //경고
b.setItem(new Object()); //경고

Box<Object> b = new Box<Object>(); //Ok.
b.setItem("ABC");
b.setItem(new Object());
```

- 제네릭 타입은 하나의 타입만 저장할 수 있게 하므로 타입 매개변수가 상속관계에 있어도 컴파일 오류가 발생한다.
```java
Box<Fruite> appleBox = new Box<Apple>(); //에러!
Box<Apple> appleBox = new Box<Apple>(); //무조건 같아야함.
Box<Apple> appleBox = new Box<Apple>(); // JDK 1.7부터 생성자에 타입 매개변수 생략 가능
```
 

### 지네릭 클래스의 제한
- 제네릭 타입T에 extends를 붙이면 타입의 종류를 제한할 수 있다.
```java
class FruitBox<T extends Fruit> {} //T는 Fruit와 그 자손만 가능하다.

....

FriutBox<Fruit> fruitBox = new FruitBox<>();
fruitBox.add(new Apple()); //ok!
fruitBox.add(new Orange()); //ok!
```
- 주의할 것은 클래스가 아니라 인터페이스를 구현을 나타낼 때도 extends를 사용한다.
```java
interface Eatable{...}

class FruitBox<T extends Eatable> {...}

class FruitBox<T extends Eatable & Fruit> {...} //T는 Fruit의 자손이면서 Eatable의 구현체.
```

## 🍎 와일드 카드 <?> - 제네릭 타입의 다형성
- 다음 예시를 보며 와일드 카드를 이해해보자.
```java
// 과일박스를 받아 주스를 만드는 메서드이다. 
// Juicer 클래스는 제네릭 클래스도 아닌데다, 
// 지네릭 클래스라고 해도 static 메서드에는 타입 매개변수 T를 사용할 수 없다.
// 따라서 제네릭을 사용하지 않던가, 아래와 같이 특정 타입을 지정해야 한다.
// 하지만 특정 타입을 지정하게 되면 다른 타입이 들어올 수 없게 된다.
class Juicer {
    static Juice makeJuice(FruitBox<Fruit> box) {
        String tmp = "";
        for(Fruit f : box.getList()) tmp += f + " ";
        return new Juice(tmp);
    }
}

...

FruitBox<Apple> appleBox = new FruitBox<Apple>();
Juicer.makeJuice(appleBox); //에러! FruitBox<Fruit>만 가능함.
```

- 위의 예시같은 경우를 해결하기 위해 와일드 카드가 나왔다. 다시 말하자면 와일드 카드는 제네릭 타입의 다형성을 지원한다.
- 와일드 카드는 모든 타입이 될 수 있다. ?는 Object 타입과 다를게 없다.

```
<? extends T> : lower bound 와일드 카드. T와 그 자손들만 가능
<? super T> : upper bound 와일드 카드. T와 그 부모만 가능
<?> : 모든 타입이 가능. <? extends Object>와 동일

- 제네렉 클래스와 달리 와일드 카드에는 & 기호를 사용할 수 없다! <? extends T & E>와 같이 할 수 없다.
```

[참고1]

```java
static Juice makeJuice(FruitBox<?> box) { //(1)
    String tmp = "";
    for(Fruit f : box.getList()) { //(2)
        tmp += f + " ";
    }
    return new Juice(tmp);
}

...

class FruitBox<T extneds Fruit> extends Box<T> {} //(3)
```
- 위의 예제 (1) makeJuice에서 FruitBox의 제네릭 타입이 ?로 모든 타입이 가능한데, (2)에서 컴파일 오류가 발생하지 않는 이유는?
    - (3) FruitBox에서 T를 Fruit로 제한을 주었기 때문이다.      


## 🍎 지네릭 메서드
- 지네릭 타입 선언 위치는 반환 타입 바로 앞이다.
```java
static <T> void sort(List<T> list, Comparator<? super T> c)
```
- 제네릭 클래스에 정의된 타입 매개변수와 제네릭 메서드에 정의된 타입 매개변수는 전혀 다른 것이다.
- 제네릭 메서드는 제네릭 클래스가 아닌 클래스에도 정의될 수 있다.
- static 멤버에는 제네릭 타입을 사용할 수 없지만 메서드에 제네릭 타입을 선언하여 사용할 수 있다.
- 지역 변수를 선언한 것과 같다고 생각하면 쉽다.

```java
static Juice makeJuice(FruitBox<? extends Fruit> box) {...}

//제네릭 메서드로 변경
static <T extends Fruit> makeJuice(FruitBox<T> box) {...}

// 제네릭 메서드 호출
FruitBox<Fruit> fruitBox = new FruitBox<Fruit>();
FruitBox<Apple> appleBox = new FruitBox<Apple>();

System.out.println(Juicer.<Fruit>makeJuice(fruitBox));
System.out.println(Juicer.<Apple>makeJuice(appleBox));
// 대부분의 경우 컴파일러가 타입을 추정할 수 있어서 생략 가능하다.
System.out.println(Juicer.makeJuice(fruitBox));
System.out.println(Juicer.makeJuice(appleBox));

//대입된 타입을 생략할 수 없는 경우엔 참조변수나 클래스 이름을 생략할 수 없다.
System.out.println(<Fruit>makeJuice(fruitBox)); //에러! 클래스 생략 불가
System.out.println(this.<Fruit>makeJuice(fruitBox)); //ok
System.out.println(Juicer.<Fruit>makeJuice(fruitBox)); //ok

```

- 같은 클래스에 있는 멤버끼리는 클래스, this(참조변수) 생략하고 메서드 이름만으로 호출이 가능하다. 하지만 대입된 타입이 있을 땐 반드시 써줘야 한다.

### 복잡한 제네릭 메서드

- Collections 클래스의 sort
```java
public static <T extends Comparable<? super T>> void sort(List<T> list)

//이해가 안된다면 와일드 카드를 걷어내보기
//List의 요소는 Comparable의 구현체이다. 인터페이스도 사용법은 똑같다.
public static <T extends Comparable<T>> void sort(List<T> list)
```
```java
public static <T extends Comparable<? super T>> void sort(List<T> list)
```
 - 타입 T를 요소로 가지는 List.
 - T는 Comparable를 구현한 클래스여야 한다.
 - 만일 T가 Student이고, Person의 자손이라면 <? super T>는 Student, Person, Object가 모두 가능하다. 

 ## 🍎 제네릭 타입의 형변환

 ### 제네릭 타입과 넌제네릭 타입 간의 형변환
 - 제네릭 타입과 원시 타입(primitive type) 간의 형변환이 가능은 하지만 경고가 발생한다.

```java
Box box = null;
Box<Object> objBox = null;

box = (Box)objBox; // 제네릭타입-> 원시타입, 경고발생
objBox = (Box<Object>) box; //원시타입-> 제네릭타입, 경고발생
```
### 제네릭 타입 간의 형변환

- 타입이 다른 제네릭 타입 간에는 형변환이 안된다!

```java
Box<Object> objBox = null;
Box<String> strBox = null;

objBox = (Box<Object>)strBox; //에러!
strBox = (Box<String>) objBox; //에러!

```
### 와일드 카드의 형변환
- `Optional<Object>` -> `Optional<T>` 이건 불가능
- `Optional<?>` -> `Optional<?>` ->  `Optional<T>` 이건 가능, 미확인 타입 경고발생
- `<? extends Object>` -> `<? extends String>` 가능, 미확인 타ㅂ 경고발생.
- `<? extends String>` -> `<? extends Object>` 가능, 미확인 타입 경고발생.

```java
// Box<String> -> Box<?> 가능!
Box<? extends Object> wBox = new Box<String>();

// FruitBox<? extends Fruit> -> FruitBox<String> 가능하지만 경고 발생
FruitBox<? extends Fruit> box = null;
FruitBox<Apple> appleBox = (FruitBox<Apple>)box; //ok. 미확인 타입으로 경고 발생


Optional<?> EMPTY = new Optional<>();// 가능
Optional<?> EMPTY = new Optional<Object>(); //위와 같음.
Optional<?> EMPTY = new Optional<?>(); //에러! 미확인 타입은 생성 불가.


Optional<?> wopt = new Optional<Object>();
Optional<Object> oopt = new Optional<Object>();
Optional<String> sopt = (Optional<String>)wopt; //Optional<?> -> Optional<String> 가능
Optional<String> sopt = (Optional<String>)oopt; //Optional<Object> -> Optional<String> 에러! 불가능

```

## 🍎 제네릭 타입의 제거
- 컴파일러는 제네릭 타입을 이용해서 타입 체크를 한 후 필요한 곳에 형변환을 넣어준다. 그리고 제네릭 타입을 소거한다. 즉 클래스 파일에는 제네릭 타입에 대한 정보가 없다.
### 제네릭 타입 제거하는 과정
1. 제네렉 타입의 bound를 제거한다.
    - <T extends Fruit>라면 Fruit로 지환된다. <T>인 경우엔 Object로 치환된다. 그리고 클래스 옆의 선언은 제거된다.
```java
class Box<T extends Fruit> {
    void add(T t){...}
}

//클래스 파일
class Box {
    void add(Fruit t){...}
}
```
2. 제네릭 타입을 제거한 후 타입이 일치하지 않으면 형변환을 추가한다.

```java
T get(int i) {
    return list.get(i);
}

//클래스 파일
Fruit get(int i) {
    return (Fruit)list.get(i); //List의 get()은 Object를 반환하므로 형변환이 필요
} 
```















## 지네릭 타입
```java
List<Product> list = new ArrayList<Product>();
list.add(new Product());
list.add(new Tv());
list.add(new Audio());
```
참조변수의 지네릭 타입과 객체의 지네릭 타입은 일치해야한다. 지네릭 타입의 자손 객체는 들어올 수 있다.     
대신 ArrayList에 저장된 객체를 꺼낼 때 형변환이 필요하다.

`Tv t = (Tv)list.get(1);`        


### 지네릭 타입의 다형성 - 와일드 카드
원래 참조변수의 지네릭 타입과 동일한 지네릭 타입인 객체만 들어올 수 있다. 하지만 와일드 카드로 지네릭 타입의 다형성을 나타낼 수 있다.   
```java
ArrayList<? extends Product> list = new ArrayList<Tv>();
ArrayList<? extends Product> list = new ArrayList<Audio>();
```

`<? extends T>` : T와 그 자손들   
`<? super T>` : T와 그 부모들   
`<?>` : 모든 타입 가능 



### 지네릭 타입의 형변환

- 지네릭 타입과 원시 타입 형변환 : 가능.but 경고 발생
```java
Box box = null;
Box<Object> objBox = null;
box = (Box)objBox;//지네릭-> 원시 가능.경고 발생
```
- 지네릭 타입 간 형변환 : 불가능
```java
Box<Object> objBox = null;
Box<String> strBox = null;
objBox = (Box<Object>)strBox; //에러
```
- 가능하게 하려면? 와일드 카드
```java
Box<? extends Object> wBox = strBox;
```

### 컴파일 시 지네릭 타입 제거
컴파일러는 지네릭 타입을 이용해서 소스파일(.java)을 체크하고, 지네릭 타입을 제거한다. 또한 필요한 부분에 형변환을 추가한다.
[.java 파일]
```java
class Box<T extends Fruit>{
    void add(T t){}
}
```
[.class 파일]
```java
class Box{
    void add(Fruit t){}
}
```