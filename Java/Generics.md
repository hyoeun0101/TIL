## 지네릭스
: 다양한 객체를 다루는 메서드나 컬렉션 클래스에서 컴파일 시 타입 체크를 해주는 기능    

** 장점 **
- 컴파일 시 타입 체크를 해줌으로써 타입 안전성을 높인다.
- 타입 체크와 형변환 코드가 필요없어서 코드가 간결해진다.

** 제약 **
```java
class Box<T>{
    static T item;//에러
    static int compare(T t1, T t2){}//에러
}
```
- static 멤버에 타입 변수 T를 사용할 수 없다. static은 공통 부분인데, T는 인스턴스마다 변하기 때문에
- new 연산자, instanceOf 연산자 뒤에 올 수 없다. 컴파일 시점에 T가 어떤 타입인지 모르게 때문에

## 지네릭 클래스
`class Box<T>`   
Box : 원시 타입   
T : 타입 변수

### 지네릭 클래스의 제한
** 타입 변수 T에 타입의 종류를 제한하는 방법 **  
`class Box<T extends Product>`          
- T에는 Product 또는 그 자손 타입만 들어올 수 있다.
- 인터페이스를 구현해야하는 제한이 있을 때에도 extends를 사용한다.


`class FruitBox<T extends Fruit & Eatable> {...}`    
- T는 클래스 Fruit 자손이면서, 인터페이스 Eatable도 구현해야한다.



## 지네릭 메서드
```java
class FruitBox<T>{
    static <T> void sort(List<T> list, Comparator<? super T> c){...}

}
```
지네릭 타입을 선언하고 사용하는 것이다. 클래스의 T와 sort 메서드의 T는 다른 것이다. 당연히 List<T>는 sort에 선언된 지네릭 타입을 의미한다. 지역변수를 선언한 것과 같다고 생각하면 쉽다.   
메서드를 호출할 때마다 타입을 대입해야 한다.   

```java
static <T extends Fruit> Juice makeJuice(FruitBox<T> box){
    String tmp = "";
    for(Fruit f : box.getList()){
        tmp += f;
    }
    return new Juice(tmp);
}
```
호출 시 지네릭 타입을 명시해줘야 하지만 생략 가능하다.    
주의할 점은 지네릭 메서드 호출할 때, 대입된 타입을 생략할 수 없는 경우에는 참조변수나 클래 이름을 생략할 수 없다.
```java
FruitBox<Fruit> fruitBox = new FruitBox<Fruit>();

System.out.println(Juicer.<Fruit>makeJuice(fruitBox));
System.out.println(Juicer.makeJuice(fruitBox)); //생략가능
System.out.println(<Fruit>makeJuice(fruitBox)); //에러. 클래스 이름 생략x

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