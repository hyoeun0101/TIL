데이터가 문자열로 요청이 들어오면, `WebDataBinder`에서 타입 변환 후 BindingReuslt에 저장,
두 번째로 데이터 검증. 에러가 없으면 값 저장하고, 에러가 있으면 BindingReuslt 에 저장.

__실습__
RegisterController   회원가입 구현하기   

### 타입 변환
1. PropertyEditor : 양방향으로 타입 변환.
    - 디폴트 PropertyEditor: 스프링이 기본 제공
    - 커스텀 PropertyEditor : 사용자가 직접 구현. PropertyEditorSupport를 상속 

propertydeitors 서칭해보기.

2. Converter : 단방향 타입 변환. PropertyEditor 단점을 개선.  
```java
public class StringToStringArrayConverter impements Converter<String,String[]>{
    @Override
    //String -> String[]
    public String[] convert(String source){
        return source.split("#");
    }
}
```
위의 Converter를 ConversionService에 등록.

3. Formmater : 양방향 타입 변환
```java
@DateTimeFormat(pattern="yyyy/MM/dd")
Date birth;

//123,456 이렇게 들어오면 숫자로
@NumberFormat(parttern="###,###")
BigDecimal salary;
```
- 우선 순위
    - 커스텀 PropertyEditor
    - ConversionService
    - 디폴트 PropertyEditor

### 2. 데이터 검증
-> Validator : 객체를 검증하기 위한 인터페이스. 

https://eunoo.tistory.com/18


- MessageSource : 다양한 리소스(파일, 배열 등)에서 메시지를 읽기 위한 인터페이스.

src/main/resources/error_message.properties 추가
```
required=필수 항목 입니다.
required.user.pwd=비밀번호는 필수 항목입니다.
invalidLength.id=아이디 길이는 {0}~{1}사이여야 합니다.
```

servlet-context.xml
```xml
<beans:bean id="messageSource" class="org.springframework.context.support.ResourceBundleMessageSource">
		<beans:property name="basenames">
			<beans:list>
				<beans:value>error_message</beans:value> <!-- /src/main/resources/error_message.properties -->
			</beans:list>
		</beans:property>
		<beans:property name="defaultEncoding" value="UTF-8"/>
	</beans:bean>

```
검증 메세지 출력
```html
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<form:form modelAttribute="user">
<!-- 이렇게 바뀜.<form id="user" action="/ch2/register/add" method="post"> -->

<form:errors path="id" cssClss="msg"/>
<!-- 이렇게 바뀜.<span id="id.errors" class="msg">필수 입력 항목입니다.</span> -->
```