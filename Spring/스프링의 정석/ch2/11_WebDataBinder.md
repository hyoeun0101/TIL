요청 데이터를 `WebDataBinder`에서 처리(`타입 변환`, `데이터 검증`)를 한 후 그 결과를 `BindingResult`에 저장한다.

## 🍎 요청 데이터를 자바 객체로 변환하는 법
- 모든 요청 데이터는 String으로 들어온다.
- 같은 키의 값이 여러 개면 String 배열로 들어온다.
### 1. PropertyEditor
- 양방향으로 타입 변환.(String -> 타입, 타입 -> String)
- 특정 타입이나 특정 필드에 적용 가능
```java 
@InitBinder
public void toDate(WebDataBinder binder){
    //특정 필드 hobby에만 적용하기
    binder.registerCustomEditor(String[].class,"hobby" new StringArrayPropertyEditor("#"));
}
```
- 디폴트 PropertyEditor는 스프링이 기본적으로 제공.(여러 PropertyEditor를 지원하니 필요할 때 구글링하여 사용하기!)
- 커스텀 PropertyEditor는 사용자가 직접 구현. (PropertyEditorSupport를 상속하면 편리)
- 모든 컨트롤러 내에서 변환- `WebBindingInitializer`를 구현 후 등록
- 특정 컨트롤러 내에서 변환- 컨트롤러에 @InitBinder 붙은 메서드 작성
```java 
@InitBinder
public void toDate(WebDataBinder binder){
    //포맷 형식
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    //스프링이 제공하는 CustomDateEditor 사용하여 변환. String -> Date
    binder.registerCustomEditor(Date.class, new CustomDateEditor(df,false));
    // String을 # 기준으로 나누어 String 배열로 변환
    binder.registerCustomEditor(String[].class, new StringArrayPropertyEditor("#"));
}
```
### 2. Convertor
- 단방향 타입 변환.(타입A -> 타입B)
- PropertyEditor 단점을 개선하였다.
- `ConversionService` 이라는 타입 변환하는 서비스를 제공한다.  WebDataBinder에 `DefaultFormattingConversionService`가 기본 등록되어 있다.
- 모든 컨트롤러 변환- ConfigurableWebBindingInitializer를 설정
- 특정 컨트롤러 변환 - 컨트롤러에 @InitBinder 붙은 메서드 작성
```java
public class StringToStringArrayConverter impements Converter<String,String[]>{
    @Override
    public String[] convert(String source){
        return source.split("#");//String -> String[]
    }
}
```
- 위의 Converter를 ConversionService에 등록하여 사용한다.
- 스프링이 제공하는 ConversionService 출력하기
```java
@InitBinder
public void toDate(WebDataBinder binder){
    ConversionService cs = binder.getConversionService();
    // cs를 출력해보면 스프링이 제공하는 ConversionService를 볼 수 있다.
}
```


### 3. Formmater
- 양방향 타입 변환(String-> 타입, 타입 -> String)
- 바인딩할 필드에 적용 - @NumberFormat, @DateTimeFormat

```java
public interface Formatter<T> extends Printer<T>, Parser<T>{}
public Printer<T>{
    String print(T object, Locale lacale);//Object -> String
}
public Parser<T>{
    T parse(String text, Locale locale) throws ParseException;//String -> Object
}
```
```java
@DateTimeFormat(pattern="yyyy-MM-dd")//2022-01-01
Date birth;

@NumberFormat(parttern="###,###")// 123,456
BigDecimal salary;
```
### 타입 변환 우선 순위
  - 커스텀 PropertyEditor
  - ConversionService
  - 디폴트 PropertyEditor
---

## 🍎 데이터 검증

- 검증이란 관심사를 분리하여 작성하자.

### Validator
: 객체를 검증하기 위한 인터페이스.
```java
public interface Validator{
    //검증 가능한 객체인가
    boolean supports (Class<?> clazz);
    //객체 검증 - target: 검증할 객체, errers: 검증시 발생한 에러 저장소
    void validate(@Nullable Object target, Errors errors);
}
```

### Errors 인터페이스

- BindingResult는 Errors의 자손

```java
public interface Errors{
    //객체 전체에 대한 에러 "id 또는 pwd 일치하지 않습니다."
    void reject(String errorCode);
    //필드: 에러 , 필드에 대한 에러."id" "유효하지 않습니다."
    void rejectValue(String field, String errorCode);
}
```

### - UserValidator 작성

```java
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class UserValidator implements Validator{

    @Override
    public boolean supports(Class<?> clazz) {
        //clazz가 User또는 자손인지 검증
        return User.class.isAssignableFrom(clazz);
        //return User.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		System.out.println("UserValidator.validate() is called");

		User user= (User)target;// 검증할 User 객체 들어옴. 사실 supports에서 검증해주기 때문에 instanceof 필요없음.
		String id = user.getId();
		//id가 비었거나 공백이면,"id"필드에서 "required"라는 에러 코드를 저장.
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "id", "required");
		ValidationUtils.rejectIfEmpty(errors, "pwd", "required");
		 //id가 null, 길이가 5~12사이가 아니라면, "id"필드에 "invalidLength"라는 에러 코드로 저장.
		if(id==null|| id.length()<5 || id.length()>12) {
			errors.rejectValue("id","invalidLegnth",new String[]("5","12"));
		}
	}
}
```

### - 검증 방법

1. 수동으로 검증 - 메서드 내에서 UserValidator()호출

```java
UserValidator uv = new UserValidator();
uv.validate(user,result);//검증.
if(result.hasErrors())//에러가 있으면,
    return "registerForm";
```

2. 자동으로 검증 - @InitBinder

```java
@InitBinder
public void toDate(WebDataBinder binder){
    //setValidator로 WebDataBinder에 UserValidator를 등록
    binder.setValidator(new UserValidator());
}

@PostMapping("register/add")
public String save(@Valid User user,BindingResult result){}

```

- @InitBinder에 검증 객체 등록하고, 검증할 객체 앞에 @Valid 붙여주기
- maven repository- Bean Validation API

## 🍎 글로벌 Validator

- 하나의 Validator로 여러 객체를 검증할 때, 글로벌 Validator로 등록.

1. servlet-context.xml에 추가 - 글로벌 Validator 등록

```xml
<annotation-driven validator="globalValidator" />
<!-- <beans:bean id="globalValidator" class="GlobalValidator 위치" /> -->
<beans:bean id="globalValidator" class="com.hyoding.ch2.GlobalValidator" />
```

2. 글로벌 Validator와 로컬 Validator를 동시에 적용

```java
@InitBinder
public void toDate(WebDataBinder binder){
    //로컬 Validator 추가
    binder.addValidators(new UserValidator());
}
```


## 🍎MessageSource
- 다양한 리소스(파일, 배열 등)에서 메시지를 읽기 위한 인터페이스.
- 어떤 코드를 주면 코드에 대한 메세지를 문자열로 반환
- Locale

```java
public interface MessageSource{
    String getMessage(String code, Object[] args, String defaultMessage, Locale locale);
    String getMessage(String code, Object[] args, Locale locale) throws NoSuchMessageException;
    String getMessage(MessageSourceResolvable resolvable, Locale locale) throws NoSuchMessageException;
}
```

- Locale : 지역 정보. 지역에서 사용하는 언어가 Locale 객체에 들어있음. 사용하는 시스템으로부터 얻을 수 있음. error_message_ko.properties 이런 파일 없으면 그냥 error_message.properties 가 디폴트.
- Object[] args 는 new String[]{"5","11"}이렇게 값을 넘겨 주는거.

1. 프로퍼티 파일을 메세지 소스로 하는 ResourceBundleMessageSource(MessageSource의 구현체)를 등록.

-> servlet-context.xml에 추가

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

2. src/main/resources/`error_message.properties` 파일 생성

```
required=필수 항목 입니다.
required.user.pwd=비밀번호는 필수 항목입니다.
invalidLength.id=아이디 길이는 {0}~{1}사이여야 합니다.
```

"id"필드에 에러가 발생하면, "required"  
1순위 required.user.id  
2순위 required.id  
3순위 required.java.lang.String  
4순위 required  
5순위 defaultMessage

### - 검증 메세지 출력

```html
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!-- 검증할 객체=user. 이렇게 바뀜. <form id="user" action="/ch2/register/add" method="post"> -->
<form:form modelAttribute="user">
  <form:errors path="id" cssClss="msg" />
  <!-- 이렇게 바뀜.<span id="id.errors" class="msg">필수 입력 항목입니다.</span> -->
</form:form>
```
