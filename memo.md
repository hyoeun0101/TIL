# 오늘 배운 것 기록하기

## 20230208

### SQL>

- COUNT 할 때는 PK로 조건을 넣어줘야한다.
- SELECT 후 결과값이 NULL이면 원래 Exception이 발생한다. 아래 코드 덕분에 Exception이 발생하지 않는 것!

[mybatis-config.xml] - NullPointerException 방지

```xml
<settings>
    <setting name="jdbcTypeForNull" value="NULL"/><!-- Property 의 값이 null 일때 NULL 삽입 -->
    <setting name="callSettersOnNulls" value="true"/>
</settings>
```

- 불필요한 변수는 만들지 말자! getter로 호출하자.
- Controller - service - mapper 로직을 잊지말자. service에서 메서드끼리는 호출해도 된다.

---

## 230213

### <내가 만든 로직>

- 서비스에서 mapper.crossCheck() 한 후, mapper.truncateBiztaxCross()를 바로 호출하였다.

### [코드 리뷰]

- mapper.crossCheck()에서 에러가 발생하면 mapper.truncateBiztaxCross()는 실행하지 않는다.
- truncate는 에러와 상관없이 실행하면 무조건 마지막에 실행되어야 하는 로직이다.
- truncate는 Controller에서 finally에 넣어주는 것이 좋다.
- `단순 구현이 아닌 로직에 대해 생각을 한 번 더 하자!!`

### gson

:google에서 나온 object mapping.

- `gson.fromJson` : json을 Object로
- `gson.toJson` : Object를 json으로

---

## 230214

```java
ObjectMapper mapper = new ObjectMapper();
mapper.disable(org.codehaus.jackson.map.DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES); // 알지 못하는 프로퍼티가 오면 실패하지 않음.
```

- Spring Boot에서는 FAIL_ON_UNKNOWN_PROPERTIES 기본값은 false 이다.
- `FAIL_ON_UNKNOWN_PROPERTIES` : deserialize 시 알지 못하는 프로퍼티가 오면 실패
- 기본값이 false인 이유는 API의 견고함의 원칙 때문이다.
- 견고함의 원칙이란? 보내는 것은 엄하게, 받는 것은 너그럽게.

---

## 230215

### biztax 흐름 이해

- src/main/webapp/web.xml에 다음과 같은 설정이 있다.

```xml
<welcome-file-list>
<welcome-file>index.jsp</welcome-file>
</welcome-file-list>
```

- src/main/webapp/index.jsp를 보면 `response.sendRedirect`로 리다이렉트를 해준다.

```html
<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ page import ="java.net.URLEncoder" %>
<html>
  <head>
    <meta charset="UTF-8" />
    <title>이마트 사업자 세금계산서 관리시스템</title>
    <script>
      //location.href = '/xpf/install.html'
    </script>
  </head>
  <body>
    <% response.sendRedirect("/security/login.do"); %>
  </body>
</html>
```

---

## 230216

root.xml 가지고 만드는 root Ac. :웹과 관련없는 설정- 로깅처리하는 인터셉터, 뷰 리졸버, messageConvertor,
servlet.xml 가지고 만드는 servlet Ac

---

## 230221

### 메서드 오버로딩

- 동일한 책임을 가진 메서드인데 사용하는 매개변수가 다를 때. 가독성을 높이기 위해 사용한다.
  오버로딩하는 방법 2가지
- 인수의 수를 변경
- 데이터 유형을 변경

- 자바에서는 메서드 오버로딩할 때 리턴타입은 상관없는 이유는?
- 모호성 때문에.

```java
class Adder{
  static int add(int a,int b){return a+b;}
  static double add(int a,int b){return a+b;}
}

class TestOverloading3{
  public static void main(String[] args){
    System.out.println(Adder.add(11,11));//ambiguity
  }
}
```

```
Compile Time Error: method add(int,int) is already defined in class Adder
```

- 오버로딩시 타입변환

```java
class OverloadingCalculation1{
  void sum(int a,long b){System.out.println(a+b);}
  void sum(int a,int b,int c){System.out.println(a+b+c);}

  public static void main(String args[]){
  OverloadingCalculation1 obj=new OverloadingCalculation1();
  obj.sum(20,20);//now second int literal will be promoted to long
  obj.sum(20,20,20);

  }
}
```

### @Bean과 @Component

- @Bean
  - 개발자가 컨트롤이 불가능한 외부 라이브러리를 빈으로 등록하고 싶은 경우에 사용
  - ObjectMapper의 경우 ObjectMapper class에 @Component를 붙일 수 없으니 ObjectMapper의 인스턴스를 생성하는 메서드를 만들고 해당 메서드에 @Bean을 붙인다.
- @Component
  - 개발자가 직접 컨트롤이 가능한 class의 경우에 사용한다.

## 230306

- `socket.setSoTimeout()`은 소켓 연결 후 클라이언트의 InputStream에서 읽을 때, timeout 설정하는 것.
- 소켓 연결 시 timeout 주는 것은 `InetSocketAddress` 클래스를 사용한다.

---

## 230307

### Character.isDigit(char c)

- c의 값이 숫자이면 true, 아니면 false

```java
Charactor.isDigit('a'); // false
Charactor.isDigit('97'); //true
Charactor.isDigit(97); //false. int값 입력하면 char로 변환 후 체크한다. 즉 97을 'a'로 판단.

```

## 230323

- 소켓 통신 코드에선 데이터를 byte 배열로 주고 받는다. 받은 byte 배열을 순서대로 HeaderPos 객체, BodyPos 객체에 넣는다.
- 여기서 HeaderPos의 어떤 특정값에 따라서 다른 BodyPos 객체를 생성해야한다.
- 기존의 코드는 if-else를 반복적으로 사용하여 HeaerPos의 특정값에 따라 BodyPos를 처리한다.

```java
if(headerPos.getInstCode("HBPT")) {
  bodyReqPos = new BodyReqPos();
} else if(headerPos.getInstCode("EPCT")) {
  bodyReqPosCT = new BodyReqPosCT();
}
```

- 메서드에서 반복되는 if-else를 없애고자, 여러 BodyPos를 인터페이스로 추상화할 수 없을까 생각을 하였다.
- 결론은... 할 수 없다!!
- 사수님의 피드백 : 전문이나 API정의서를 보면 공통적인 것을 추상화할 수 있는지 없는지 판단할 수 있어야한다.
- 전문을 보니 BodyPos 부분은 공통적인 것이 없어 추상화가 불가능했다.

## 230327

```
io.netty.channel.ChannelPipelineException: com.mycloudmembership.prelaysocket2.netty.NettyEncoderDecoder$NettyDecoder is not a @Sharable handler, so can't be added or removed multiple times.

```

### [230706] DTO, VO, Entity 무엇을 사용해야 할까?
- DTO
  - Data Transfer Object. 데이터 전송 객체. 데이터 전달의 목적
  - 비즈니스 로직을 갖지 않는 순수한 데이터 객체, getter/setter 메소드만 갖는다.
  - setter를 가지는 경우 `가변 객체`로 활용된다.
  - final 필드, 생성자를 이용하면 불변 객체로 활용할 수 있다.
- VO
  - Value Object. 값 자체를 표현하는 객체. 객 자체를 값으로 사용하기 위함
  - 특정한 비즈니스 로직을 가진다.
  - `불변 객체`이며, 오직 읽기만 가능하다.
  - 값의 비교를 위해 equals, hashcode를 재정의한다. 그럼 같은 값을 가진 객체는 모두 똑같은 객체임을 의미한다.

- Entity
  - 실제 DB 테이블과 1:1로 매핑
  - 상속받거나 구현체여서는 안되며, 테이블의 컬럼과 같아야함. 가장 Core한 클래스
  - 절대로 요청, 응답값을 전달하는 클래스를 사용해선 안됨.
  
  2024년 나는 월 500을 재밌게 일하며 번다.
  2024년 나는 복근, 애플힙, 도자기 피부를 가졌다.
  2024년 나는 행복한 연애 중이다.
  2024년 나는 개발 실력이 폭풍 성장했다.
  2024년 나는 50평의 전망 좋은 집에서 산다.

  2024년 나는 월 500을 재밌게 일하며 번다.
  2024년 나는 복근, 애플힙, 도자기 피부를 가졌다.
  2024년 나는 행복한 연애 중이다.
  2024년 나는 개발 실력이 폭풍 성장했다. 개발 운영, 개선 을 한다.
  2024년 나는  50평의 전망 좋은 집에서 산다.

  살 거 : 수분크림, 키보드

  2024년 나는 월 5000을 재밌게 일하며 번다.
  2024년 나는 복근, 애플힙, 도자기 피부를 가졌다.
  2024년 나는 성장하는 연애 중이다
  2024년 나는 개발 실력이 폭풍 성장했다.
  2024년 나는 50평의 전망 좋은 집에서 산다.
  
