## 상속
`코드의 재사용`, `중복 제거`를 통해 프로그램의 생산성을 높이고 유지보수가 쉽게 한다.
- 조상 클래스를 변경하면, 자식 클래스에게 영향을 미침.
- 단, 조상의 `생성자, 초기화 블럭`은 상속X
- 단일 상속 : 부모 클래스의 메서드끼리 충돌날 수 있으므로 자바에선 `단일 상속`이다.
- `오버라이딩` : 부모 클래스의 메서드를 재정의.
    - 접근 제어자가 부모 클래스보다 좁으면 X
    - 부모 클래스보다 예외 처리 많으면 X 

    <br>


** 포함 관계와 상속 관계? 어떻게 구분지을까?
- 포함 관계 : 원은 점을 가지고 있다.
- 상속 관계 : 스포츠카는 자동차이다.
```java
class Circle{
    Point p = new Point();
    int z;
}
class Point{
    int x;
    int y;
}
```


### 참조변수 super와 생성자 super()
참조변수 super : 부모의 멤버와 자식의 멤버 구분 짓기 위함.
```java
class A{
    int num=10;
}

class B extends A{
    int num=20;

    void method(){
        System.out.println(this.num);//20
        System.out.println(super.num);//10
    }
}
```
생성자 super() : 조상의 생성자 호출. 모든 생성자 첫줄에는 super(); 생략되어있다.
     
    

## 제어자
### 접근 제어자 - private, default, protected, public
### static - 클래스의, 공통적인
### final - 마지막의, 변경할 수 없는
- 클래스 : 부모 클래스 X 확장X
- 메서드 : 오버라이딩 X
- 변수 : 상수

### abstract - 추상의 


## 내부 클래스
** 내부 클래스의 장점    
1. 내부 클래스와 외부 클래스 간의 멤버 접근이 쉽다. (외부 클래스 객체 생성 필요X)
2. 코드의 캡슐화를 통해 복잡성 줄인다. 


|InterfaceInner|StaticInner|LocalInner|
|-----------|---------------|-------|
|외부 클래스의 인스턴스 멤버|im사용 X. ||
|외부 클래스의 인스터와 관련된 작업|||


|종류|역할| |
|---|--------|----|
|인스턴스 내부 클래스|외부 클래스의 인스턴스 멤버|외부 클래스의 인스턴스와 관련된 작업에 사용|
|static 내부 클래스|외부 클래스의 static 메서드에서 사용|
|지역 내부 클래스|외부 클래스의 메서드, 초기화 블럭 안에 선언|선언된 영역 내부에서만 사용ok|

** 내부 클래스의 제어자와 접근성
- 접근 제어자 사용 가능
- static 내부 클래스만 static 멤버 사용 ok.
- 다만, final static은 모든 내부 클래스에서 사용 ok.
- Static Inner에서 인스턴스멤버 접근 X. 굳이 하려면 외부 클래스의 객체 생성 후 가능
```java
Outer outer = new Outer();
InstanceInner inner = outer.new InstanceInner();
```
- 외부 클래스의 private 멤버도 접근 가능하다.
- 지역 클래스에서는 외부 클래스의 final 지역변수만 접근 가능하다.
    - 메서드는 소멸되고, 지역 클래스는 실행 중일 수도 있기 때문에.

- 외부 클래스의 변수 접근 : Outer.this.value    

## 익명 클래스
```java
class Ex7{
    Object iv = new Object(){   void method(){ }  };
    static Object cv = new Object(){  void method(){ }  };

    void myMethod(){
        Object lv = new Object(){ void method(){}  };
    }
}
```
컴파일 시 생성되는 클래스 파일   
Ex7.class   
Ex7$1.class
Ex7$2.class
Ex7$3.class

