# 예외 처리
1. 컴파일 에러 - 컴파일 시 발생 에러
2. 런타임 에러 - 실행 시 발생 에러
3. 논리적 에러 - 실행은 되지만, 의도대로 동작하지 않는 것
   
   
자바에서는 런타임 에러,즉  실행 시 발생할 수 있는 오류를 `에러`와 `예외`로 구분.   
   
(앞에서부터 조상)   
Obejct - Throwable - Exception - RuntimeException, IOExcpetion 등
                   - Error - OutOfMemoryError 등

### 예외 클래스 Exception
- RuntimeException클래스와 그 자손들( ArithmeticException, ClassCastException, NullPointerException, IndexOutOfBoundsException 등) : 프로그래머의 실수로 발생 , unchecked (예외 처리 선택)
- 이외의 Exception클래스와 그 자손들 : 사용자의 실수와 같은 외부의 이유로 발생, 존재하지 않은 파일의 이름 입력(FileNotFoundException), 클래스 이름 잘못 작성(ClassNotFoundException), checked (예외 처리 필수 - 예외처리 안하면 컴파일 되지도 않음.)

## 예외 처리하기 - try-catch문
- try 블럭에서 예외가 발생하면, 발생한 예외와 일치하는 catch블럭이 있나 확인 후 실행. 없으면 예외 처리 안됨.

1. 예외가 발생하면, 그 예외 클래스의 인스턴스가 생성됨.
2. catch() 괄호 내에 선언된 참조변수와 발생한 예외 클래스의 인스턴스를 instanceof연산자로 검사를 함.
3. 결과가 true일때까지 계속 찾고, 없으면 예외는 처리되지 않음.

* 멀티 catch 블럭   
- catch() 블럭에서 | 이걸로 예외 클래스를 여러 개 참조할 수 있는데, 조상과 자손의 관계는 안됨. 그냥 조상 클래스만 써주면 되기 때문.
- 멀티 catch 블럭에서는 실제로 어떤 예외 클래스가 발생한 건지 알 수 없음. 그래서 여러 예외 클래스 중 공통적인 멤버(그것들의 조상 클래스에 선언된 멤버)만 사용 가능함.

## 예외 발생시키기
1. 예외 객체 생성 - Exception e = new Exception("예외 메세지");
2. throw e;

## 예외 결과 얻기
- 예외 발생하면 getMessage(), printStackTrace()로 정보를 얻을 수 있음.
    - getMessage() : 발생한 예외 클래스의 인스턴스에 저장된 메세지 얻기
    - printStackTrace() : 예외 발생 시 호출 스택에 있었던 메서드의 정보와 예외 메세지를 화면에 출력

