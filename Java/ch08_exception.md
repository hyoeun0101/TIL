# 예외 처리
1. 컴파일 에러 - 컴파일 시 발생하는 에러
2. 런타임 에러 - 실행 시 발생하는 에러
3. 논리적 에러 - 실행은 되지만, 의도대로 동작하지 않는 것
   
   
자바에서는 실행 시 발생할 수 있는 오류를 `Error`와 `Exception`로 구분한다. Error는 메모리 부족 등과 같이 심각한 오류이고 Exception은 수습될 수 있는 미약한 오류이다.   
   
![image](https://velog.velcdn.com/images/salgu1998/post/c8310c15-9633-4d69-b86e-61c884a01593/image.png)    

### 예외 클래스 Exception, RuntimeException
 - 모든 예외 클래스는 `Exception 클래스의 자손이`다.
1. `Exception` : __checked__(예외 처리 필수)    
 사용자의 실수와 같은 `외부적인 이유`로 발생한다.

```
checked 예외는 반드시 예외 처리를 해줘야하며, 하지 않으면 컴파일조차 되지 않는다.
```
    종류 : `FileNotFoundException`, `ClassNotFoundException`, `IOException` 등     

2. `RuntimeException` : __unchecked__ (예외 처리 선택)
프로그래머의 실수로 발생한다.
```
예외 처리를 하지 않아도 성공적으로 컴파일이 된다. 하지만 발생한 예외를 처리하지 않으면 프로그램이 비정상적으로 종료된다. 
```
    종류 : `ArithmeticException`, `ClassCastException`, `NullPointerException`, `IndexOutOfBoundsException` 등


## 예외 처리하기 `try-catch문`
- try 블럭에서 예외가 발생하면, 발생한 예외와 일치하는 catch블럭이 있나 확인 후 실행. 없으면 예외 처리 안됨.

    1. `예외가 발생`하면, 그 예외 클래스의` 인스턴스`가 `생성`됨.
    2. catch() 괄호 내에 선언된 `참조변수`와 발생한 예외 클래스의 `인스턴스`를` instanceof연산자`로 검사를 함.
    3. 결과가 true일때까지 계속 찾고, 없으면 예외는 처리되지 않음.

```java
class A{
    public static void main(String[] args) {
        System.out.println(1);
        System.out.println(2);

        try{
            System.out.println(3);
            System.out.println(0/0); //ArithmeticException 발생
            System.out.println(4);
        }catch(ArithmeticException ae){
            if( ae instanceof ArithmeticException){
                System.out.println("ArithmeticException");
            }
        }catch(Exception e){
            System.out.println("Exception");
        }
    }
}
```

## 멀티 catch 블럭  
```java
try{
    ...
}catch(ExceptionA | ExceptionB e){
    e.printStackTrace();
}
``` 
catch() 블럭에서 `|` 기호로 예외 클래스를 여러 개 참조할 수 있다.   
단, 조상과 자손의 관계에 있다면 컴파일 에러가 발생한다. 그냥 조상 클래스만 써주는 것과 똑같기 때문이다.    
멀티 catch 블럭에서는 `실제로 어떤 예외 클래스가 발생한 건지 알 수 없다`. 여러 예외 클래스 중 공통 메서드(그것들의 조상 클래스에 선언된 메서드)만 사용 가능하다.
```java
try{
    ...
}catch(ExceptionA | ExceptionB e){
    if(e instanceof ExceptionA){
        ExceptionA e1 = (ExceptionA) e;
        e1.methodA(); //ExceptionA에 선언된 메서드만 호출 가능
    }
}
```

## 사용자 정의 예외 만들기
보통 Exception클래스 또는 RuntimeException클래스로부터 상속받아 정의한다.
```java
class MyException extends Exception{
    private final int ERR_CODE;

    MyException(String msg, int errCode){
        super(msg);
        ERR_CODE = errCode;
    }

    MyException(String msg){
        this(msg, 100);
    }

    public int getErrCode(){
        return ERR_CODE;
    }
}
```
`super(msg);`는 조상 클래스인 Exception 클래스의 생성자를 호출하는 코드이다.    
기존의 예외 클래스는 주로 Exception 클래스를 상속받아 `checked`로 작성하여 예외 처리가 반드시 필요하다.   
요즘은 예외 처리를 선택적으로 할 수 있도록 RuntimeException 클래스를 상속받아 작성하기도 한다.

## 연결된 예외
예외가 다른 예외를 발생시킬 수도 있다.     
예외 A가 예외 B를 발생시켰다면, A는 B의 원인 예외(cause Exception)이다.   

```
Throwable initCause(Throwable cause) : 지정한 예외를 원인 예외로 등록
Throwable getCause() : 원인 예외를 반환
RuntimeException(Throwable cause) : 원인 예외를 등록하는 RuntimeException 생성자
```

__왜 예외를 연결하여 발생시키는가?__   
1. 여러 가지 예외를 하나의 큰 분류로 묶기 위해서이다.
```java
try{
    startInstall(); //SpaceException 발생!!
    copyFiles();
}catch(InstallException e){ //SpaceException의 부모 클래스
    e.printStackTrace();
}
```
이 예시에서 InstallException를 SpaceException의 부모 클래스로 작성했다고 해보자. 이 역시 여러 가지 예외를 하나의 부모 클래스로 묶어 다룬다. 하지만 이렇게 작성하면 InstallException의 자식 예외 클래스 중 실제로 발생한 예외가 무엇인지 알 수 없다는 문제가 생긴다. 또 상속 관계를 변경해야 한다는 불편한 점이 있다.

2. checked예외를 unchecked예로 바꾸기 위해서이다.
checked 예외이지만 예외를 처리할 수 없는 상황일 때, checked 예외를 unchecked 예외로 바꿔 예외 처리를 하지 않도록 한다.   
SpaceException이 checked라면 RuntimeException 생성자를 이용해 unchecked로 바꿀 수 있다.


```java
package chap07;

public class Example {
    public static void main(String[] args) {
        try {
            install();
        } catch (InstallException e) {
            e.printStackTrace();
        }
    }

    static void install() throws InstallException{
        try{
            startInstall();
        }catch (SpaceException e){
            InstallException ie = new InstallException("설치 중 예외 발생");
            ie.initCause(e);
            throw ie;
        }
    }
    private static void startInstall() throws SpaceException{
        if(!enoughSpace()){
            throw new SpaceException("설치할 공간이 부족합니다.");
        }
    }
    static boolean enoughSpace(){
        return false;
    }
}


class InstallException extends Exception{
    InstallException(String msg){
        super(msg);
    }
}
class SpaceException extends Exception{
    SpaceException(String msg){
        super(msg);
    }
}
```

실행 결과
```
chap08.InstallException: 설치 중 예외 발생
	at chap08.Example.install(Example.java:16)
	at chap08.Example.main(Example.java:6)
Caused by: chap08.SpaceException: 설치할 공간이 부족합니다.
	at chap08.Example.startInstall(Example.java:23)
	at chap08.Example.install(Example.java:14)
	... 1 more
```
Caused by 를 통해 원인 예외를 출력한다.